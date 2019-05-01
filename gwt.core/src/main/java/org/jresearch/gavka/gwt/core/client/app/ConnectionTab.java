package org.jresearch.gavka.gwt.core.client.app;

import javax.annotation.Nonnull;

import org.dominokit.domino.ui.tabs.Tab;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.rest.data.GafkaCoordinates;

public class ConnectionTab extends Tab {

	@Nonnull
	private final Bus bus;
	@Nonnull
	private final GafkaCoordinates tabId;

	public ConnectionTab(@Nonnull final Bus bus, @Nonnull final GafkaCoordinates tabId) {
		super(tabId.topic());
		this.bus = bus;
		this.tabId = tabId;
	}

	public static ConnectionTab create(@Nonnull final Bus bus, @Nonnull final GafkaCoordinates tabId) {
		return new ConnectionTab(bus, tabId);
	}

	@Override
	public Tab activate() {
		bus.fire(new TabEvent(tabId));
		return super.activate();
	}

}
