package org.jresearch.gavka.srv;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.jresearch.commons.gwt.shared.loader.PageLoadResultBean;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.rest.api.PagingParameters;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageService implements MessageService {

	protected AdminClient kafkaClient;

	public KafkaMessageService() {
		final Properties props = new Properties();
		props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

		kafkaClient = AdminClient.create(props);
	}

	@Override
	@SuppressWarnings({ "null" })
	public PageLoadResultBean<Message> getMessages(final PagingParameters pagingParameters, final String topic, final LocalDate from, final LocalDate to, final boolean avro) {
		final Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		props.put("client.id", "gavka-tool");
		props.put("group.id", "gavka-tool");
		props.put("auto.offset.reset", "earliest");

		if (avro) {
			props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
		} else {
			props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		}
		final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singleton(topic));
		final Set<TopicPartition> assignments = consumer.assignment();
		assignments.forEach(tp -> consumer.seekToBeginning(assignments));
		final List<Message> messages = new ArrayList<>();
		final ConsumerRecords<String, String> records = consumer.poll(1000);
		for (final ConsumerRecord<String, String> consumerRecord : records) {
			messages.add(new Message(consumerRecord.key(), consumerRecord.value(), consumerRecord.offset()));
		}
		consumer.close();
		final int offset = pagingParameters.getOffset();
		return new PageLoadResultBean<>(offset, messages.size(), messages.subList(offset, offset + pagingParameters.getAmount()));
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

}
