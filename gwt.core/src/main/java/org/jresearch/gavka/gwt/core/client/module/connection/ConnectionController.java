package org.jresearch.gavka.gwt.core.client.module.connection;

import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.REST;
import org.jresearch.commons.gwt.client.mvc.AbstractModuleController;
import org.jresearch.commons.gwt.client.mvc.GwtMethodCallback;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.gwt.core.client.app.GavkaAppController;
import org.jresearch.gavka.gwt.core.client.module.connection.srv.GavkaConnectionRestService;

import com.google.gwt.inject.client.AsyncProvider;

@Singleton
public class ConnectionController extends AbstractModuleController<ConnectionView> {

	@Nonnull
	public static final String ID = "org.jresearch.gavka.gwt.core.client.module.connection.ConnectionController"; //$NON-NLS-1$
	private GavkaConnectionRestService srv;
	private GavkaAppController gavkaAppController;

	@Inject
	public ConnectionController(@Nonnull final Bus bus, @Nonnull final GavkaAppController appController, @Nonnull final AsyncProvider<ConnectionView> view, @Nonnull final GavkaConnectionRestService srv) {
		super(ID, bus, appController, view);
		gavkaAppController = appController;
		this.srv = srv;
	}

	@Override
	public void onViewCreate() {
		super.onViewCreate();
		REST.withCallback(new GwtMethodCallback<>(bus, this::addSubmodules)).call(srv).get();
	}

	public void addSubmodules(final List<Connection> connections) {
		connections.forEach(this::addSubmodule);
	}

	public void addSubmodule(Connection connection) {
		gavkaAppController.addSubmodule(ID, connection.getLabel());
	}

}
