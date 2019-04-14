package org.jresearch.gavka.gwt.core.client.module.consumer;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.app.GavkaAppController;
import org.jresearch.gavka.gwt.core.client.module.GafkaFactory;
import org.jresearch.gavka.rest.data.GafkaCoordinates;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class ConsumerControllerFactory implements GafkaFactory<ConsumerController> {

	@Nonnull
	private final Provider<Bus> bus;
	@Nonnull
	private final Provider<GavkaAppController> appController;
	@Nonnull
	private final Provider<ConsumerViewFactory> view;

	@Inject
	public ConsumerControllerFactory(@Nonnull final Provider<Bus> bus, @Nonnull final Provider<GavkaAppController> appController, @Nonnull final Provider<ConsumerViewFactory> view) {
		this.bus = bus;
		this.appController = appController;
		this.view = view;
	}

	@SuppressWarnings("null")
	@Override
	public ConsumerController create(@Nonnull final GafkaCoordinates gafkaCoordinates) {
		return new ConsumerController(bus.get(), appController.get(), view.get(), gafkaCoordinates);
	}

}
