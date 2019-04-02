package org.jresearch.gavka.rest.data;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@Value.Style(builder = "new") // builder has to have constructor
@JsonDeserialize(builder = ImmutableTopicRestInfo.Builder.class)
public interface TopicRestInfo {

	String name();

	List<PartitionInfo> partitionInfo();

	List<GroupInfo> groupInfo();

}