package org.jresearch.gavka.web.ws;

import java.util.concurrent.TimeUnit;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jresearch.gavka.domain.TopicInfo;
import org.jresearch.gavka.rest.data.GafkaCoordinates;
import org.jresearch.gavka.srv.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Component
public class CousumerUpdater {

	@Autowired
	private MessageService messageService;
	@Autowired
	private ConsumerWebSocketHandler consumerWebSocketHandler;

	private final Cache<GafkaCoordinates, TopicInfo> updateCache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();

	@Scheduled(fixedRate = 1000)
	public void check() {
		consumerWebSocketHandler.getGafkaCoordinates().stream().parallel().forEach(this::check);
	}

	public void check(final GafkaCoordinates coordinates) {
		final TopicInfo topicInfo = messageService.getTopic(coordinates.connectionId(), coordinates.topic());
		if (topicInfo != null) {
			@Nullable
			final TopicInfo prevTopicInfo = updateCache.getIfPresent(coordinates);
			updateCache.put(coordinates, topicInfo);
			if (!topicInfo.equals(prevTopicInfo)) {
				consumerWebSocketHandler.sendConsumerUpdate(coordinates.connectionId(), coordinates.topic(), topicInfo);
			}
		}
	}
}
