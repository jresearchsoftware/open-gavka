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
import org.jresearch.commons.gwt.client.mvc.event.InitEvent;
import org.jresearch.commons.gwt.client.mvc.event.module.ModuleEvent;
import org.jresearch.commons.gwt.client.service.AppRestService;
import org.jresearch.commons.gwt.client.service.LocalizationRestService;
import org.jresearch.gavka.gwt.core.client.module.message.MessageController;
import org.jresearch.gavka.gwt.core.client.module.message.MessageControllerFactory;
import org.jresearch.gavka.gwt.core.client.module.message.srv.GavkaMessageRestService;
import org.jresearch.gavka.rest.api.ConnectionLabel;

import com.google.gwt.inject.client.AsyncProvider;

public class GavkaAppController extends AbstractAppController<GavkaAppView> {

	@Nonnull
	private static final String ID = "org.jresearch.gavka.gwt.core.client.app.GavkaAppController"; //$NON-NLS-1$
	@Nonnull
	private GavkaMessageRestService srv;
	private boolean needInit = true;
	@Nonnull
	private MessageControllerFactory messageControllerFactory;

	@Inject
	public GavkaAppController(@Nonnull final GavkaMessageRestService srv, @Nonnull final Set<IAppModule> appModules, @Nonnull final MessageControllerFactory messageControllerFactory, @Nonnull final AppRestService appService, @Nonnull final AsyncProvider<GavkaAppView> view, @Nonnull final LocalizationRestService localizationService, @Nonnull final Bus bus) {
		super(ID, appService, localizationService, appModules, view, bus, false);
		this.srv = srv;
		this.messageControllerFactory = messageControllerFactory;
	}

	@Override
	public void onInit(final InitEvent initEvent) {
		super.onInit(initEvent);
		// Load connections
		// Load topics for each connection
		// Load initial module with connection id and topic
		final String activeModuleId = getActiveModuleId();
		if (activeModuleId != null) {
			loadConnections(activeModuleId);
		}
	}

	private void loadConnections(String activeModuleId) {
		GwtMethodCallback<List<ConnectionLabel>> callback = new GwtMethodCallback<>(bus, r -> onConnectionLoad(activeModuleId, r));
		REST.withCallback(callback).call(srv).connections();
	}

	private void onConnectionLoad(String activeModuleId, List<ConnectionLabel> connections) {
		connections.forEach(c -> updateConnection(activeModuleId, c));
	}

	private void updateConnection(String activeModuleId, ConnectionLabel connection) {
		REST.withCallback(new GwtMethodCallback<List<String>>(bus, r -> onTopicsLoad(activeModuleId, connection, r))).call(srv).topics(connection.getId());
	}

	private void onTopicsLoad(String activeModuleId, ConnectionLabel connection, List<String> topics) {
		topics.forEach(t -> updateTopic(activeModuleId, connection, t));
	}

	private void updateTopic(String activeModuleId, ConnectionLabel connection, String topic) {
		getOptView().ifPresent(v -> v.addTopic(connection, topic));
		messageControllerFactory.create(connection.getId(), topic);
		if (needInit) {
			needInit = false;
			bus.fire(new ModuleEvent(MessageController.id(activeModuleId, connection.getId(), topic)));
		}
	}
}
