package org.jresearch.gavka.gwt.core.client.module.message;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jresearch.commons.gwt.client.app.GeneralModule;

@Singleton
public class MessageModule extends GeneralModule {

	private static final String NAME = "Topic messages"; //$NON-NLS-1$

	@SuppressWarnings("all")
	@Inject
	public MessageModule() {
		super(NAME, MessageController.ID, MessageController.ID);
	}

}
