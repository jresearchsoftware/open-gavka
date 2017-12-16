package org.jresearch.gavka.domain;

import javax.annotation.Nonnull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class LogUiAppender {

	@Nonnull
	private String name = ""; //$NON-NLS-1$
	@Nonnull
	private String className = ""; //$NON-NLS-1$

	/**
	 * @return the name
	 */
	@Nonnull
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(@Nonnull final String name) {
		this.name = name;
	}

	/**
	 * @return the className
	 */
	@Nonnull
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(@Nonnull final String className) {
		this.className = className;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.omitNullValues()
				.add("name", name)
				.add("className", className)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getName());
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof LogUiAppender) {
			final LogUiAppender that = (LogUiAppender) object;
			return Objects.equal(this.getName(), that.getName());
		}
		return false;
	}

}
