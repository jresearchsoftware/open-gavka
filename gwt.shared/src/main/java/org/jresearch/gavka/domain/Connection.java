package org.jresearch.gavka.domain;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@Value.Modifiable
//Due GWT builder has to have constructor, optional accept null, standard getters
@Value.Style(get = { "get*", "is*" }, builder = "new", optionalAcceptNullable = true)
//For GWT deserialization
@JsonDeserialize(builder = ImmutableConnection.Builder.class)
public interface Connection extends ConnectionParameters {

	@Value.Default
	default String getId() {
		return "";
	}

	@Value.Default
	default String getLabel() {
		return "";
	}

	@Value.Default
	default String getIcon() {
		return "mdi-hubspot";
	}

	@Value.Default
	default String getColor() {
		return "TEAL LIGHTEN 5";
	}

	Optional<ConnectionCheck> connectionCheck();

}
