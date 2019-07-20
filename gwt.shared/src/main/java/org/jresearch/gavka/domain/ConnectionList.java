package org.jresearch.gavka.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableConnectionList.class)
@JsonDeserialize(as = ImmutableConnectionList.class)
public interface ConnectionList {

	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	LocalDateTime timestamp();

	List<Connection> connections();

}
