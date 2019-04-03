package org.jresearch.gavka.gwt.core.client.module.consumer;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.AbstractModuleController;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.app.GavkaAppController;
import org.jresearch.gavka.gwt.core.client.module.GafkaModule;
import org.jresearch.gavka.gwt.core.client.module.GafkaViewProvider;

public class ConsumerController extends AbstractModuleController<ConsumerView> {

	@Nonnull
	public static final String ID = "org.jresearch.gavka.gwt.core.client.module.consumer.ConsumerController"; //$NON-NLS-1$

	public ConsumerController(@Nonnull final Bus bus, @Nonnull final GavkaAppController appController, @Nonnull final ConsumerViewFactory view, @Nonnull final String connectionId, @Nonnull final String topic) {
		super(GafkaModule.id(ID, connectionId, topic), bus, appController, new GafkaViewProvider(view, connectionId, topic));
	}

}
