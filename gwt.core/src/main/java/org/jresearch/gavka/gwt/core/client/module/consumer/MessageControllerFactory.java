package org.jresearch.gavka.gwt.core.client.module.consumer;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.app.GavkaAppController;
import org.jresearch.gavka.gwt.core.client.module.GafkaFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class MessageControllerFactory implements GafkaFactory<MessageController> {

	@Nonnull
	private final Provider<Bus> bus;
	@Nonnull
	private final Provider<GavkaAppController> appController;
	@Nonnull
	private final Provider<MessageViewFactory> view;

	@Inject
	public MessageControllerFactory(@Nonnull final Provider<Bus> bus, @Nonnull final Provider<GavkaAppController> appController, @Nonnull final Provider<MessageViewFactory> view) {
		this.bus = bus;
		this.appController = appController;
		this.view = view;
	}

	@Override
	public MessageController create(@Nonnull final String connectionId, @Nonnull final String topic) {
		return new MessageController(bus.get(), appController.get(), view.get(), connectionId, topic);
	}

}
