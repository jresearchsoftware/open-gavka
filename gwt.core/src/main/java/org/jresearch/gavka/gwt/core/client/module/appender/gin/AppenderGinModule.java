package org.jresearch.gavka.gwt.core.client.module.appender.gin;

import org.jresearch.commons.gwt.client.app.IAppModule;
import org.jresearch.commons.gwt.client.gin.InstallableGinModule;
import org.jresearch.gavka.gwt.core.client.module.appender.AppenderController;
import org.jresearch.gavka.gwt.core.client.module.appender.AppenderModule;

import com.google.gwt.inject.client.multibindings.GinMultibinder;

public class AppenderGinModule extends InstallableGinModule {

	private final String ID = "org.jresearch.gavka.gwt.core.client.module.appender.gin.AppenderGinModule"; //$NON-NLS-1$

	@Override
	protected String getId() {
		return ID;
	}

	@Override
	protected void configure() {

		bind(AppenderController.class).asEagerSingleton();

		final GinMultibinder<IAppModule> multibinder = GinMultibinder.newSetBinder(binder(), IAppModule.class);
		multibinder.addBinding().to(AppenderModule.class).asEagerSingleton();
	}

}
