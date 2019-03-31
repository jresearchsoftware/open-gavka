package org.jresearch.gavka.gwt.core.client.module.consumer;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.INotificator;
import org.jresearch.gavka.gwt.core.client.module.GafkaFactory;
import org.jresearch.gavka.gwt.core.client.module.consumer.widget.ConsumerDataSource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class ConsumerViewFactory implements GafkaFactory<ConsumerView> {

	private final Provider<INotificator> notificator;
	private final Provider<ConsumerDataSource> consumerDataSource;

	@Inject
	public ConsumerViewFactory(@Nonnull final Provider<INotificator> notificator, @Nonnull final Provider<ConsumerDataSource> consumerDataSource) {
		this.notificator = notificator;
		this.consumerDataSource = consumerDataSource;
	}

	@Override
	public ConsumerView create(@Nonnull final String connectionId, @Nonnull final String topic) {
		return new ConsumerView(notificator.get(), consumerDataSource.get(), connectionId, topic);
	}

}
