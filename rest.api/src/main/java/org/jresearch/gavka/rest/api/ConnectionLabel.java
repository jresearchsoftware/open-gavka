package org.jresearch.gavka.rest.api;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@Value.Style(builder = "new") // builder has to have constructor
@JsonDeserialize(builder = ImmutableConnectionLabel.Builder.class)
public interface ConnectionLabel {

	String id();

	String label();

}
