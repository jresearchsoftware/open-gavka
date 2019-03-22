package org.jresearch.gavka.domain;

public class PartitionOffsetInfo {

	private long partitionNumber;
	
	private long startOffset;
	
	private long endOffset;

	public PartitionOffsetInfo() {
		super();
	}

	public PartitionOffsetInfo(long partitionNumber, long startOffset, long endOffset) {
		super();
		this.partitionNumber = partitionNumber;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}

	public long getPartitionNumber() {
		return partitionNumber;
	}

	public void setPartitionNumber(long partitionNumber) {
		this.partitionNumber = partitionNumber;
	}

	public long getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(long startOffset) {
		this.startOffset = startOffset;
	}

	public long getEndOffset() {
		return endOffset;
	}

	public void setEndOffset(long endOffset) {
		this.endOffset = endOffset;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (endOffset ^ (endOffset >>> 32));
		result = prime * result + (int) (partitionNumber ^ (partitionNumber >>> 32));
		result = prime * result + (int) (startOffset ^ (startOffset >>> 32));
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
		PartitionOffsetInfo other = (PartitionOffsetInfo) obj;
		if (endOffset != other.endOffset)
			return false;
		if (partitionNumber != other.partitionNumber)
			return false;
		if (startOffset != other.startOffset)
			return false;
		return true;
	}
	
	
	
}
