package org.jresearch.gavka.gwt.core.client.module.overview;

import org.jresearch.commons.gwt.client.app.GeneralModule;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class OverviewModule extends GeneralModule {

	private static final long serialVersionUID = -1854389134724608555L;

	private static final String NAME = "Overview"; //$NON-NLS-1$
	// private static final String ICON = "mdi-action-track-changes";
	// //$NON-NLS-1$

	@SuppressWarnings("all")
	@Inject
	public OverviewModule() {
		super(NAME, OverviewController.ID, OverviewController.ID);
	}

}
