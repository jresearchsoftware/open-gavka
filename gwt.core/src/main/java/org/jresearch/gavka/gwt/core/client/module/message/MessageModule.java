package org.jresearch.gavka.gwt.core.client.module.message;

import org.jresearch.commons.gwt.client.app.GeneralModule;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MessageModule extends GeneralModule {

	private static final String NAME = "Topic messages"; //$NON-NLS-1$

	@SuppressWarnings("all")
	@Inject
	public MessageModule() {
		super(NAME, MessageController.ID, MessageController.ID);
	}

}
