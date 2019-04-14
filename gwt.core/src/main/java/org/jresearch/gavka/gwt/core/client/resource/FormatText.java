package org.jresearch.gavka.gwt.core.client.resource;

import javax.annotation.Nonnull;

import com.google.gwt.i18n.client.Messages;

public interface FormatText extends Messages {

	@Nonnull
	@DefaultMessage("{0} ({1})")
	String appender(String name, String className);

	@Nonnull
	@DefaultMessage("No coordinates stored for module {0}")
	String coordinateError(String moduleId);

}
