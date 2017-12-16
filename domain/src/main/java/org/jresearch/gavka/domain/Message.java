package org.jresearch.gavka.domain;

import javax.annotation.Nonnull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Message {

	@Nonnull
	private String key;
	@Nonnull
	private String value;
	private long offset;

	public Message() {
		// GWT
		this.key = "";
		this.value = "";
	}

	public Message(final @Nonnull String key, final @Nonnull String value, final long offset) {
		this.key = key;
		this.value = value;
		this.offset = offset;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the offset
	 */
	public long getOffset() {
		return offset;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(@Nonnull final String key) {
		this.key = key;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(@Nonnull final String value) {
		this.value = value;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(final long offset) {
		this.offset = offset;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("key", key)
				.add("value", value)
				.add("offset", offset)
				.toString();
	}

	@SuppressWarnings("boxing")
	@Override
	public int hashCode() {
		return Objects.hashCode(getKey(), getValue(), getOffset());
	}

	@SuppressWarnings("boxing")
	@Override
	public boolean equals(final Object object) {
		if (object instanceof Message) {
			final Message that = (Message) object;
			return Objects.equal(this.getKey(), that.getKey())
					&& Objects.equal(this.getValue(), that.getValue())
					&& Objects.equal(this.getOffset(), that.getOffset());
		}
		return false;
	}

}
