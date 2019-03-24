package org.jresearch.gavka.gwt.core.client.app;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.jresearch.commons.gwt.client.app.AbstractAppController;
import org.jresearch.commons.gwt.client.app.IAppModule;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.commons.gwt.client.mvc.event.InitEvent;
import org.jresearch.commons.gwt.client.mvc.event.module.ModuleEvent;
import org.jresearch.commons.gwt.client.service.AppRestService;
import org.jresearch.commons.gwt.client.service.LocalizationRestService;

import com.google.gwt.inject.client.AsyncProvider;

public class GavkaAppController extends AbstractAppController<GavkaAppView> {

	@Nonnull
	private static final String ID = "org.jresearch.gavka.gwt.core.client.app.GavkaAppController"; //$NON-NLS-1$

	@Inject
	public GavkaAppController(@Nonnull final Set<IAppModule> appModules, @Nonnull final AppRestService appService, @Nonnull final AsyncProvider<GavkaAppView> view, @Nonnull final LocalizationRestService localizationService, @Nonnull final Bus bus) {
		super(ID, appService, localizationService, appModules, view, bus, false);
	}

	@Override
	public void onInit(final InitEvent initEvent) {
		super.onInit(initEvent);
		// load initial module
		final String activeModuleId = getActiveModuleId();
		if (activeModuleId != null) {
			bus.fire(new ModuleEvent(activeModuleId));
		}
	}

	public void addSubmodule(String moduleId, String submoduleId, String title) {
		executeCommand(v -> v.addSubmodule(moduleId, submoduleId, title));
	}

}
