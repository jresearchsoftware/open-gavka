package org.jresearch.gavka.gwt.core.client.module.logger;

import org.jresearch.commons.gwt.client.app.GeneralModule;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LoggerModule extends GeneralModule {

	private static final long serialVersionUID = -1854389134724608555L;

	private static final String NAME = "Loggers"; //$NON-NLS-1$

	@SuppressWarnings("all")
	@Inject
	public LoggerModule() {
		super(NAME, LoggerController.ID, LoggerController.ID);
	}

}
