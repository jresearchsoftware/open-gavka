package org.jresearch.gavka.gwt.core.client.module.consumer;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.INotificator;
import org.jresearch.gavka.gwt.core.client.module.GafkaFactory;
import org.jresearch.gavka.rest.data.GafkaCoordinates;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class ConsumerViewFactory implements GafkaFactory<ConsumerView> {

	private final Provider<INotificator> notificator;

	@Inject
	public ConsumerViewFactory(@Nonnull final Provider<INotificator> notificator) {
		this.notificator = notificator;
	}

	@SuppressWarnings("null")
	@Override
	public ConsumerView create(@Nonnull final GafkaCoordinates coordinates) {
		return new ConsumerView(notificator.get(), coordinates);
	}

}
