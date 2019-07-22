package org.jresearch.gavka.domain;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
//Due GWT builder has to have constructor, optional accept null, standard getters
@Value.Style(builder = "new", optionalAcceptNullable = true)
//For GWT deserialization
@JsonDeserialize(builder = ImmutableConnectionCheck.Builder.class)
public interface ConnectionCheck extends SimpleCheck<ConnectionParameters> {

	ListCheck<String> bootstrapServerCheck();

	SimpleCheck<String> schemaRegistryUrlCheck();

	ListCheck<String> propertiesCheck();

}
