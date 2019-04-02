package org.jresearch.gavka.rest.data;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@Value.Style(builder = "new") // builder has to have constructor
@JsonDeserialize(builder = ImmutableGroupInfo.Builder.class)
public interface GroupInfo {

	String groupId();

	int partition();

	long currentOffset();

	long lag();

}
