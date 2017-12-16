package org.jresearch.gavka.gwt.core.client.module.message.gin;

import org.jresearch.commons.gwt.client.app.IAppModule;
import org.jresearch.commons.gwt.client.gin.InstallableGinModule;
import org.jresearch.gavka.gwt.core.client.module.message.MessageController;
import org.jresearch.gavka.gwt.core.client.module.message.MessageModule;

import com.google.gwt.inject.client.multibindings.GinMultibinder;

public class MessagaGinModule extends InstallableGinModule {

	private final String ID = "org.jresearch.gavka.gwt.core.client.module.message.gin.MessagaGinModule"; //$NON-NLS-1$

	@Override
	protected String getId() {
		return ID;
	}

	@Override
	protected void configure() {

		bind(MessageController.class).asEagerSingleton();

		final GinMultibinder<IAppModule> multibinder = GinMultibinder.newSetBinder(binder(), IAppModule.class);
		multibinder.addBinding().to(MessageModule.class).asEagerSingleton();
	}

}
