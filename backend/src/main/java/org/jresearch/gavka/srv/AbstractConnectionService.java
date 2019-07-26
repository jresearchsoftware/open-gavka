package org.jresearch.gavka.srv;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.Nonnull;

import org.apache.kafka.clients.CommonClientConfigs;
import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.domain.ConnectionList;
import org.jresearch.gavka.domain.ImmutableConnectionList;

@SuppressWarnings("nls")
public abstract class AbstractConnectionService implements ConnectionService {

	private static final String PROP_SCHEMA_REGISTRY_URL = "schema.registry.url";

	@SuppressWarnings("null")
	@Override
	public Optional<Properties> getKafkaConnectionProperties(final String conectionId, final KafkaVersion ver) {
		return get(conectionId).map(AbstractConnectionService::toProperties);
	}

	private static Properties toProperties(@Nonnull final Connection conection) {
		final Properties result = new Properties();
		result.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, String.join(",", conection.getBootstrapServers()));
		result.put(PROP_SCHEMA_REGISTRY_URL, String.join(",", conection.getSchemaRegistryUrl()));
		result.putAll(conection.getProperties());
		return result;
	}

	@Override
	public void importConnections(final ConnectionList connections) {
		connections.connections().forEach(this::importConnection);
	}

	private void importConnection(final Connection connection) {
		final String id = connection.getId();
		if (id.isEmpty() || !get(id).isPresent()) {
			update(connection);
		}
	}

	@SuppressWarnings("null")
	@Override
	public ConnectionList exportConnections() {
		return ImmutableConnectionList
				.builder()
				.addAllConnections(connections())
				.timestamp(LocalDateTime.now())
				.build();
	}
}
