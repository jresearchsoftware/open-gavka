package org.jresearch.gavka.gwt.core.client.gin;

import org.jresearch.commons.gwt.client.app.BaseAppModule;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(LogbackUiGinModule.class)
public interface LogbackUiGinjector extends Ginjector {

	BaseAppModule getAppModule();

}
