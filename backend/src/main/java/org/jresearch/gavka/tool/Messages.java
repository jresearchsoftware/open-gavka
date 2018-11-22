package org.jresearch.gavka.tool;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.RandomStringUtils;
import org.jresearch.gavka.domain.Message;

import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

@SuppressWarnings("nls")
@Slf4j
public class Messages {

	private static final Random RANDOM = new Random();

	private static final List<Message> messages;
	private static final List<String> topics;

	static {
		log.debug("Generates Topics");
		topics = StreamEx.generate(Messages::generateTopic).limit(10).toList();
		log.debug("Generates Messages");
		messages = StreamEx.generate(Messages::generateMessage).limit(1000).toList();
		log.debug("All generations are done");
	}

	private Messages() {
		// prevent from instantiation
	}

	public static List<Message> getMessages() {
		return messages;
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

	@SuppressWarnings("null")
	@Nonnull
	private static String pad(@Nonnull final String toPad) {
		return Strings.padStart(toPad, 2, '0');
	}

}
