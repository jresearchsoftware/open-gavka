package org.jresearch.gavka.rest.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * field descriptor, describe the sorting and ordering for {@link PageLoadInput}
 */
public class FieldDescriptor {

	/** Case-insensitive name of field */
	@Nonnull
	private final String name = "";

	/**
	 * field filter if any, <code>null</code> or empty string mans all values
	 */
	@Nullable
	private String filter;

	/**
	 * sort order, to exclude the field from sorting use sortDirection
	 * {@link Sort#NONE}
	 */
	private int sortOrder;

	/** sort direction, {@link Sort#NONE} to ignore */
	@Nonnull
	private final Sort sortDirection = Sort.NONE;

	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(final String filter) {
		this.filter = filter;
	}

	/**
	 * @return the sortOrder
	 */
	public int getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder
	 *            the sortOrder to set
	 */
	public void setSortOrder(final int sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the sortDirection
	 */
	public Sort getSortDirection() {
		return sortDirection;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("name", name)
				.add("filter", filter)
				.add("sortOrder", sortOrder)
				.add("sortDirection", sortDirection)
				.toString();
	}

	@SuppressWarnings("boxing")
	@Override
	public int hashCode() {
		return Objects.hashCode(getName(), getFilter(), getSortOrder(), getSortDirection());
	}

	@SuppressWarnings({ "null", "boxing" })
	@Override
	public boolean equals(final Object object) {
		if (object instanceof FieldDescriptor) {
			final FieldDescriptor that = (FieldDescriptor) object;
			return Objects.equal(this.getName(), that.getName())
					&& Objects.equal(this.getFilter(), that.getFilter())
					&& Objects.equal(this.getSortOrder(), that.getSortOrder())
					&& Objects.equal(this.getSortDirection(), that.getSortDirection());
		}
		return false;
	}

}
