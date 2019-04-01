package org.jresearch.gavka.gwt.core.client.module.consumer.widget;

import javax.annotation.Nonnull;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.REST;
import org.jresearch.commons.gwt.client.mvc.AbstractMethodCallback;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.module.consumer.srv.GavkaConsumerRestService;
import org.jresearch.gavka.rest.data.TopicRestInfo;

public class ConsumerDataSource {

	@Nonnull
	private final GavkaConsumerRestService srv;
	@Nonnull
	private final Bus bus;

	private boolean loading = false;
	private final String connection;
	private final String topic;

	public ConsumerDataSource(final String connection, final String topic, @Nonnull final GavkaConsumerRestService srv, @Nonnull final Bus bus) {
		this.connection = connection;
		this.topic = topic;
		this.srv = srv;
		this.bus = bus;
	}

	public void load(final ConsumerDataLoadCallback callback) {
		if (!loading) {
			loading = true;
			REST.withCallback(new AbstractMethodCallback<TopicRestInfo>(bus) {
				@Override
				public void onSuccess(final Method method, final TopicRestInfo result) {
					loading = false;
					callback.onLoad(result);
				}

				@Override
				public void onFailure(final Method method, final Throwable caught) {
					loading = false;
					callback.onLoad(null);
					super.onFailure(method, caught);
				}
			}).call(srv).get(connection, topic);
		}
	}

}
