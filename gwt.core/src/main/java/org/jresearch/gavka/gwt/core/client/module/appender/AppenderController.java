package org.jresearch.gavka.gwt.core.client.module.appender;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.AbstractModuleController;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.app.LogbackUiController;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AppenderController extends AbstractModuleController<AppenderView> {

	@Nonnull
	public static final String ID = "org.jresearch.gavka.gwt.core.client.module.appender.AppenderController"; //$NON-NLS-1$

	@Inject
	public AppenderController(@Nonnull final Bus bus, @Nonnull final LogbackUiController appController, @Nonnull final AsyncProvider<AppenderView> view) {
		super(ID, bus, appController, view);
	}

}
