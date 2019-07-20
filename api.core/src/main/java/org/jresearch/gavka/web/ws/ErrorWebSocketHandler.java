package org.jresearch.gavka.web.ws;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

@Component
@SuppressWarnings("nls")
public class ErrorWebSocketHandler extends AbstractWebSocketHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorWebSocketHandler.class);

	@SuppressWarnings("null")
	@Nonnull
	private final Multimap<UUID, WebSocketSession> sessions = HashMultimap.create();

	@Override
	@SuppressWarnings("resource")
	public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
		Multimaps.filterValues(sessions, new ConsumerSessionDecorator(session)::equals).clear();
		super.afterConnectionClosed(session, status);
	}

	@SuppressWarnings({ "resource", "null" })
	@Override
	protected void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
		final String payload = message.getPayload();
		if (!payload.isEmpty()) {
			final UUID uuid = UUID.fromString(payload);
			// remove all possible session duplicates
			Multimaps.filterValues(sessions, session::equals).clear();
			sessions.put(uuid, new ConsumerSessionDecorator(session));
		}

		super.handleTextMessage(session, message);
	}

	public void sendMessage(@Nonnull final UUID uuid, @Nonnull final String errorMessage) {
		final TextMessage message = new TextMessage(errorMessage);
		sessions
				.get(uuid)
				.forEach(s -> sendMessage(s, message));
	}

	public void sendMessage(final WebSocketSession session, final TextMessage message) {
		if (!session.isOpen()) {
			LOGGER.warn("Session is already closed {}", session);
			// remove closed session
			Multimaps.filterValues(sessions, session::equals).clear();
			return;
		}
		try {
			session.sendMessage(message);
		} catch (final IOException e) {
			LOGGER.error("Can't send consumer info update to {}", session, e);
		}
	}

}
