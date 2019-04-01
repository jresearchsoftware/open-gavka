package org.jresearch.gavka.gwt.core.client.module.consumer;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.INotificator;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.module.GafkaFactory;
import org.jresearch.gavka.gwt.core.client.module.consumer.srv.GavkaConsumerRestService;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class ConsumerViewFactory implements GafkaFactory<ConsumerView> {

	private final Provider<INotificator> notificator;
	private final GavkaConsumerRestService srv;
	private final Provider<Bus> bus;

	@Inject
	public ConsumerViewFactory(@Nonnull final Provider<INotificator> notificator, @Nonnull final GavkaConsumerRestService srv, final Provider<Bus> bus) {
		this.notificator = notificator;
		this.srv = srv;
		this.bus = bus;
	}

	@Override
	public ConsumerView create(@Nonnull final String connectionId, @Nonnull final String topic) {
		return new ConsumerView(notificator.get(), srv, bus.get(), connectionId, topic);
	}

}
