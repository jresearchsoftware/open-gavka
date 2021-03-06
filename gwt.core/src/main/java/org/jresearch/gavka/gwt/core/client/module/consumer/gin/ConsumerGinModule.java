package org.jresearch.gavka.gwt.core.client.module.consumer.gin;

import org.jresearch.commons.gwt.client.app.IAppModule;
import org.jresearch.commons.gwt.client.gin.InstallableGinModule;
import org.jresearch.gavka.gwt.core.client.module.consumer.ConsumerModule;

import com.google.gwt.inject.client.multibindings.GinMultibinder;

public class ConsumerGinModule extends InstallableGinModule {

	private static final String ID = "org.jresearch.gavka.gwt.core.client.module.consumer.gin.ConsumerGinModule"; //$NON-NLS-1$

	@Override
	protected String getId() {
		return ID;
	}

	@Override
	protected void configure() {

		final GinMultibinder<IAppModule> multibinder = GinMultibinder.newSetBinder(binder(), IAppModule.class);
		multibinder.addBinding().to(ConsumerModule.class).asEagerSingleton();

	}

}
