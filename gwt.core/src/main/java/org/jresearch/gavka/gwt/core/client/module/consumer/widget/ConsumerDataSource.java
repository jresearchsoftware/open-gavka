package org.jresearch.gavka.gwt.core.client.module.consumer.widget;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.module.GafkaModule;
import org.jresearch.gavka.gwt.core.client.module.consumer.srv.GavkaConsumerRestService;
import org.jresearch.gavka.rest.data.GafkaCoordinates;
import org.jresearch.gavka.rest.data.TopicRestInfo;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;

import elemental2.dom.DomGlobal;
import elemental2.dom.Location;
import elemental2.dom.WebSocket;

public class ConsumerDataSource {

	public static interface TopicRestInfoMapper extends ObjectMapper<TopicRestInfo> {
	}

	public static interface GafkaCoordinatesMapper extends ObjectMapper<GafkaCoordinates> {
	}

	@Nonnull
	private final GavkaConsumerRestService srv;
	@Nonnull
	private final Bus bus;

	private final boolean loading = false;
	private final String connection;
	private final String topic;

	public ConsumerDataSource(final String connection, final String topic, @Nonnull final GavkaConsumerRestService srv, @Nonnull final Bus bus, final ConsumerDataLoadCallback callback) {
		this.connection = connection;
		this.topic = topic;
		this.srv = srv;
		this.bus = bus;
		final TopicRestInfoMapper topicRestInfoMapper = GWT.create(TopicRestInfoMapper.class);
		final GafkaCoordinatesMapper gafkaCoordinatesMapper = GWT.create(GafkaCoordinatesMapper.class);
		final Location location = DomGlobal.location;
		final boolean secure = location.getProtocol().equals("https"); //$NON-NLS-1$
		final WebSocket socket = new WebSocket(secure ? "wss" : "ws" + "://" + location.getHost() + "/api/rest/ws/consumer");
		socket.onopen = e -> {
			// register for connection an topic
			final GafkaCoordinates coordinates = GafkaModule.create(connection, topic);
			socket.send(gafkaCoordinatesMapper.write(coordinates));
			return null;
		};
		socket.onmessage = m -> {
			final String json = (String) m.data;
			callback.onLoad(topicRestInfoMapper.read(json));
			return null;
		};

	}

	public void load(final ConsumerDataLoadCallback callback) {
//		if (!loading) {
//			loading = true;
//			REST.withCallback(new AbstractMethodCallback<TopicRestInfo>(bus) {
//				@Override
//				public void onSuccess(final Method method, final TopicRestInfo result) {
//					loading = false;
//					callback.onLoad(result);
//				}
//
//				@Override
//				public void onFailure(final Method method, final Throwable caught) {
//					loading = false;
//					callback.onLoad(null);
//					super.onFailure(method, caught);
//				}
//			}).call(srv).get(connection, topic);
//		}
	}

}
