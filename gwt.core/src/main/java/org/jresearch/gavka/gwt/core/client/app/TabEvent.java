package org.jresearch.gavka.gwt.core.client.app;

import javax.annotation.Nonnull;

import com.google.web.bindery.event.shared.Event;

public class TabEvent extends Event<TabHandler> {

	public static final Type<TabHandler> TYPE = new Type<>();

	@Nonnull
	private final String connectionTopicId;

	public TabEvent(@Nonnull final String connectionTopicId) {
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

	public String getConnectionTopicId() {
		return connectionTopicId;
	}

}
