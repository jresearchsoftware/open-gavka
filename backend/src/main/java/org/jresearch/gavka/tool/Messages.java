package org.jresearch.gavka.tool;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.RandomStringUtils;
import org.jresearch.gavka.domain.Message;

import com.google.common.base.Strings;

import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

public class Messages {

	private static final List<Message> messages;
	private static final List<String> topics;

	static {
		messages = StreamEx.generate(Messages::generateMessage).limit(1000).toList();
		topics = StreamEx.generate(Messages::generateTopic).limit(10).toList();
	}

	private Messages() {
		// prevent from instantiation
	}

	public static List<Message> getMessages(final String topic, final LocalDate date) {
		return messages;
	}

	public static List<String> getTopics() {
		return topics;
	}

	@SuppressWarnings("null")
	private static Message generateMessage() {
		try {
			final SecureRandom r = SecureRandom.getInstanceStrong();
			final String key = RandomStringUtils.randomAlphanumeric(r.nextInt(6) + 5);
			final String value = IntStreamEx
					.of(r, 100, 0, 256)
					.mapToObj(Integer::toHexString)
					.map(Messages::pad)
					.map(String::toUpperCase)
					.joining(" "); //$NON-NLS-1$
			return new Message(key, value, r.nextLong());
		} catch (final NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}

	@SuppressWarnings("null")
	private static String generateTopic() {
		try {
			final SecureRandom r = SecureRandom.getInstanceStrong();
			return RandomStringUtils.randomAlphanumeric(r.nextInt(6) + 5);
		} catch (final NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}

	@Nonnull
	private static String pad(@Nonnull final String toPad) {
		return Strings.padStart(toPad, 2, '0');
	}

}
