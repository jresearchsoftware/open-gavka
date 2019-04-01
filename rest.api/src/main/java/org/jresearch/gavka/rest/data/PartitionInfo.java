package org.jresearch.gavka.rest.data;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutablePartitionInfo.class)
@JsonDeserialize(as = ImmutablePartitionInfo.class)
public interface PartitionInfo {

	long partitionNumber();

	long startOffset();

	long endOffset();

}
