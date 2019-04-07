package org.jresearch.gavka.web.ws;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import com.google.common.primitives.Ints;

public class ConsumerSessionDecorator extends ConcurrentWebSocketSessionDecorator {

	private static final int TIME_OUT = Ints.saturatedCast(TimeUnit.SECONDS.toMillis(5));

	public ConsumerSessionDecorator(final WebSocketSession delegate) {
		super(delegate, TIME_OUT, Integer.MAX_VALUE, OverflowStrategy.DROP);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof WebSocketSession)) {
			return false;
		}
		final WebSocketSession other = (WebSocketSession) obj;
		return Objects.equals(getId(), other.getId());
	}

}
