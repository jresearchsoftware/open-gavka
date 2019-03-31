package org.jresearch.gavka.gwt.core.client.module;

import javax.annotation.Nonnull;

public interface GafkaFactory<T> {

	T create(@Nonnull final String connectionId, @Nonnull final String topic);

}
