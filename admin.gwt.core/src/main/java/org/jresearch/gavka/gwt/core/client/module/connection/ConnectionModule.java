package org.jresearch.gavka.gwt.core.client.module.connection;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jresearch.commons.gwt.client.app.GeneralModule;

@Singleton
public class ConnectionModule extends GeneralModule {

	private static final String NAME = "Kafka connections"; //$NON-NLS-1$

	@SuppressWarnings("all")
	@Inject
	public ConnectionModule() {
		super(NAME, ConnectionController.ID, ConnectionController.ID);
	}

}
