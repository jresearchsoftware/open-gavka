package org.jresearch.gavka.gwt.core.client.app;

import javax.annotation.Nonnull;

import org.jresearch.gavka.rest.data.GafkaCoordinates;

import com.google.web.bindery.event.shared.Event;

public class TabEvent extends Event<TabHandler> {

	public static final Type<TabHandler> TYPE = new Type<>();

	@Nonnull
	private final GafkaCoordinates connectionTopicId;

	public TabEvent(@Nonnull final GafkaCoordinates connectionTopicId) {
		this.connectionTopicId = connectionTopicId;
	}

	@Override
	public Type<TabHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final TabHandler handler) {
		handler.onConnectionTab(this);
	}

	public GafkaCoordinates getConnectionTopicId() {
		return connectionTopicId;
	}

}
