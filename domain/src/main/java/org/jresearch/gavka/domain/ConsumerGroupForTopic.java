package org.jresearch.gavka.domain;

import java.util.HashMap;
import java.util.Map;

public class ConsumerGroupForTopic {

	private String groupId;
	private Map<Integer, PartitionInfoForConsumerGroup> partitionInfo = new HashMap<>();

	public ConsumerGroupForTopic() {
		super();
	}

	public ConsumerGroupForTopic(final String groupId, final Map<Integer, PartitionInfoForConsumerGroup> partitionInfo) {
		super();
		this.groupId = groupId;
		this.partitionInfo = partitionInfo;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(final String groupId) {
		this.groupId = groupId;
	}

	public Map<Integer, PartitionInfoForConsumerGroup> getPartitionInfo() {
		return partitionInfo;
	}

	public void setPartitionInfo(final Map<Integer, PartitionInfoForConsumerGroup> partitionInfo) {
		this.partitionInfo = partitionInfo;
	}

	public void addPartitionInfo(final Integer p, final PartitionInfoForConsumerGroup v) {
		partitionInfo.put(p, v);
	}

	public void removePartitionInfo(final Integer p) {
		partitionInfo.remove(p);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((partitionInfo == null) ? 0 : partitionInfo.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ConsumerGroupForTopic other = (ConsumerGroupForTopic) obj;
		if (groupId == null) {
			if (other.groupId != null) {
				return false;
			}
		} else if (!groupId.equals(other.groupId)) {
			return false;
		}
		if (partitionInfo == null) {
			if (other.partitionInfo != null) {
				return false;
			}
		} else if (!partitionInfo.equals(other.partitionInfo)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ConsumerGroupForTopic [groupId=" + groupId + ", partitionInfo=" + partitionInfo + "]";
	}

}
