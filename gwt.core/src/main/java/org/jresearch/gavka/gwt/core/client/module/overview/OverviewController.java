package org.jresearch.gavka.gwt.core.client.module.overview;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.AbstractModuleController;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.app.LogbackUiController;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class OverviewController extends AbstractModuleController<OverviewView> {

	@Nonnull
	public static final String ID = "org.jresearch.gavka.gwt.core.client.module.overview.OverviewController"; //$NON-NLS-1$

	@Inject
	public OverviewController(@Nonnull final Bus bus, @Nonnull final LogbackUiController appController, @Nonnull final AsyncProvider<OverviewView> view) {
		super(ID, bus, appController, view);
	}

}
