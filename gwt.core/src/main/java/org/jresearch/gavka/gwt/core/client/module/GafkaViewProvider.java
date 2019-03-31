package org.jresearch.gavka.gwt.core.client.module;

import org.jresearch.gavka.gwt.core.client.module.GafkaFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GafkaViewProvider<V> implements AsyncProvider<V> {

	private final GafkaFactory<V> factory;
	private final String connectionId;
	private final String topic;

	public GafkaViewProvider(final GafkaFactory<V> factory, final String connectionId, final String topic) {
		this.factory = factory;
		this.connectionId = connectionId;
		this.topic = topic;
	}

	@Override
	public void get(final AsyncCallback<? super V> callback) {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess(factory.create(connectionId, topic));
			}

			@Override
			public void onFailure(final Throwable ex) {
				callback.onFailure(ex);
			}
		});
	}

}
