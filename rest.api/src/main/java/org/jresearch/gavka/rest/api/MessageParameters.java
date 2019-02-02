package org.jresearch.gavka.rest.api;

import javax.annotation.Nullable;

import org.jresearch.commons.gwt.shared.model.time.GwtLocalDateTimeModel;
import org.jresearch.gavka.domain.KeyFormat;
import org.jresearch.gavka.domain.MessageFormat;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class MessageParameters {

	private String topic;
	private String key;
	@Nullable
	private GwtLocalDateTimeModel from;
	private KeyFormat keyFormat;
	private MessageFormat messageFormat;

	public String getTopic() {
		return topic;
	}

	public void setTopic(final String topic) {
		this.topic = topic;
	}

	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	@Nullable
	public GwtLocalDateTimeModel getFrom() {
		return from;
	}

	public void setFrom(@Nullable final GwtLocalDateTimeModel from) {
		this.from = from;
	}

	public KeyFormat getKeyFormat() {
		return keyFormat;
	}

	public void setKeyFormat(final KeyFormat keyFormat) {
		this.keyFormat = keyFormat;
	}

	public MessageFormat getMessageFormat() {
		return messageFormat;
	}

	public void setMessageFormat(final MessageFormat messageFormat) {
		this.messageFormat = messageFormat;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("topic", topic)
				.add("key", key)
				.add("from", from)
				.add("keyFormat", keyFormat)
				.add("messageFormat", messageFormat)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getTopic(), getKey(), getFrom(), getKeyFormat(), getMessageFormat());
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof MessageParameters) {
			final MessageParameters that = (MessageParameters) object;
			return Objects.equal(this.getTopic(), that.getTopic())
					&& Objects.equal(this.getKey(), that.getKey())
					&& Objects.equal(this.getFrom(), that.getFrom())
					&& Objects.equal(this.getKeyFormat(), that.getKeyFormat())
					&& Objects.equal(this.getMessageFormat(), that.getMessageFormat());
		}
		return false;
	}

}
