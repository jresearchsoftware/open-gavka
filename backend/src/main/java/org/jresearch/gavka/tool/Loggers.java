package org.jresearch.gavka.tool;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jresearch.gavka.domain.Message;

import com.google.common.base.Joiner;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.util.LoggerNameUtil;
import one.util.streamex.StreamEx;

public class Loggers {

	static {
		// Warm the logger
		ForkJoinPool.commonPool().execute(Logs::getLoggers);
	}

	private static final String QUERY_FORMAT = ".*{0}.*"; //$NON-NLS-1$
	public static final Pattern CAMEL_CASE_P01 = Pattern.compile("\\*"); //$NON-NLS-1$
	public static final String CAMEL_CASE_R01 = ".*"; //$NON-NLS-1$
	public static final Pattern CAMEL_CASE_P02 = Pattern.compile("(\\p{Upper}\\p{Lower}*)", Pattern.UNICODE_CHARACTER_CLASS); //$NON-NLS-1$
	public static final String CAMEL_CASE_R02 = "$1\\\\p{Lower}*"; //$NON-NLS-1$
	public static final Pattern CAMEL_CASE_P03 = Pattern.compile("(\\p{Lower})\\.", Pattern.UNICODE_CHARACTER_CLASS); //$NON-NLS-1$
	public static final String CAMEL_CASE_R03 = "$1\\\\p{Lower}*\\\\."; //$NON-NLS-1$

	private Loggers() {
		// prevent from instantiation
	}

	public static List<Message> getLoggers(final String filter, final boolean inherited) {
		return filter(filter, inherited, Long.MAX_VALUE, Loggers::toUi);
	}

	private static <R> List<R> filter(final String query, final boolean inherited, final long limit, final Function<Logger, R> logMapper) {
		final String patternString = MessageFormat.format(QUERY_FORMAT, CAMEL_CASE_P03.matcher(CAMEL_CASE_P02.matcher(CAMEL_CASE_P01.matcher(query).replaceAll(CAMEL_CASE_R01)).replaceAll(CAMEL_CASE_R02)).replaceAll(CAMEL_CASE_R03));
		final Pattern queryPattern = Pattern.compile(patternString, Pattern.UNICODE_CHARACTER_CLASS);
		return StreamEx.of(Logs.getLoggers()).parallel().filter(l -> inherited || l.getLevel() != null).filter(l -> queryPattern.matcher(l.getName()).matches()).limit(limit).map(logMapper).toList();
	}

	@Nonnull
	private static Message toUi(@Nonnull final Logger logger) {
		return new Message(logger.getName(), toUi(logger.getLevel()), logger.getEffectiveLevel().levelInt);
	}

	@Nullable
	public static Logger getParent(@Nonnull final Logger logger) {
		if (isRoot(logger)) {
			return null;
		}
		final List<String> nameParts = LoggerNameUtil.computeNameParts(logger.getName());
		final String parentString = nameParts.size() == 1 ? Logger.ROOT_LOGGER_NAME : Joiner.on('.').join(nameParts.subList(0, nameParts.size() - 1));
		return logger.getLoggerContext().getLogger(parentString);
	}

	public static boolean isRoot(@Nonnull final Logger logger) {
		final String name = logger.getName();
		return Logger.ROOT_LOGGER_NAME.equals(name);
	}

	private static String toUi(final Level level) {
		return level == null ? "" : level.toString();
	}

	public static boolean updateLogger(final Message logger) {
		return true;
	}

}
