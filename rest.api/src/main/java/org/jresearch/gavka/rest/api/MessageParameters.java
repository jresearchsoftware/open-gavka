package org.jresearch.gavka.rest.api;

import java.util.Optional;

import org.immutables.value.Value;
import org.jresearch.commons.gwt.shared.model.time.GwtLocalDateTimeModel;
import org.jresearch.gavka.domain.KeyFormat;
import org.jresearch.gavka.domain.MessageFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@Value.Style(builder = "new") // builder has to have constructor
@JsonDeserialize(builder = ImmutableMessageParameters.Builder.class)
public interface MessageParameters {

	String connection();

	String topic();

	String key();

	String valuePattern();

	Optional<GwtLocalDateTimeModel> from();

	KeyFormat keyFormat();

	MessageFormat messageFormat();

}
