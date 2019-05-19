package org.jresearch.gavka.gwt.core.client.app;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.fusesource.restygwt.client.REST;
import org.jresearch.commons.gwt.client.app.AbstractAppController;
import org.jresearch.commons.gwt.client.app.IAppModule;
import org.jresearch.commons.gwt.client.mvc.GwtMethodCallback;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.commons.gwt.client.mvc.event.module.ModuleEvent;
import org.jresearch.commons.gwt.client.service.AppRestService;
import org.jresearch.commons.gwt.client.service.LocalizationRestService;
import org.jresearch.gavka.gwt.core.client.module.GafkaModule;
import org.jresearch.gavka.gwt.core.client.module.message.srv.GavkaMessageRestService;
import org.jresearch.gavka.rest.api.ConnectionLabel;
import org.jresearch.gavka.rest.data.GafkaCoordinates;

import com.google.gwt.inject.client.AsyncProvider;

public class GavkaAppController extends AbstractAppController<GavkaAppView> implements TabHandler {

	@Nonnull
	private static final String ID = "org.jresearch.gavka.gwt.core.client.app.GavkaAppController"; //$NON-NLS-1$
	@Nonnull
	private final GavkaMessageRestService srv;
	private boolean needInit = true;

	@Inject
	public GavkaAppController(@Nonnull final GavkaMessageRestService srv, @Nonnull final Set<IAppModule> appModules, @Nonnull final AppRestService appService, @Nonnull final AsyncProvider<GavkaAppView> view, @Nonnull final LocalizationRestService localizationService, @Nonnull final Bus bus) {
		super(ID, appService, localizationService, appModules, view, bus, false);
		this.srv = srv;
		bus.addHandler(TabEvent.TYPE, this);
	}

	@Override
	protected void onViewLoad() {
		// Load connections
		// Load topics for each connection
		// Load initial module with connection id and topic
		super.onViewLoad();
		final String activeModuleId = getActiveModuleId();
		if (activeModuleId != null) {
			loadConnections(activeModuleId);
		}
	}

	private void loadConnections(final String activeModuleId) {
		final GwtMethodCallback<List<ConnectionLabel>> callback = new GwtMethodCallback<>(bus, r -> onConnectionLoad(activeModuleId, r));
		REST.withCallback(callback).call(srv).connections();
	}

	private void onConnectionLoad(final String activeModuleId, final List<ConnectionLabel> connections) { connections.forEach(c -> updateConnection(activeModuleId, c)); }

	private void updateConnection(final String activeModuleId, final ConnectionLabel connection) { REST.withCallback(new GwtMethodCallback<List<String>>(bus, r -> onTopicsLoad(activeModuleId, connection, r))).call(srv).topics(connection.id()); }

	private void onTopicsLoad(final String activeModuleId, final ConnectionLabel connection, final List<String> topics) { topics.forEach(t -> updateTopic(activeModuleId, connection, t)); }

	private void updateTopic(final String activeModuleId, final ConnectionLabel connection, final String topic) {
		getOptView().ifPresent(v -> v.addTopic(connection, topic));
		final GafkaCoordinates coordinates = GafkaModule.create(connection.id(), topic);
		getModules().stream()
				.filter(m -> m instanceof GafkaModule)
				.map(m -> (GafkaModule) m)
				.map(GafkaModule::getControllerFactory)
				.forEach(f -> f.create(coordinates));
		if (needInit) {
			needInit = false;
			bus.fire(new ModuleEvent(GafkaModule.id(activeModuleId, coordinates)));
			// Select topic in navigation tree
			bus.fire(new TabEvent(coordinates));
		}
	}

	@Override
	public void onConnectionTab(final TabEvent event) { getOptView().ifPresent(v -> v.selectTopic(event.getConnectionTopicId())); }
}
