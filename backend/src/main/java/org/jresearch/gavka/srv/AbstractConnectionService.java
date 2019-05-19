package org.jresearch.gavka.srv;

import java.util.Optional;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.jresearch.gavka.domain.Connection;

@SuppressWarnings("nls")
public abstract class AbstractConnectionService implements ConnectionService {

	private static final String PROP_SCHEMA_REGISTRY_URL = "schema.registry.url";

	@Override
	public Optional<Properties> getKafkaConnectionProperties(final String conectionId, final KafkaVersion ver) { return get(conectionId).map(AbstractConnectionService::toProperties); }

	private static Properties toProperties(final Connection conection) {
		final Properties result = new Properties();
		result.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, String.join(",", conection.getBootstrapServers()));
		conection.getSchemaRegistryUrl().ifPresent(url -> result.put(PROP_SCHEMA_REGISTRY_URL, url));
		return result;
	}

}
