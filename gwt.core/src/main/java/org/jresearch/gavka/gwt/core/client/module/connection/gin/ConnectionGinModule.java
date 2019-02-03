package org.jresearch.gavka.gwt.core.client.module.connection.gin;

import org.jresearch.commons.gwt.client.app.IAppModule;
import org.jresearch.commons.gwt.client.gin.InstallableGinModule;
import org.jresearch.gavka.gwt.core.client.module.connection.ConnectionController;
import org.jresearch.gavka.gwt.core.client.module.connection.ConnectionModule;

import com.google.gwt.inject.client.multibindings.GinMultibinder;

public class ConnectionGinModule extends InstallableGinModule {

	private final String ID = "org.jresearch.gavka.gwt.core.client.module.connection.gin.ConnectionGinModule"; //$NON-NLS-1$

	@Override
	protected String getId() {
		return ID;
	}

	@Override
	protected void configure() {

		bind(ConnectionController.class).asEagerSingleton();

		final GinMultibinder<IAppModule> multibinder = GinMultibinder.newSetBinder(binder(), IAppModule.class);
		multibinder.addBinding().to(ConnectionModule.class).asEagerSingleton();
	}

}
