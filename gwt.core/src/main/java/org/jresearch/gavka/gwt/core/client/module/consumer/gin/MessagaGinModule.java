package org.jresearch.gavka.gwt.core.client.module.consumer.gin;

import org.jresearch.commons.gwt.client.app.IAppModule;
import org.jresearch.commons.gwt.client.gin.InstallableGinModule;
import org.jresearch.gavka.gwt.core.client.module.consumer.MessageModule;
import org.jresearch.gavka.gwt.core.client.module.consumer.MessageViewFactory;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.inject.client.multibindings.GinMultibinder;

public class MessagaGinModule extends InstallableGinModule {

	private static final String ID = "org.jresearch.gavka.gwt.core.client.module.consumer.gin.MessagaGinModule"; //$NON-NLS-1$

	@Override
	protected String getId() {
		return ID;
	}

	@Override
	protected void configure() {

		// Factories
		install(new GinFactoryModuleBuilder().build(MessageViewFactory.class));

		final GinMultibinder<IAppModule> multibinder = GinMultibinder.newSetBinder(binder(), IAppModule.class);
		multibinder.addBinding().to(MessageModule.class).asEagerSingleton();

	}

}
