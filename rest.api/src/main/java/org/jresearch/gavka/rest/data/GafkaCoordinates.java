package org.jresearch.gavka.rest.data;

import javax.annotation.Nonnull;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@Value.Style(builder = "new") // builder has to have constructor
@JsonDeserialize(builder = ImmutableGafkaCoordinates.Builder.class)
public interface GafkaCoordinates {

	@Nonnull
	String connectionId();

	@Nonnull
	String topic();

}