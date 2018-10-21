package org.jresearch.gavka.srv;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.jresearch.gavka.domain.KeyFormat;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.domain.MessageFilter;
import org.jresearch.gavka.domain.MessageFormat;
import org.jresearch.gavka.rest.api.MessagePortion;
import org.jresearch.gavka.rest.api.PagingParameters;
import org.jresearch.gavka.rest.api.PartitionOffset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.base.MoreObjects;

@Profile("default")
@Component
@SuppressWarnings("nls")
public class KafkaMessageService extends AbstractMessageService {

	protected AdminClient kafkaClient;
	
    @Value("${bootstrap.servers}")
 	private String serverUrl;

    @Value("${schema.registry.url}")
 	private String schemaRegistryUrl;
    
	public KafkaMessageService() {}
	
	@PostConstruct
	protected void initClient() {
		final Properties props = new Properties();
		props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, serverUrl);

		kafkaClient = AdminClient.create(props);

	}

	@Override
	@SuppressWarnings({ "null" })
	public MessagePortion getMessages(final PagingParameters pagingParameters, final MessageFilter filter) {
		final Properties props = new Properties();
		props.put("bootstrap.servers", serverUrl);
		props.put("schema.registry.url", schemaRegistryUrl);
		props.put("key.deserializer", getKeyDeserializer(filter.getKeyFormat()));
		props.put("value.deserializer", getMessageDeserializer(filter.getMessageFormat()));
		props.put("client.id", "gavka-tool");
		props.put("group.id", "gavka-tool-" + UUID.randomUUID());
		props.put("auto.offset.reset", "earliest");

		try (final KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(props)) {
			final Map<Integer, Long> partitionOffsets = new HashMap<>();
			final Map<Integer, TopicPartition> partitions = initConsumer(filter, consumer);

			for (final TopicPartition tp : partitions.values()) {
				partitions.put(tp.partition(), tp);
				partitionOffsets.put(tp.partition(), consumer.position(tp));
			}
			// if the client sends partitions offsets then position to that
			// offsets
			if (!pagingParameters.getPartitionOffsets().isEmpty()) {
				pagingParameters.getPartitionOffsets().stream().forEach(p -> {
					partitionOffsets.put(p.getPartition(), p.getOffset());
					consumer.seek(partitions.get(p.getPartition()), p.getOffset());
				});
			} else {
				positionConsumer(partitions, filter, consumer);
			}
			final List<Message> messages = new ArrayList<>();

			ConsumerRecords<Object, Object> records = consumer.poll(1000);
			final int pagesSize = pagingParameters.getAmount();
			while (messages.size() < pagesSize && !records.isEmpty()) {
				for (final ConsumerRecord<Object, Object> consumerRecord : records) {
					if (messages.size() == pagesSize) {
						break;
					}
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
					messages.add(new Message(stringKey, stringValue, consumerRecord.offset(),
							consumerRecord.partition(), consumerRecord.timestamp()));
					partitionOffsets.put(consumerRecord.partition(), consumerRecord.offset() + 1);
				}

				consumer.commitSync();
				records = consumer.poll(1000);
			}
			final List<PartitionOffset> po = new ArrayList<>();
			for (final Integer partitionOffset : partitionOffsets.keySet()) {
				final PartitionOffset p = new PartitionOffset();
				p.setOffset(partitionOffsets.get(partitionOffset));
				p.setPartition(partitionOffset);
				po.add(p);
			}
			return new MessagePortion(po, messages);
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

	protected void positionConsumer(final Map<Integer, TopicPartition> partitions, final MessageFilter filter,
			final KafkaConsumer<Object, Object> consumer) {
		if (filter.getFrom() == null) {
			// no start time, position to the beginning
			consumer.seekToBeginning(partitions.values());
		} else {
			// position to time offsets
			final Map<TopicPartition, Long> query = new HashMap<>();
			final long out = Date.from(filter.getFrom().atZone(ZoneId.systemDefault()).toInstant()).getTime();
			for (final TopicPartition topicPartition : partitions.values()) {
				query.put(topicPartition, out);
			}
			final Map<TopicPartition, OffsetAndTimestamp> result = consumer.offsetsForTimes(query);
			result.entrySet().stream().forEach(entry -> consumer.seek(entry.getKey(),
					Optional.ofNullable(entry.getValue()).map(OffsetAndTimestamp::offset).orElse(new Long(0))));
		}
	}

	@Override
	public List<String> getMessageTopics() {
		List<String> list = new ArrayList<>();
		try {
			list = new ArrayList<>(kafkaClient.listTopics().names().get());
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(list);
		return list;
	}

	private String getKeyDeserializer(final KeyFormat f) {
		switch (f) {
		case STRING:
			return "org.apache.kafka.common.serialization.StringDeserializer";
		case AVRO:
			return "io.confluent.kafka.serializers.KafkaAvroDeserializer";
		default:
			return "org.apache.kafka.common.serialization.BytesDeserializer";
		}

	}

	private String getMessageDeserializer(final MessageFormat f) {
		switch (f) {
		case STRING:
			return "org.apache.kafka.common.serialization.StringDeserializer";
		case AVRO:
			return "io.confluent.kafka.serializers.KafkaAvroDeserializer";
		default:
			return "org.apache.kafka.common.serialization.BytesDeserializer";
		}

	}

	@Override
	public void exportMessages(final OutputStream bos, final MessageFilter filter) throws IOException {
		final SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		final Properties props = new Properties();
		props.put("bootstrap.servers", serverUrl);
		props.put("schema.registry.url", schemaRegistryUrl);
		props.put("key.deserializer", getKeyDeserializer(filter.getKeyFormat()));
		props.put("value.deserializer", getMessageDeserializer(filter.getMessageFormat()));
		props.put("client.id", "gavka-tool");
		props.put("group.id", "gavka-tool-" + UUID.randomUUID());
		props.put("auto.offset.reset", "earliest");

		long stopTime = System.currentTimeMillis();
		long currentTime = 0;
		try (final KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(props)) {
			final Map<Integer, TopicPartition> partitions = initConsumer(filter, consumer);

			positionConsumer(partitions, filter, consumer);
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
					bos.write(getStringForExport(new Message(stringKey, stringValue, consumerRecord.offset(), consumerRecord.partition(),
							consumerRecord.timestamp()), sf).getBytes());
				}
				bos.flush();
				consumer.commitSync();
				if(currentTime >=stopTime) {
					break;
				}
				records = consumer.poll(1000);
			}

		}
	}

	protected String getStringForExport(final Message message, final SimpleDateFormat sf) {
		return MoreObjects.toStringHelper(Message.class)
				.add("key", message.getKey())
				.add("value", message.getValue())
				.add("offset", message.getOffset())
				.add("partition", message.getPartition())
				.add("timestamp", sf.format(new Date(message.getTimestamp())))
				.toString() + "\n";

	}
}
