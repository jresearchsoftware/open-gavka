package org.jresearch.gavka.gwt.core.client.module;

import org.jresearch.commons.gwt.client.app.GeneralModule;

public class GafkaModule<T> extends GeneralModule {

	private final GafkaControllerFactory<T> controllerFactory;

	public GafkaModule(final String name, final String moduleId, final GafkaControllerFactory<T> controllerFactory) {
		super(name, moduleId, moduleId);
		this.controllerFactory = controllerFactory;
	}

	public GafkaControllerFactory<T> getControllerFactory() {
		return controllerFactory;
	}

}
