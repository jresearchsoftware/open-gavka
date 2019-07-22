package org.jresearch.gavka.domain;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
//Due GWT builder has to have constructor, optional accept null, standard getters
@Value.Style(builder = "new", optionalAcceptNullable = true)
//For GWT deserialization
@JsonDeserialize(builder = ImmutableSimpleCheck.Builder.class)
public interface SimpleCheck<O> {

	/** Object to check */
	O subject();

	CheckStatus status();

	Optional<String> reason();

}
