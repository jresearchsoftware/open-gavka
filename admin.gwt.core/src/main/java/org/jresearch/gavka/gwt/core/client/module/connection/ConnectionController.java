package org.jresearch.gavka.gwt.core.client.module.connection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.REST;
import org.jresearch.commons.gwt.client.mvc.AbstractModuleController;
import org.jresearch.commons.gwt.client.mvc.GwtMethodCallback;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.commons.gwt.client.mvc.event.module.ModuleEvent;
import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.domain.ModifiableConnection;
import org.jresearch.gavka.gwt.core.client.app.GavkaAppController;
import org.jresearch.gavka.gwt.core.client.module.connection.srv.GavkaConnectionRestService;

import com.google.gwt.inject.client.AsyncProvider;

@Singleton
public class ConnectionController extends AbstractModuleController<ConnectionView> {

	@Nonnull
	public static final String ID = "org.jresearch.gavka.gwt.core.client.module.connection.ConnectionController"; //$NON-NLS-1$
	@Nonnull
	private final GavkaConnectionRestService srv;
	private final GavkaAppController gavkaAppController;
	private final Map<String, Connection> cons = new HashMap<>();

	@Inject
	public ConnectionController(@Nonnull final Bus bus, @Nonnull final GavkaAppController appController, @Nonnull final AsyncProvider<ConnectionView> view, @Nonnull final GavkaConnectionRestService srv) {
		super(ID, bus, appController, view);
		gavkaAppController = appController;
		this.srv = srv;
	}

	@Override
	public void onModileActivation(final ModuleEvent event) {
		super.onModileActivation(event);
		refreshConnections();
	}

	@Override
	public void onModule(final ModuleEvent event) {
		super.onModule(event);
		if (isActive()) {
			final Connection connection = cons.get(event.getData());
			if (connection != null) {
				getOptView().ifPresent(v -> v.edit(ModifiableConnection.create().from(connection)));
			}
		}
	}

	public void onLoad(final List<Connection> connections) {
		cons.clear();
		connections.forEach(this::addSubmodule);
		getOptView().ifPresent(v -> v.updateConnections(connections));
	}

	public void addSubmodule(final Connection connection) {
		cons.put(connection.getId(), connection);
		gavkaAppController.addSubmodule(ID, connection.getId(), connection.getLabel());
	}

	public void refreshConnections() { REST.withCallback(new GwtMethodCallback<>(bus, this::onLoad)).call(srv).get(); }

}
