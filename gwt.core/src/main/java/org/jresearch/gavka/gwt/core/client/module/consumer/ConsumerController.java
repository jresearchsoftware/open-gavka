package org.jresearch.gavka.gwt.core.client.module.consumer;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.AbstractModuleController;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.app.GavkaAppController;
import org.jresearch.gavka.gwt.core.client.module.GafkaModule;
import org.jresearch.gavka.gwt.core.client.module.GafkaViewProvider;
import org.jresearch.gavka.rest.data.GafkaCoordinates;

public class ConsumerController extends AbstractModuleController<ConsumerView> {

	@Nonnull
	public static final String ID = "org.jresearch.gavka.gwt.core.client.module.consumer.ConsumerController"; //$NON-NLS-1$

	public ConsumerController(@Nonnull final Bus bus, @Nonnull final GavkaAppController appController, @Nonnull final ConsumerViewFactory view, @Nonnull final GafkaCoordinates coordinates) {
		super(GafkaModule.id(ID, coordinates), bus, appController, new GafkaViewProvider<>(view, coordinates));
		GafkaModule.addCoordinates(getId(), coordinates);
	}

}
