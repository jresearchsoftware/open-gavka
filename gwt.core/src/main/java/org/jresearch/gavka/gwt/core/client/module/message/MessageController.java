package org.jresearch.gavka.gwt.core.client.module.message;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.jresearch.commons.gwt.client.mvc.AbstractModuleController;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.app.GavkaAppController;

import com.google.inject.assistedinject.Assisted;

public class MessageController extends AbstractModuleController<MessageView> {

	@Nonnull
	private static final String STRING = "."; //$NON-NLS-1$
	@Nonnull
	public static final String ID = "org.jresearch.gavka.gwt.core.client.module.message.MessageController"; //$NON-NLS-1$
	@Nonnull
	private final String connectionId;
	@Nonnull
	private final String topic;

	@Inject
	public MessageController(@Nonnull final Bus bus, @Nonnull final GavkaAppController appController, @Nonnull final MessageViewFactory view, @Assisted("connectionId") @Nonnull String connectionId, @Assisted("topic") @Nonnull String topic) {
		super(id(ID, connectionId, topic), bus, appController, new GafkaViewProvider(view, connectionId, topic));
		this.connectionId = connectionId;
		this.topic = topic;
	}

	@SuppressWarnings("null")
	@Nonnull
	public static String id(String controllerId, String connectionId, String topic) {
		return String.join(STRING, controllerId, connectionId, topic);
	}

}
