package org.jresearch.gavka.gwt.core.client.module.appender;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.AbstractView;
import org.jresearch.gavka.gwt.core.client.module.appender.widget.AppenderPage;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AppenderView extends AbstractView<AppenderController> {

	@Nonnull
	private final AppenderPage page;

	@Inject
	public AppenderView(@Nonnull final AppenderPage page, @Nonnull final AppenderController controller) {
		super(controller);
		this.page = page;
	}

	@Override
	public Widget getContent() {
		return page;
	}

}
