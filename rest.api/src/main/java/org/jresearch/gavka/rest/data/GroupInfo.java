package org.jresearch.gavka.rest.data;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableGroupInfo.class)
@JsonDeserialize(as = ImmutableGroupInfo.class)
public interface GroupInfo {

	String groupId();

	int partition();

	long currentOffset();

	long lag();

}
