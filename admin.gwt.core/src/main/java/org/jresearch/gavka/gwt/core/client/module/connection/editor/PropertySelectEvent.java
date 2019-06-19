package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import javax.annotation.Nonnull;

import com.google.web.bindery.event.shared.Event;

public class PropertySelectEvent extends Event<PropertySelectHandler> {

	public static final Type<PropertySelectHandler> TYPE = new Type<>();

	@Nonnull
	private final Property property;

	public PropertySelectEvent(@Nonnull final Property property) {
		this.property = property;
	}

	@Override
	public Type<PropertySelectHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(final PropertySelectHandler handler) {
		handler.onPropertySelect(this);
	}

	public Property getProperty() { return property; }

}
