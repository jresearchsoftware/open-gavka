package org.jresearch.gavka.domain;

import java.time.LocalDateTime;
import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
public interface MessageFilter {

	String topic();

	String key();

	String valuePattern();

	Optional<LocalDateTime> from();

	KeyFormat keyFormat();

	MessageFormat messageFormat();

}
