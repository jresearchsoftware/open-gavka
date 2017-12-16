package org.jresearch.gavka.tool;

import java.text.MessageFormat;
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.LoggerFactory;

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

}
