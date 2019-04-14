package org.jresearch.gavka.gwt.core.client.module;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.app.GeneralModule;
import org.jresearch.gavka.gwt.core.client.resource.GavkaRs;
import org.jresearch.gavka.rest.data.GafkaCoordinates;
import org.jresearch.gavka.rest.data.ImmutableGafkaCoordinates;

public class GafkaModule<T> extends GeneralModule {

	@Nonnull
	private static final String STRING = "."; //$NON-NLS-1$
	@Nonnull
	private static final Map<String, GafkaCoordinates> COORDINATES = new HashMap<>();

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
	public static String id(final String controllerId, final GafkaCoordinates gafkaCoordinates) {
		return String.join(STRING, controllerId, gafkaCoordinates.connectionId(), gafkaCoordinates.topic());
	}

	public static GafkaCoordinates create(@Nonnull final String connectionId, @Nonnull final String topic) {
		return new ImmutableGafkaCoordinates.Builder().connectionId(connectionId).topic(topic).build();
	}

	@Nonnull
	public static GafkaCoordinates getCoordinates(@Nonnull final String moduleId) {
		final GafkaCoordinates gafkaCoordinates = COORDINATES.get(moduleId);
		if (gafkaCoordinates == null) {
			throw new IllegalStateException(GavkaRs.FMT.coordinateError(moduleId));
		}
		return gafkaCoordinates;
	}

	public static void addCoordinates(@Nonnull final String moduleId, @Nonnull final GafkaCoordinates gafkaCoordinates) {
		COORDINATES.put(moduleId, gafkaCoordinates);
	}

}
