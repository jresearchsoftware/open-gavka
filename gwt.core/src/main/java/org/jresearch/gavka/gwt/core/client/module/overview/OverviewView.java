package org.jresearch.gavka.gwt.core.client.module.overview;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.AbstractView;
import org.jresearch.gavka.gwt.core.client.module.overview.widget.OverviewPage;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class OverviewView extends AbstractView<OverviewController> {

	@Nonnull
	private final OverviewPage page;

	@Inject
	public OverviewView(@Nonnull final OverviewPage page, @Nonnull final OverviewController controller) {
		super(controller);
		this.page = page;
	}

	@Override
	public Widget getContent() {
		return page;
	}

}
