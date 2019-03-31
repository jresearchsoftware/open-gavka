package org.jresearch.gavka.gwt.core.client.module.consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jresearch.gavka.gwt.core.client.module.GafkaModule;

@Singleton
public class ConsumerModule extends GafkaModule<ConsumerController> {

	private static final String NAME = "Consumers"; //$NON-NLS-1$

	@SuppressWarnings("all")
	@Inject
	public ConsumerModule(final ConsumerControllerFactory factory) {
		super(NAME, ConsumerController.ID, factory);
	}

}
