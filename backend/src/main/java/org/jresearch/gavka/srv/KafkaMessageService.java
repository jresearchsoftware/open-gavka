package org.jresearch.gavka.srv;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.jresearch.gavka.domain.ConsumerGroupForTopic;
import org.jresearch.gavka.domain.KeyFormat;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.domain.MessageFilter;
import org.jresearch.gavka.domain.MessageFormat;
import org.jresearch.gavka.domain.PartitionInfoForConsumerGroup;
import org.jresearch.gavka.domain.PartitionOffsetInfo;
import org.jresearch.gavka.domain.TopicInfo;
import org.jresearch.gavka.rest.api.MessagePortion;
import org.jresearch.gavka.rest.api.PagingParameters;
import org.jresearch.gavka.rest.api.PartitionOffset;
import org.jresearch.gavka.srv.ConnectionService.KafkaVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.base.MoreObjects;

@Profile("default")
@Component
@SuppressWarnings("nls")
public class KafkaMessageService extends AbstractMessageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageService.class);

	@Autowired
	private ConnectionService connectionService;

	protected AdminClient getClient(final String connectionId) {
		final Properties props = connectionService.getKafkaConnectionProperties(connectionId, KafkaVersion.LATEST).orElseGet(Properties::new);
		return AdminClient.create(props);
	}

	@Override
	@SuppressWarnings({ "null" })
	public MessagePortion getMessages(final String connectionId, final PagingParameters pagingParameters, final MessageFilter filter) {
		final Properties props = getProperties(connectionId);

		props.put("key.deserializer", getKeyDeserializer(filter.getKeyFormat()));
		props.put("value.deserializer", getMessageDeserializer(filter.getMessageFormat()));
		props.put("auto.offset.reset", "earliest");
		LOGGER.debug("Retreiving data from topic : {} ", filter.getTopic());
		try (final KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(props)) {
			final Map<Integer, Long> partitionOffsets = new HashMap<>();
			final Map<Integer, TopicPartition> partitions = initConsumer(filter, consumer);

			for (final TopicPartition tp : partitions.values()) {
				partitions.put(tp.partition(), tp);
				LOGGER.debug("initial offset: partition {}, position {} ", tp.partition(), consumer.position(tp));
				partitionOffsets.put(tp.partition(), consumer.position(tp));
			}
			// if the client sends partitions offsets then position to that
			// offsets
			if (!pagingParameters.getPartitionOffsets().isEmpty()) {
				pagingParameters.getPartitionOffsets().stream().forEach(p -> {
					LOGGER.debug("positioning offset from client partition {}, position {} ", p.getPartition(),
							p.getOffset());
					partitionOffsets.put(p.getPartition(), p.getOffset());
					consumer.seek(partitions.get(p.getPartition()), p.getOffset());
				});
			} else {
				positionConsumer(partitions, filter, consumer, partitionOffsets);
			}
			final List<Message> messages = new ArrayList<>();

			ConsumerRecords<Object, Object> records = consumer.poll(1000);
			final int pagesSize = pagingParameters.getAmount();
			while (messages.size() < pagesSize && !records.isEmpty()) {
				LOGGER.debug("Getting {} records ", records.count());
				for (final ConsumerRecord<Object, Object> consumerRecord : records) {
					if (messages.size() == pagesSize) {
						break;
					}
					String stringKey = "";
					if (consumerRecord.key() != null) {
						stringKey = consumerRecord.key().toString();
					}
					if (!filter.getKey().isEmpty() && !filter.getKey().equals(stringKey)) {
						partitionOffsets.put(consumerRecord.partition(), consumerRecord.offset() + 1);
						continue;
					}
					String stringValue = "";
					if (consumerRecord.value() != null) {
						stringValue = consumerRecord.value().toString();
					}
					messages.add(new Message(stringKey, stringValue, consumerRecord.offset(),
							consumerRecord.partition(), consumerRecord.timestamp()));
					partitionOffsets.put(consumerRecord.partition(), consumerRecord.offset() + 1);
				}
				records = consumer.poll(1000);
			}
			final List<PartitionOffset> po = new ArrayList<>();
			for (final Integer partitionOffset : partitionOffsets.keySet()) {
				final PartitionOffset p = new PartitionOffset();
				p.setOffset(partitionOffsets.get(partitionOffset));
				p.setPartition(partitionOffset);
				po.add(p);
			}
			LOGGER.debug("Message offsets for topic {}:{} ", filter.getTopic(), po);
			return new MessagePortion(po, messages);
		} catch (final Exception e) {
			LOGGER.error("Exception reading records", e);
			return null;
		}
	}

	protected Map<Integer, TopicPartition> initConsumer(final MessageFilter filter, final KafkaConsumer<Object, Object> consumer) {
		final Map<Integer, TopicPartition> partitions = new HashMap<>();
		// get all partitions for the topic
		for (final PartitionInfo partition : consumer.partitionsFor(filter.getTopic())) {
			final TopicPartition tp = new TopicPartition(filter.getTopic(), partition.partition());
			partitions.put(partition.partition(), tp);
		}
		consumer.assign(partitions.values());
		return partitions;
	}

	private static void positionConsumer(final Map<Integer, TopicPartition> partitions, final MessageFilter filter, final KafkaConsumer<Object, Object> consumer, final Map<Integer, Long> partitionOffsets) {
		if (filter.getFrom() == null) {
			// no start time, position to the beginning
			consumer.seekToBeginning(partitions.values());
		} else {
			// position to time offsets
			final Map<TopicPartition, Long> query = new HashMap<>();
			final long out = Date.from(filter.getFrom().atZone(ZoneId.of("UTC")).toInstant()).getTime();
			for (final TopicPartition topicPartition : partitions.values()) {
				query.put(topicPartition, out);
			}
			final Map<TopicPartition, OffsetAndTimestamp> result = consumer.offsetsForTimes(query);
			result.entrySet().stream().forEach(entry -> {
				if (entry.getValue() != null) {
					final Long offset = entry.getValue().offset();
					final TopicPartition partition = entry.getKey();
					consumer.seek(partition, offset);
					partitionOffsets.put(partition.partition(), offset);
				} else {
					final TopicPartition partition = entry.getKey();
					consumer.seekToEnd(Collections.singleton(partition));
					partitionOffsets.put(partition.partition(), consumer.position(partition));
				}
			});
		}
	}

	@Override
	public List<String> getMessageTopics(final String connectionId) {
		List<String> list = new ArrayList<>();
		try (AdminClient kafkaClient = getClient(connectionId)) {
			list = new ArrayList<>(kafkaClient.listTopics().names().get());
		} catch (final InterruptedException | ExecutionException e) {
			LOGGER.error("Error getting topics", e);
		}
		Collections.sort(list);
		return list;
	}

	private static String getKeyDeserializer(final KeyFormat f) {
		switch (f) {
		case STRING:
			return "org.apache.kafka.common.serialization.StringDeserializer";
		case AVRO:
			return "io.confluent.kafka.serializers.KafkaAvroDeserializer";
		default:
			return "org.apache.kafka.common.serialization.BytesDeserializer";
		}

	}

	private static String getMessageDeserializer(final MessageFormat f) {
		switch (f) {
		case STRING:
			return "org.apache.kafka.common.serialization.StringDeserializer";
		case AVRO:
			return "io.confluent.kafka.serializers.KafkaAvroDeserializer";
		default:
			return "org.apache.kafka.common.serialization.BytesDeserializer";
		}

	}

	protected Properties getProperties(final String connectionId) {
		final Properties props = connectionService.getKafkaConnectionProperties(connectionId, KafkaVersion.LATEST).orElseGet(Properties::new);
		props.put("client.id", "gavka-tool");
		props.put("group.id", "gavka-tool-" + UUID.randomUUID());
		return props;
	}

	@Override
	public void exportMessages(final String connectionId, final OutputStream bos, final MessageFilter filter) throws IOException {
		final SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		final Properties props = getProperties(connectionId);
		props.put("key.deserializer", getKeyDeserializer(filter.getKeyFormat()));
		props.put("value.deserializer", getMessageDeserializer(filter.getMessageFormat()));
		props.put("auto.offset.reset", "earliest");

		final long stopTime = System.currentTimeMillis();
		long currentTime = 0;
		try (final KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(props)) {
			final Map<Integer, TopicPartition> partitions = initConsumer(filter, consumer);

			positionConsumer(partitions, filter, consumer, new HashMap<>());
			ConsumerRecords<Object, Object> records = consumer.poll(1000);
			while (!records.isEmpty()) {
				for (final ConsumerRecord<Object, Object> consumerRecord : records) {
					String stringKey = "";
					if (consumerRecord.key() != null) {
						stringKey = consumerRecord.key().toString();
					}
					if (!filter.getKey().isEmpty() && !filter.getKey().equals(stringKey)) {
						continue;
					}
					String stringValue = "";
					if (consumerRecord.value() != null) {
						stringValue = consumerRecord.value().toString();
					}
					currentTime = consumerRecord.timestamp();
					bos.write(getStringForExport(new Message(stringKey, stringValue, consumerRecord.offset(),
							consumerRecord.partition(), consumerRecord.timestamp()), sf).getBytes());
				}
				bos.flush();
				if (currentTime >= stopTime) {
					break;
				}
				records = consumer.poll(1000);
			}

		}
	}

	@Override
	public TopicInfo getTopic(final String connectionId, final String topicName) {
		final TopicInfo ti = new TopicInfo();
		ti.setName(topicName);

		try (KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(getProperties(connectionId))) {
			final List<TopicPartition> partitions = consumer.partitionsFor(topicName).stream()
					.map(s -> new TopicPartition(topicName, s.partition())).collect(Collectors.toList());
			final Map<TopicPartition, Long> beginingOffsets = consumer.beginningOffsets(partitions);
			final Map<TopicPartition, Long> endOffsets = consumer.endOffsets(partitions);
			for (final TopicPartition topicPartition : partitions) {
				final int pNumber = topicPartition.partition();
				final PartitionOffsetInfo po = new PartitionOffsetInfo(pNumber, beginingOffsets.get(pNumber), endOffsets.get(pNumber));
				ti.addPartition(pNumber, po);
			}
		}
		try (AdminClient kafkaClient = getClient(connectionId)) {
			final List<ConsumerGroupForTopic> cgf = new LinkedList<>();
			final List<String> groupIds = kafkaClient.listConsumerGroups().all().get().stream().map(s -> s.groupId())
					.collect(Collectors.toList());
			final Map<String, ConsumerGroupDescription> groups = kafkaClient.describeConsumerGroups(groupIds).all().get();
			for (final String groupId : groupIds) {
				final ConsumerGroupDescription descr = groups.get(groupId);
				final Optional<TopicPartition> tp = descr.members().stream().map(s -> s.assignment().topicPartitions())
						.flatMap(coll -> coll.stream()).filter(s -> s.topic().equals(topicName)).findAny();
				if (tp.isPresent()) {
					final ConsumerGroupForTopic gr = new ConsumerGroupForTopic();
					gr.setGroupId(groupId);
					kafkaClient.listConsumerGroupOffsets(groupId).partitionsToOffsetAndMetadata().get()
							.forEach((k, v) -> {
								if (k.topic().equals(topicName)) {
									final PartitionInfoForConsumerGroup pi = new PartitionInfoForConsumerGroup();
									pi.setCurrentOffset(v.offset());
									final PartitionOffsetInfo po = ti.getPartitions().get(k.partition());
									if (po == null) {
										pi.setLag(v.offset());
									} else {
										pi.setLag(po.getEndOffset() - v.offset());
									}
									gr.addPartitionInfo(k.partition(), pi);
								}

							});
					cgf.add(gr);
				}
			}
			ti.setConsumerGroups(cgf);

		} catch (final Exception e) {
			LOGGER.error("Exception getting consumer groups", e);
		}
		return ti;
	}

	protected static String getStringForExport(final Message message, final SimpleDateFormat sf) {
		return MoreObjects.toStringHelper(Message.class).add("key", message.getKey()).add("value", message.getValue())
				.add("offset", message.getOffset()).add("partition", message.getPartition())
				.add("timestamp", sf.format(new Date(message.getTimestamp()))).toString() + "\n";

	}
}
