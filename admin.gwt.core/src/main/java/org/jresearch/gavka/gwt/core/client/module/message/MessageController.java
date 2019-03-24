package org.jresearch.gavka.gwt.core.client.module.message;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jresearch.commons.gwt.client.mvc.AbstractModuleController;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.app.GavkaAppController;

import com.google.gwt.inject.client.AsyncProvider;

@Singleton
public class MessageController extends AbstractModuleController<MessageView> {

	@Nonnull
	public static final String ID = "org.jresearch.gavka.gwt.core.client.module.message.MessageController"; //$NON-NLS-1$

	@Inject
	public MessageController(@Nonnull final Bus bus, @Nonnull final GavkaAppController appController, @Nonnull final AsyncProvider<MessageView> view) {
		super(ID, bus, appController, view);
	}

}
