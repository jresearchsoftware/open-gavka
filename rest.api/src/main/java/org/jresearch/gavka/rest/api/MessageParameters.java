package org.jresearch.gavka.rest.api;

import org.jresearch.commons.gwt.shared.model.time.GwtLocalDateModel;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class MessageParameters {

	private String topic;
	private GwtLocalDateModel from;
	private GwtLocalDateModel to;
	private boolean avro;

	public String getTopic() {
		return topic;
	}

	public void setTopic(final String topic) {
		this.topic = topic;
	}

	public GwtLocalDateModel getFrom() {
		return from;
	}

	public void setFrom(final GwtLocalDateModel from) {
		this.from = from;
	}

	public GwtLocalDateModel getTo() {
		return to;
	}

	public void setTo(final GwtLocalDateModel to) {
		this.to = to;
	}

	public boolean isAvro() {
		return avro;
	}

	public void setAvro(final boolean avro) {
		this.avro = avro;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("topic", topic)
				.add("from", from)
				.add("to", to)
				.add("avro", avro)
				.toString();
	}

	@SuppressWarnings("boxing")
	@Override
	public int hashCode() {
		return Objects.hashCode(getTopic(), getFrom(), getTo(), avro);
	}

	@SuppressWarnings("boxing")
	@Override
	public boolean equals(final Object object) {
		if (object instanceof MessageParameters) {
			final MessageParameters that = (MessageParameters) object;
			return Objects.equal(this.getTopic(), that.getTopic())
					&& Objects.equal(this.getFrom(), that.getFrom())
					&& Objects.equal(this.getTo(), that.getTo())
					&& Objects.equal(this.avro, that.avro);
		}
		return false;
	}

}
