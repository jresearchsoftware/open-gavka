package org.jresearch.gavka.gwt.core.client;

import org.fusesource.restygwt.client.Defaults;
import org.jresearch.commons.gwt.client.AbstractModule;
import org.jresearch.commons.gwt.client.AbstractModuleEntryPoint;
import org.jresearch.gavka.gwt.core.client.gin.GavkaGinjector;

import com.google.gwt.core.shared.GWT;

public class GavminAppEntryPoint extends AbstractModuleEntryPoint {

	@Override
	protected AbstractModule getModule() {
		final GavkaGinjector gin = GWT.create(GavkaGinjector.class);
		return gin.getAppModule();
	}

	@Override
	public void onModuleLoad() {
		// Set the root URL for rest calls
		Defaults.setServiceRoot("/api/");
		// Defaults.setServiceRoot(com.google.gwt.core.client.GWT.getHostPageBaseURL());
		super.onModuleLoad();
	}

}
