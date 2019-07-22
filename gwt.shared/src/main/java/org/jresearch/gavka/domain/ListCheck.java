package org.jresearch.gavka.domain;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
//Due GWT builder has to have constructor, optional accept null, standard getters
@Value.Style(builder = "new", optionalAcceptNullable = true)
//For GWT deserialization
@JsonDeserialize(builder = ImmutableListCheck.Builder.class)
public interface ListCheck<O> extends SimpleCheck<List<O>> {

	List<SimpleCheck<O>> checks();

}
