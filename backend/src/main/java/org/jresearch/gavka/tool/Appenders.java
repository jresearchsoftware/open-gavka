package org.jresearch.gavka.tool;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import javax.annotation.Nonnull;

import org.jresearch.gavka.domain.LogUiAppender;
import org.reflections.Reflections;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableSet;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import one.util.streamex.StreamEx;

public class Appenders {

	private static List<String> types = ImmutableList.of();

	static {
		ForkJoinPool.commonPool().execute(Appenders::findAppendersTypes);
	}

	private Appenders() {
		// prevent from instantiation
	}

	private static void findAppendersTypes() {
		final Reflections reflections = new Reflections();
		types = StreamEx
				.of(reflections.getSubTypesOf(Appender.class))
				.filter(c -> !(c.isInterface() || Modifier.isAbstract(c.getModifiers())))
				.map(Class::getName)
				.toList();
	}

	@Nonnull
	public static List<LogUiAppender> getAppenders() {
		final com.google.common.collect.ImmutableSet.Builder<LogUiAppender> builder = ImmutableSet.builder();
		final List<Logger> loggers = Logs.getLoggers();
		for (final Logger logger : loggers) {
			final Iterator<Appender<ILoggingEvent>> iteratorForAppenders = logger.iteratorForAppenders();
			while (iteratorForAppenders.hasNext()) {
				builder.add(toUi(iteratorForAppenders.next()));
			}
		}
		return builder.build().asList();
	}

	@Nonnull
	public static List<LogUiAppender> getAppenders(@Nonnull final Logger logger) {
		final Builder<LogUiAppender> builder = ImmutableList.builder();
		final Iterator<Appender<ILoggingEvent>> iteratorForAppenders = logger.iteratorForAppenders();
		while (iteratorForAppenders.hasNext()) {
			final Appender<ch.qos.logback.classic.spi.ILoggingEvent> appender = iteratorForAppenders.next();
			builder.add(toUi(appender));
		}
		if (logger.isAdditive()) {
			final Logger parent = Loggers.getParent(logger);
			if (parent != null) {
				builder.addAll(getAppenders(parent));
			}
		}
		return builder.build();
	}

	@Nonnull
	public static LogUiAppender toUi(@Nonnull final Appender<?> appender) {
		final LogUiAppender result = new LogUiAppender();
		result.setName(appender.getName());
		result.setClassName(appender.getClass().getName());
		return result;
	}

	public static List<String> getAppenderTypes() {
		return types;
	}

}
