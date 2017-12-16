package org.jresearch.gavka.gwt.core.client.gin;

import org.jresearch.commons.gwt.client.gin.UtilsGinModule;
import org.jresearch.commons.gwt.client.mvc.INotificator;
import org.jresearch.commons.gwt.client.mvc.LogNotificator;
import org.jresearch.gavka.gwt.core.client.app.GavkaAppController;
import org.jresearch.gavka.gwt.core.client.module.logger.gin.LoggerGinModule;

import com.google.gwt.inject.client.AbstractGinModule;

public class LogbackUiGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		// Common apps
		install(new UtilsGinModule());
		// modules
		install(new LoggerGinModule());

		bind(GavkaAppController.class).asEagerSingleton();
		bind(INotificator.class).to(LogNotificator.class).asEagerSingleton();
	}
}
