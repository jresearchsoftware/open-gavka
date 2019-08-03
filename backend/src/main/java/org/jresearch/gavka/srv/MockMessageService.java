package org.jresearch.gavka.srv;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.jresearch.gavka.domain.ConsumerGroupForTopic;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.domain.MessageFilter;
import org.jresearch.gavka.domain.PartitionInfoForConsumerGroup;
import org.jresearch.gavka.domain.PartitionOffsetInfo;
import org.jresearch.gavka.domain.TopicInfo;
import org.jresearch.gavka.rest.api.MessagePortion;
import org.jresearch.gavka.rest.api.PagingParameters;
import org.jresearch.gavka.tool.Messages;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

@Profile("nokafka")
@Component
public class MockMessageService extends AbstractMessageService {

	long currentOffset = 0;
	long lag = 0;
	long startOffset = 100;
	long endOffset = 200;

	@Override
	public MessagePortion getMessages(final String connectionId, final PagingParameters pagingParameters, final MessageFilter filter) {
		final List<Message> messages = "no".equalsIgnoreCase(filter.key()) ? ImmutableList.of() : Messages.getMessages();
		return new MessagePortion(ImmutableList.of(), messages);
	}

	@Override
	public List<String> getMessageTopics(final String connectionId) {
		return Messages.getTopics();
	}

	@Override
	public void exportMessages(final String connectionId, final OutputStream bos, final MessageFilter filter) throws KafkaException {
		try {
			final List<Message> messages = Messages.getMessages();
			for (final Message message : messages) {
				bos.write(message.toString().getBytes());
			}
		} catch (final IOException e) {
			throw new KafkaException(e.getMessage(), e);
		}
	}

	@Override
	public TopicInfo getTopic(final String connectionId, final String topicName) {
		final Map<Integer, PartitionInfoForConsumerGroup> partitionInfo = ImmutableMap.of(1, new PartitionInfoForConsumerGroup(1, currentOffset++, lag++));
		final List<ConsumerGroupForTopic> consumerGroups = ImmutableList.of(new ConsumerGroupForTopic("groupId", partitionInfo));
		final Map<Integer, PartitionOffsetInfo> partitions = ImmutableMap.of(1, new PartitionOffsetInfo(1, startOffset++, endOffset++));
		return new TopicInfo(topicName, consumerGroups, partitions);
	}

}
