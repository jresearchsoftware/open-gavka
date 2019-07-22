package org.jresearch.gavka.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
//Due GWT builder has to have constructor, optional accept null, standard getters
@Value.Style(get = { "get*", "is*" }, builder = "new", optionalAcceptNullable = true)
//For GWT deserialization
@JsonDeserialize(builder = ImmutableConnectionParameters.Builder.class)
public interface ConnectionParameters {

	List<String> getBootstrapServers();

	Optional<String> getSchemaRegistryUrl();

	Map<String, String> getProperties();

}
