package org.jresearch.gavka.gwt.core.client.module.logger;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.AbstractView;
import org.jresearch.gavka.gwt.core.client.module.logger.widget.LoggerPage;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LoggerView extends AbstractView<LoggerController> {

	@Nonnull
	private final LoggerPage page;

	@Inject
	public LoggerView(@Nonnull final LoggerPage page, @Nonnull final LoggerController controller) {
		super(controller);
		this.page = page;
	}

	@Override
	public Widget getContent() {
		return page;
	}

}
