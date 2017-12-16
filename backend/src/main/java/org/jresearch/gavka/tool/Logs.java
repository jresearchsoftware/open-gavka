package org.jresearch.gavka.tool;

import static java.util.Optional.*;

import java.text.MessageFormat;
import java.util.List;

import javax.annotation.Nonnull;

import org.jresearch.gavka.domain.LogUiLevel;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import one.util.streamex.StreamEx;

public class Logs {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Logs.class);

	private static LoggerContext logContext;

	private Logs() {
		// prevent instantiation
	}

	@Nonnull
	public static List<Appender<ILoggingEvent>> getAppenders() {
		return StreamEx
				.of(getLoggers())
				.flatMap(l -> StreamEx.<Appender<ILoggingEvent>> of(l.iteratorForAppenders()))
				.toList();
	}

	@SuppressWarnings({ "null" })
	@Nonnull
	public static List<Logger> getLoggers() {
		return getContext().getLoggerList();
	}

	@SuppressWarnings("null")
	@Nonnull
	private synchronized static LoggerContext getContext() {
		if (logContext == null) {
			try {
				logContext = (LoggerContext) LoggerFactory.getILoggerFactory();
			} catch (final ClassCastException e) {
				throw new UnsupportedLoggerImplementation(MessageFormat.format("Logger context {0} is not supported", LoggerFactory.getILoggerFactory().getClass())); //$NON-NLS-1$
			}
		}
		return logContext;
	}

	@SuppressWarnings("boxing")
	public static boolean updateLogger(final String name, final LogUiLevel level, final boolean additive) {
		final Logger logger = getContext().getLogger(name);
		if (logger != null) {
			synchronized (logger) {
				logger.setLevel(ofNullable(level).map(l -> Level.toLevel(l.name())).orElse(null));
				logger.setAdditive(additive);
				LOGGER.trace("Update logger {} with level {} and additive {}", name, level, additive); //$NON-NLS-1$
			}
			return true;
		}
		return false;
	}

}
