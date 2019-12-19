package org.jresearch.gavka.gwt.core.client.module.consumer.widget;

import org.jresearch.gavka.rest.data.GafkaCoordinates;
import org.jresearch.gavka.rest.data.TopicRestInfo;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;

import elemental2.dom.DomGlobal;
import elemental2.dom.Location;
import elemental2.dom.WebSocket;

public class ConsumerDataSource {

	public static interface TopicRestInfoMapper extends ObjectMapper<TopicRestInfo> {
		// nothing
	}

	public static interface GafkaCoordinatesMapper extends ObjectMapper<GafkaCoordinates> {
		// nothing
	}

	public ConsumerDataSource(final GafkaCoordinates coordinates, final ConsumerDataLoadCallback callback) {
		final TopicRestInfoMapper topicRestInfoMapper = GWT.create(TopicRestInfoMapper.class);
		final GafkaCoordinatesMapper gafkaCoordinatesMapper = GWT.create(GafkaCoordinatesMapper.class);
		final Location location = DomGlobal.location;
		final boolean secure = location.protocol.equals("https"); //$NON-NLS-1$
		final WebSocket socket = new WebSocket(secure ? "wss" : "ws" + "://" + location.host + "/api/ws/consumer");
		socket.onopen = e -> {
			// register for connection an topic
			socket.send(gafkaCoordinatesMapper.write(coordinates));
		};

		socket.onmessage = m -> {
			final String json = m.data.asString();
			callback.onLoad(topicRestInfoMapper.read(json));
		};

	}

}
