package org.jresearch.gavka.domain;

import java.util.List;

import com.google.common.collect.Lists;

public enum LogUiLevel {

	ALL, TRACE, DEBUG, INFO, WARN, ERROR, OFF;

	public static final String INHERITED = "INHERITED"; //$NON-NLS-1$

	/**
	 * @return all possible levels along with INHERITED
	 */
	public static List<String> stringValues() {
		final List<String> result = Lists.newArrayList(INHERITED);
		for (final LogUiLevel level : values()) {
			result.add(level.name());
		}
		return result;
	}

	/**
	 * @return the {@link #name()} of level or INHERITED in case of
	 *         <code>null</code>
	 */
	public static String stringValue(final LogUiLevel level) {
		return level == null ? INHERITED : level.name();
	}

	/**
	 * Level or <code>null</code> for INHERITED or incorrect level
	 *
	 * @param value
	 *            - proposed level
	 * @return resolved level or <code>null</code>
	 */
	public static LogUiLevel value(final String value) {
		try {
			return valueOf(value);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}

}
