package org.jresearch.gavka.gwt.core.client.module;

import javax.annotation.Nonnull;

import org.jresearch.gavka.rest.data.GafkaCoordinates;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GafkaViewProvider<V> implements AsyncProvider<V> {

	@Nonnull
	private final GafkaFactory<V> factory;
	@Nonnull
	private final GafkaCoordinates gafkaCoordinates;

	public GafkaViewProvider(@Nonnull final GafkaFactory<V> factory, @Nonnull final GafkaCoordinates gafkaCoordinates) {
		this.factory = factory;
		this.gafkaCoordinates = gafkaCoordinates;
	}

	@Override
	public void get(final AsyncCallback<? super V> callback) {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess(factory.create(gafkaCoordinates));
			}

			@Override
			public void onFailure(final Throwable ex) {
				callback.onFailure(ex);
			}
		});
	}

}
