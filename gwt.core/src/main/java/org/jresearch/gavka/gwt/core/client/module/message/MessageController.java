package org.jresearch.gavka.gwt.core.client.module.message;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.AbstractModuleController;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.app.GavkaAppController;

public class MessageController extends AbstractModuleController<MessageView> {

	@Nonnull
	private static final String STRING = "."; //$NON-NLS-1$
	@Nonnull
	public static final String ID = "org.jresearch.gavka.gwt.core.client.module.message.MessageController"; //$NON-NLS-1$

	public MessageController(@Nonnull final Bus bus, @Nonnull final GavkaAppController appController, @Nonnull final MessageViewFactory view, @Nonnull final String connectionId, @Nonnull final String topic) {
		super(id(ID, connectionId, topic), bus, appController, new GafkaViewProvider(view, connectionId, topic));
	}

	@SuppressWarnings("null")
	@Nonnull
	public static String id(final String controllerId, final String connectionId, final String topic) {
		return String.join(STRING, controllerId, connectionId, topic);
	}

}
