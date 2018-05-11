package org.jresearch.gavka.rest.api;

import java.util.List;

import org.jresearch.gavka.domain.Message;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class MessagePortion {

	private List<PartitionOffset> partitionOffsets;
	private List<Message> messages;

	public MessagePortion() {
	}

	public MessagePortion(final List<PartitionOffset> partitionOffsets, final List<Message> messages) {
		this.partitionOffsets = partitionOffsets;
		this.messages = messages;
	}

	public List<PartitionOffset> getPartitionOffsets() {
		return partitionOffsets;
	}

	public void setPartitionOffsets(final List<PartitionOffset> partitionOffsets) {
		this.partitionOffsets = partitionOffsets;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(final List<Message> messages) {
		this.messages = messages;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("partitionOffsets", partitionOffsets)
				.add("messages", messages)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getPartitionOffsets(), getMessages());
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof MessagePortion) {
			final MessagePortion that = (MessagePortion) object;
			return Objects.equal(this.getPartitionOffsets(), that.getPartitionOffsets())
					&& Objects.equal(this.getMessages(), that.getMessages());
		}
		return false;
	}

}
