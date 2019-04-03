package org.jresearch.gavka.gwt.core.client.module;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.app.GeneralModule;

public class GafkaModule<T> extends GeneralModule {

	@Nonnull
	private static final String STRING = "."; //$NON-NLS-1$

	private final GafkaFactory<T> controllerFactory;

	public GafkaModule(@Nonnull final String name, @Nonnull final String moduleId, @Nonnull final GafkaFactory<T> controllerFactory) {
		super(name, moduleId, moduleId);
		this.controllerFactory = controllerFactory;
	}

	public GafkaFactory<T> getControllerFactory() {
		return controllerFactory;
	}

	@SuppressWarnings("null")
	@Nonnull
	public static String id(final String controllerId, final String connectionId, final String topic) {
		return String.join(STRING, controllerId, connectionId, topic);
	}

	public static Optional<String> getTopicName(final String id) {
		final int index = id.lastIndexOf('.');
		return index == -1 ? Optional.empty() : Optional.of(id.substring(index + 1));
	}

	public static Optional<String> getConnectionTopicName(final String id) {
		final int index = id.lastIndexOf('.', id.lastIndexOf('.') - 1);
		return index == -1 ? Optional.empty() : Optional.of(id.substring(index + 1));
	}

	public static Optional<String> getConnectionName(final String id) {
		final int endIndex = id.lastIndexOf('.') - 1;
		final int startIndex = id.lastIndexOf('.', endIndex);
		return startIndex == -1 ? Optional.empty() : Optional.of(id.substring(startIndex + 1, endIndex));
	}

}
