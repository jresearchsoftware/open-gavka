package org.jresearch.gavka.gwt.core.client.module;

import javax.annotation.Nonnull;

import org.jresearch.gavka.rest.data.GafkaCoordinates;

public interface GafkaFactory<T> {

	T create(@Nonnull final GafkaCoordinates gafkaCoordinates);

}
