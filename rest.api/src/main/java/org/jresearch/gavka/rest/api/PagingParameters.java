package org.jresearch.gavka.rest.api;

import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class PagingParameters {

	/** The number of item to return. */
	private int amount;
	/** start item to return from 0 */
	private int offset;
	/** Optional list of field descriptors, to filter and sort */
	private List<FieldDescriptor> fieldDescriptors;

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(final int amount) {
		this.amount = amount;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(final int offset) {
		this.offset = offset;
	}

	/**
	 * @return the fieldDescriptors
	 */
	public List<FieldDescriptor> getFieldDescriptors() {
		return fieldDescriptors;
	}

	/**
	 * @param fieldDescriptors
	 *            the fieldDescriptors to set
	 */
	public void setFieldDescriptors(final List<FieldDescriptor> fieldDescriptors) {
		this.fieldDescriptors = fieldDescriptors;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("amount", amount)
				.add("offset", offset)
				.add("fieldDescriptors", fieldDescriptors)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getAmount(), getOffset(), getFieldDescriptors());
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof PagingParameters) {
			final PagingParameters that = (PagingParameters) object;
			return Objects.equal(this.getAmount(), that.getAmount())
					&& Objects.equal(this.getOffset(), that.getOffset())
					&& Objects.equal(this.getFieldDescriptors(), that.getFieldDescriptors());
		}
		return false;
	}

}
