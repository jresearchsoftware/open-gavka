package org.jresearch.gavka.gwt.core.client.module.overview.widget;

import javax.annotation.Nonnull;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class OverviewPage extends Composite {

	// @formatter:off
	interface Binder extends UiBinder<Label, OverviewPage> {/* nothing */}
	// @formatter:on

	@Inject
	protected OverviewPage(@Nonnull final Binder binder) {
		initWidget(binder.createAndBindUi(this));
		setStyleName("OverviewPage");
	}

}
