package org.jresearch.gavka.tool;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.RandomStringUtils;
import org.jresearch.gavka.domain.ConsumerGroupForTopic;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.domain.PartitionInfoForConsumerGroup;
import org.jresearch.gavka.domain.PartitionOffsetInfo;
import org.jresearch.gavka.domain.TopicInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

@SuppressWarnings("nls")
public class Messages {

	private static final Logger LOGGER = LoggerFactory.getLogger(Messages.class);

	private static final Random RANDOM = new Random();

	private static final List<Message> messages;
	private static final List<String> topics;
	private static final Map<String, TopicInfo> topicInfo;

	static {
		LOGGER.debug("Generates Topics");
		topics = StreamEx.generate(Messages::generateTopic).limit(10).toList();
		LOGGER.debug("Generates Messages");
		messages = StreamEx.generate(Messages::generateMessage).limit(1000).toList();
		topicInfo = StreamEx.of(topics).mapToEntry(Messages::generateTopicInfo).toMap();
		LOGGER.debug("All generations are done");
	}

	private Messages() {
		// prevent from instantiation
	}

	public static List<Message> getMessages() {
		return messages;
	}

	public static TopicInfo getTopicInfo(final String s) {
		return topicInfo.get(s);
	}

	public static List<String> getTopics() {
		return topics;
	}

	@SuppressWarnings("null")
	private static Message generateMessage() {
		final String key = RandomStringUtils.randomAlphanumeric(RANDOM.nextInt(6) + 5);
		final String value = IntStreamEx
				.of(RANDOM, 100, 0, 256)
				.mapToObj(Integer::toHexString)
				.map(Messages::pad)
				.map(String::toUpperCase)
				.joining(" "); //$NON-NLS-1$
		return new Message(key, value, RANDOM.nextLong(), RANDOM.nextInt(3), System.currentTimeMillis());
	}

	private static String generateTopic() {
		return RandomStringUtils.randomAlphanumeric(RANDOM.nextInt(6) + 5);
	}

	private static TopicInfo generateTopicInfo(final String name) {
		final TopicInfo ti = new TopicInfo();
		ti.setName(name);
		ti.setPartitions(IntStreamEx.of(0, 10).mapToObj(Integer::new).mapToEntry(Messages::generatePartitionOffsetInfo).toMap());
		ti.setConsumerGroups(StreamEx.generate(Messages::generateConsumerGroup).limit(5).toList());
		return ti;
	}

	private static PartitionInfoForConsumerGroup generatePartitionInfo(final int partition) {
		return new PartitionInfoForConsumerGroup(partition, RANDOM.nextLong(), RANDOM.nextLong());
	}

	private static ConsumerGroupForTopic generateConsumerGroup() {
		final ConsumerGroupForTopic cg = new ConsumerGroupForTopic();
		cg.setGroupId(RandomStringUtils.randomAlphanumeric(RANDOM.nextInt(6) + 5));
		cg.setPartitionInfo(IntStreamEx.of(0, 10).mapToObj(Integer::new).mapToEntry(Messages::generatePartitionInfo).toMap());
		return cg;
	}

	private static PartitionOffsetInfo generatePartitionOffsetInfo(final int p) {
		return new PartitionOffsetInfo(p, 0, RANDOM.nextLong());
	}

	@SuppressWarnings("null")
	@Nonnull
	private static String pad(@Nonnull final String toPad) {
		return Strings.padStart(toPad, 2, '0');
	}

}
