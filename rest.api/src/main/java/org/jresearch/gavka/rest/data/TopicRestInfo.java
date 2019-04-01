package org.jresearch.gavka.rest.data;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableTopicRestInfo.class)
@JsonDeserialize(as = ImmutableTopicRestInfo.class)
public interface TopicRestInfo {

	String name();

	List<PartitionInfo> partitionInfo();

	List<GroupInfo> groupInfo();

}