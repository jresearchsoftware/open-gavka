package org.jresearch.gavka.rest.data;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@Value.Style(builder = "new") // builder has to have constructor
@JsonDeserialize(builder = ImmutablePartitionInfo.Builder.class)
public interface PartitionInfo {

	long partitionNumber();

	long startOffset();

	long endOffset();

}
