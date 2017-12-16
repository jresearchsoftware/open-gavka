package org.jresearch.gavka.domain;

import javax.annotation.Nonnull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class LogUiLogger {

	@Nonnull
	private String name = ""; //$NON-NLS-1$
	@Nonnull
	private LogUiLevel level = LogUiLevel.OFF;
	@Nonnull
	private LogUiLevel effectiveLevel = LogUiLevel.OFF;
	private boolean additive;

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
	 * @return the level
	 */
	@Nonnull
	public LogUiLevel getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(@Nonnull final LogUiLevel level) {
		this.level = level;
	}

	/**
	 * @return the effectiveLevel
	 */
	@Nonnull
	public LogUiLevel getEffectiveLevel() {
		return effectiveLevel;
	}

	/**
	 * @param effectiveLevel
	 *            the effectiveLevel to set
	 */
	public void setEffectiveLevel(@Nonnull final LogUiLevel effectiveLevel) {
		this.effectiveLevel = effectiveLevel;
	}

	/**
	 * @return the additive
	 */
	public boolean isAdditive() {
		return additive;
	}

	/**
	 * @param additive
	 *            the additive to set
	 */
	public void setAdditive(final boolean additive) {
		this.additive = additive;
	}

	/** Only name */
	@Override
	public int hashCode() {
		return Objects.hashCode(getName());
	}

	/** Only name */
	@Override
	public boolean equals(final Object object) {
		if (object instanceof LogUiLogger) {
			final LogUiLogger that = (LogUiLogger) object;
			return Objects.equal(this.getName(), that.getName());
		}
		return false;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.omitNullValues()
				.add("name", name)
				.add("level", level)
				.add("effectiveLevel", effectiveLevel)
				.add("additive", additive)
				.toString();
	}

}
