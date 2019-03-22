package org.jresearch.gavka.domain;

public class PartitionInfoForConsumerGroup {

	private int partition;
	
	private long currentOffset;
	
	private long lag;
	
	public PartitionInfoForConsumerGroup() {
		super();
	}
	
	public PartitionInfoForConsumerGroup(int partition, long currentOffset, long lag) {
		super();
		this.partition = partition;
		this.currentOffset = currentOffset;
		this.lag = lag;
	}

	public long getCurrentOffset() {
		return currentOffset;
	}

	public void setCurrentOffset(long currentOffset) {
		this.currentOffset = currentOffset;
	}

	public long getLag() {
		return lag;
	}

	public void setLag(long lag) {
		this.lag = lag;
	}


	public int getPartition() {
		return partition;
	}


	public void setPartition(int partition) {
		this.partition = partition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (currentOffset ^ (currentOffset >>> 32));
		result = prime * result + (int) (lag ^ (lag >>> 32));
		result = prime * result + partition;
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
		PartitionInfoForConsumerGroup other = (PartitionInfoForConsumerGroup) obj;
		if (currentOffset != other.currentOffset)
			return false;
		if (lag != other.lag)
			return false;
		if (partition != other.partition)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PartitionInfoForConsumerGroup [partition=" + partition + ", currentOffset=" + currentOffset + ", lag="
				+ lag + "]";
	}

	
	
}
