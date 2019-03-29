package org.jresearch.gavka.gwt.core.client.module.message;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GafkaViewProvider implements AsyncProvider<MessageView> {

	private MessageViewFactory factory;
	private String connectionId;
	private String topic;

	public GafkaViewProvider(MessageViewFactory factory, String connectionId, String topic) {
		this.factory = factory;
		this.connectionId = connectionId;
		this.topic = topic;
	}

	@Override
	public void get(AsyncCallback<? super MessageView> callback) {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess(factory.create(connectionId, topic));
			}

			@Override
			public void onFailure(Throwable ex) {
				callback.onFailure(ex);
			}
		});
	}

}
