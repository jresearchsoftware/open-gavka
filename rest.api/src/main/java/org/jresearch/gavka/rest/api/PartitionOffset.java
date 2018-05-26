package org.jresearch.gavka.rest.api;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class PartitionOffset {

	private int partition;
	private long offset;

	public int getPartition() {
		return partition;
	}

	public void setPartition(final int partition) {
		this.partition = partition;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(final long offset) {
		this.offset = offset;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("partition", partition)
				.add("offset", offset)
				.toString();
	}

	@SuppressWarnings("boxing")
	@Override
	public int hashCode() {
		return Objects.hashCode(getPartition(), getOffset());
	}

	@SuppressWarnings("boxing")
	@Override
	public boolean equals(final Object object) {
		if (object instanceof PartitionOffset) {
			final PartitionOffset that = (PartitionOffset) object;
			return Objects.equal(this.getPartition(), that.getPartition())
					&& Objects.equal(this.getOffset(), that.getOffset());
		}
		return false;
	}

}
