package org.jresearch.gavka.gwt.core.client.module.message;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jresearch.gavka.gwt.core.client.module.GafkaModule;

@Singleton
public class MessageModule extends GafkaModule<MessageController> {

	private static final String NAME = "Messages"; //$NON-NLS-1$

	@SuppressWarnings("all")
	@Inject
	public MessageModule(final MessageControllerFactory factory) {
		super(NAME, MessageController.ID, factory);
	}

}
