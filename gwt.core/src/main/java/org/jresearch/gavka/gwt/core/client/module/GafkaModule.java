package org.jresearch.gavka.gwt.core.client.module;

import org.jresearch.commons.gwt.client.app.GeneralModule;

public class GafkaModule<T> extends GeneralModule {

	private final GafkaFactory<T> controllerFactory;

	public GafkaModule(final String name, final String moduleId, final GafkaFactory<T> controllerFactory) {
		super(name, moduleId, moduleId);
		this.controllerFactory = controllerFactory;
	}

	public GafkaFactory<T> getControllerFactory() {
		return controllerFactory;
	}

}
