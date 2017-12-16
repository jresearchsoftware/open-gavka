package org.jresearch.gavka.gwt.core.client.module.logger.gin;

import org.jresearch.commons.gwt.client.app.IAppModule;
import org.jresearch.commons.gwt.client.gin.InstallableGinModule;
import org.jresearch.gavka.gwt.core.client.module.logger.LoggerController;
import org.jresearch.gavka.gwt.core.client.module.logger.LoggerModule;

import com.google.gwt.inject.client.multibindings.GinMultibinder;

public class LoggerGinModule extends InstallableGinModule {

	private final String ID = "org.jresearch.gavka.gwt.core.client.module.logger.gin.LoggerGinModule"; //$NON-NLS-1$

	@Override
	protected String getId() {
		return ID;
	}

	@Override
	protected void configure() {

		bind(LoggerController.class).asEagerSingleton();

		final GinMultibinder<IAppModule> multibinder = GinMultibinder.newSetBinder(binder(), IAppModule.class);
		multibinder.addBinding().to(LoggerModule.class).asEagerSingleton();
	}

}
