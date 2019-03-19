package org.jresearch.gavka.srv;

import java.util.Optional;
import java.util.Properties;

import org.jresearch.gavka.domain.Connection;

public abstract class AbstractConnectionService implements ConnectionService {

	private static final String PROP_SCHEMA_REGISTRY_URL = "schema.registry.url";
	private static final String PROP_BOOTSTRAP_SERVERS = "bootstrap.servers";

	@Override
	public Optional<Properties> getKafkaConnectionProperties(String conectionId, KafkaVersion ver) {
		return get(conectionId).map(AbstractConnectionService::toProperties);
	}

	private static Properties toProperties(Connection conection) {
		final Properties result = new Properties();
		result.put(PROP_BOOTSTRAP_SERVERS, String.join(",", conection.getBootstrapServers()));
		if (conection.getSchemaRegistryUrl() != null) {
			result.put(PROP_SCHEMA_REGISTRY_URL, conection.getSchemaRegistryUrl());
		}
		return result;
	}

}
