package org.jresearch.gavka.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.server.rest.BaseSpringController;
import org.jresearch.gavka.domain.ConsumerGroupForTopic;
import org.jresearch.gavka.domain.PartitionInfoForConsumerGroup;
import org.jresearch.gavka.domain.PartitionOffsetInfo;
import org.jresearch.gavka.domain.TopicInfo;
import org.jresearch.gavka.rest.api.GavkaConsumerService;
import org.jresearch.gavka.rest.data.GroupInfo;
import org.jresearch.gavka.rest.data.ImmutableGroupInfo;
import org.jresearch.gavka.rest.data.ImmutablePartitionInfo;
import org.jresearch.gavka.rest.data.ImmutableTopicRestInfo;
import org.jresearch.gavka.rest.data.PartitionInfo;
import org.jresearch.gavka.rest.data.TopicRestInfo;
import org.jresearch.gavka.srv.ConsumerRetrievalException;
import org.jresearch.gavka.srv.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import one.util.streamex.StreamEx;

@RestController
@RequestMapping(GavkaConsumerService.SRV_PATH)
public class GavkaConsumerController extends BaseSpringController implements GavkaConsumerService {

	@Autowired
	private MessageService messageService;

	@Override
	@GetMapping(M_R_GET)
	public TopicRestInfo get(@PathVariable final String connectionId, @PathVariable final String topic) {
		try {
			return toRest(messageService.getTopic(connectionId, topic));
		} catch (final ConsumerRetrievalException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("null")
	public static TopicRestInfo toRest(final TopicInfo info) {
		return new ImmutableTopicRestInfo.Builder()
				.addAllGroupInfo(toRest(info.getConsumerGroups()))
				.addAllPartitionInfo(toRest(info.getPartitions()))
				.name(info.getName())
				.build();
	}

	@Nonnull
	@SuppressWarnings("null")
	private static Iterable<? extends PartitionInfo> toRest(final Map<Integer, PartitionOffsetInfo> partitions) {
		return StreamEx.ofValues(partitions).map(GavkaConsumerController::toRest).toList();
	}

	@Nonnull
	@SuppressWarnings("null")
	private static PartitionInfo toRest(final PartitionOffsetInfo partition) {
		return new ImmutablePartitionInfo.Builder()
				.endOffset(partition.getEndOffset())
				.partitionNumber(partition.getPartitionNumber())
				.startOffset(partition.getStartOffset())
				.build();
	}

	@Nonnull
	@SuppressWarnings("null")
	private static Iterable<? extends GroupInfo> toRest(final List<ConsumerGroupForTopic> consumerGroups) {
		return StreamEx.of(consumerGroups).flatCollection(GavkaConsumerController::toRest).toList();
	}

	@Nonnull
	@SuppressWarnings("null")
	private static List<GroupInfo> toRest(final ConsumerGroupForTopic partition) {
		return StreamEx.ofValues(partition.getPartitionInfo()).map(p -> toRest(partition.getGroupId(), p)).toList();
	}

	@Nonnull
	@SuppressWarnings("null")
	private static GroupInfo toRest(final String groupId, final PartitionInfoForConsumerGroup partition) {
		return new ImmutableGroupInfo.Builder()
				.currentOffset(partition.getCurrentOffset())
				.groupId(groupId)
				.lag(partition.getLag())
				.partition(partition.getPartition())
				.build();
	}
}
