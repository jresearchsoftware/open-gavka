package org.jresearch.gavka.gwt.core.client.module.logger.widget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.fusesource.restygwt.client.Method;
import org.jresearch.commons.gwt.client.mvc.AbstractMethodCallback;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.module.logger.srv.LogUiLoggerService;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.inject.Inject;

public class LoggerSuggestOracle extends SuggestOracle {

	@Nonnull
	private final LogUiLoggerService srv;
	@Nonnull
	private final Bus bus;

	/**
	 * Suggestion class for {@link LoggerSuggestOracle}.
	 */
	public static class LoggerWordSuggestion implements Suggestion, Serializable {

		private static final long serialVersionUID = -8659786586689527670L;

		private String loggerName;

		/**
		 * REST Constructor.
		 */
		public LoggerWordSuggestion() {
			// nothing
		}

		/**
		 * Constructor for <code>LoggerWordSuggestion</code>.
		 */
		public LoggerWordSuggestion(final String loggerName) {
			this.loggerName = loggerName;
		}

		@Override
		public String getDisplayString() {
			return loggerName;
		}

		@Override
		public String getReplacementString() {
			return loggerName;
		}
	}

	@Inject
	public LoggerSuggestOracle(@Nonnull final LogUiLoggerService srv, @Nonnull final Bus bus) {
		this.srv = srv;
		this.bus = bus;
	}

	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		srv.requestSuggestion(request.getQuery(), request.getLimit(), new AbstractMethodCallback<List<String>>(bus) {
			@Override
			public void onSuccess(final Method method, final List<String> response) {
				final List<LoggerWordSuggestion> result = new ArrayList<>();
				for (final String string : response) {
					result.add(new LoggerWordSuggestion(string));
				}
				callback.onSuggestionsReady(request, new Response(result));
			}
		});
	}

}
