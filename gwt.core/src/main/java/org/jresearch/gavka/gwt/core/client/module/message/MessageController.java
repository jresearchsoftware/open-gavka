package org.jresearch.gavka.gwt.core.client.module.message;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.AbstractModuleController;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.app.GavkaAppController;
import org.jresearch.gavka.gwt.core.client.module.GafkaModule;
import org.jresearch.gavka.gwt.core.client.module.GafkaViewProvider;
import org.jresearch.gavka.rest.data.GafkaCoordinates;

public class MessageController extends AbstractModuleController<MessageView> {

	@Nonnull
	public static final String ID = "org.jresearch.gavka.gwt.core.client.module.message.MessageController"; //$NON-NLS-1$

	public MessageController(@Nonnull final Bus bus, @Nonnull final GavkaAppController appController, @Nonnull final MessageViewFactory view, @Nonnull final GafkaCoordinates gafkaCoordinates) {
		super(GafkaModule.id(ID, gafkaCoordinates), bus, appController, new GafkaViewProvider<>(view, gafkaCoordinates));
		GafkaModule.addCoordinates(getId(), gafkaCoordinates);
	}

}
