package org.jresearch.gavka.domain;

import java.util.List;
import java.util.Map;

public class TopicInfo {

	private String name;
	
	private List<ConsumerGroupForTopic> consumerGroups;

	private Map<Integer,PartitionOffsetInfo> partitions;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ConsumerGroupForTopic> getConsumerGroups() {
		return consumerGroups;
	}

	public void setConsumerGroups(List<ConsumerGroupForTopic> consumerGroups) {
		this.consumerGroups = consumerGroups;
	}

	public Map<Integer, PartitionOffsetInfo> getPartitions() {
		return partitions;
	}

	public void setPartitions(Map<Integer, PartitionOffsetInfo> partitions) {
		this.partitions = partitions;
	}
	
	public void addPartition(Integer number, PartitionOffsetInfo po) {
		partitions.put(number, po);
	}
	
	public void removePartition(Integer number) {
		partitions.remove(number);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((consumerGroups == null) ? 0 : consumerGroups.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((partitions == null) ? 0 : partitions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TopicInfo other = (TopicInfo) obj;
		if (consumerGroups == null) {
			if (other.consumerGroups != null)
				return false;
		} else if (!consumerGroups.equals(other.consumerGroups))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (partitions == null) {
			if (other.partitions != null)
				return false;
		} else if (!partitions.equals(other.partitions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TopicInfo [name=" + name + ", consumerGroups=" + consumerGroups + ", partitions=" + partitions + "]";
	}
	
	
}
