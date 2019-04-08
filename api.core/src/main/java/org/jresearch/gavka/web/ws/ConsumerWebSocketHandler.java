package org.jresearch.gavka.web.ws;

import java.io.IOException;
import java.util.Set;

import javax.annotation.Nonnull;

import org.jresearch.gavka.domain.TopicInfo;
import org.jresearch.gavka.rest.data.GafkaCoordinates;
import org.jresearch.gavka.rest.data.ImmutableGafkaCoordinates;
import org.jresearch.gavka.rest.data.TopicRestInfo;
import org.jresearch.gavka.srv.MessageService;
import org.jresearch.gavka.web.GavkaConsumerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

@Component
@SuppressWarnings("nls")
public class ConsumerWebSocketHandler extends AbstractWebSocketHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerWebSocketHandler.class);

	@Autowired
	private MessageService messageService;

	@SuppressWarnings("null")
	@Nonnull
	private final Multimap<GafkaCoordinates, WebSocketSession> sessions = HashMultimap.create();

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	@SuppressWarnings("resource")
	public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
		Multimaps.filterValues(sessions, new ConsumerSessionDecorator(session)::equals).clear();
		super.afterConnectionClosed(session, status);
	}

	@SuppressWarnings("resource")
	@Override
	protected void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
		final String payload = message.getPayload();
		if (!payload.isEmpty()) {
			final GafkaCoordinates coordinates = objectMapper.readValue(payload, GafkaCoordinates.class);
			// remove all possible session duplicates
			Multimaps.filterValues(sessions, session::equals).clear();
			sessions.put(coordinates, new ConsumerSessionDecorator(session));

			final TopicInfo info = messageService.getTopic(coordinates.connectionId(), coordinates.topic());

			sendConsumerUpdate(coordinates.connectionId(), coordinates.topic(), info);
		}

		super.handleTextMessage(session, message);
	}

	@SuppressWarnings("null")
	public void sendConsumerUpdate(@Nonnull final String connectionId, @Nonnull final String topic, final TopicInfo info) {
		try {
			final TopicRestInfo rest = GavkaConsumerController.toRest(info);
			final TextMessage message = new TextMessage(objectMapper.writeValueAsString(rest));
			final GafkaCoordinates coordinates = create(connectionId, topic);
			sessions.get(coordinates)
					.forEach(s -> sendConsumerUpdate(s, message));
		} catch (final JsonProcessingException e) {
			LOGGER.error("Can't send consumer info update ({}) for connectionId {} and topic {} - JSON serialization error", info, connectionId, topic, e);
		}
	}

	private static GafkaCoordinates create(@Nonnull final String connectionId, @Nonnull final String topic) {
		return new ImmutableGafkaCoordinates.Builder().connectionId(connectionId).topic(topic).build();
	}

	public static void sendConsumerUpdate(final WebSocketSession session, final TextMessage message) {
		if (!session.isOpen()) {
			LOGGER.warn("Try to use closed session {}", session);
			return;
		}
		try {
			session.sendMessage(message);
		} catch (final IOException e) {
			LOGGER.error("Can't send consumer info update to {}", session, e);
		}
	}

	public Set<GafkaCoordinates> getGafkaCoordinates() {
		return sessions.keySet();
	}

}
