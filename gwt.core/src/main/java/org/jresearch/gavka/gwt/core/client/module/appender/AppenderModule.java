package org.jresearch.gavka.gwt.core.client.module.appender;

import org.jresearch.commons.gwt.client.app.GeneralModule;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AppenderModule extends GeneralModule {

	private static final long serialVersionUID = -1854389134724608555L;

	private static final String NAME = "Appenders"; //$NON-NLS-1$

	@SuppressWarnings("all")
	@Inject
	public AppenderModule() {
		super(NAME, AppenderController.ID, AppenderController.ID);
	}

}
