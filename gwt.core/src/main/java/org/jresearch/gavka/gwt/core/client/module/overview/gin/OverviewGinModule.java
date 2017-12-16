package org.jresearch.gavka.gwt.core.client.module.overview.gin;

import org.jresearch.commons.gwt.client.app.IAppModule;
import org.jresearch.commons.gwt.client.gin.InstallableGinModule;
import org.jresearch.gavka.gwt.core.client.module.overview.OverviewController;
import org.jresearch.gavka.gwt.core.client.module.overview.OverviewModule;

import com.google.gwt.inject.client.multibindings.GinMultibinder;

public class OverviewGinModule extends InstallableGinModule {

	private final String ID = "org.jresearch.gavka.gwt.core.client.module.overview.gin.OverviewGinModule"; //$NON-NLS-1$

	@Override
	protected String getId() {
		return ID;
	}

	@Override
	protected void configure() {

		bind(OverviewController.class).asEagerSingleton();

		final GinMultibinder<IAppModule> multibinder = GinMultibinder.newSetBinder(binder(), IAppModule.class);
		multibinder.addBinding().to(OverviewModule.class).asEagerSingleton();
	}

}
