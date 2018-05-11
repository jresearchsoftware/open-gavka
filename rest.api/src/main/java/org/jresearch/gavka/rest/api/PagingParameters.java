package org.jresearch.gavka.rest.api;

import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class PagingParameters {

	/** The number of item to return. */
	private int amount;
	/** list of offsets for topic partitions */
	private List<PartitionOffset> partitionOffsets;

	public PagingParameters() {
	}

	public PagingParameters(final int amount, final List<PartitionOffset> partitionOffsets) {
		this.amount = amount;
		this.partitionOffsets = partitionOffsets;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(final int amount) {
		this.amount = amount;
	}

	public List<PartitionOffset> getPartitionOffsets() {
		return partitionOffsets;
	}

	public void setPartitionOffsets(final List<PartitionOffset> partitionOffsets) {
		this.partitionOffsets = partitionOffsets;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("amount", amount)
				.add("partitionOffsets", partitionOffsets)
				.toString();
	}

	@SuppressWarnings("boxing")
	@Override
	public int hashCode() {
		return Objects.hashCode(getAmount(), getPartitionOffsets());
	}

	@SuppressWarnings("boxing")
	@Override
	public boolean equals(final Object object) {
		if (object instanceof PagingParameters) {
			final PagingParameters that = (PagingParameters) object;
			return Objects.equal(this.getAmount(), that.getAmount())
					&& Objects.equal(this.getPartitionOffsets(), that.getPartitionOffsets());
		}
		return false;
	}

}
