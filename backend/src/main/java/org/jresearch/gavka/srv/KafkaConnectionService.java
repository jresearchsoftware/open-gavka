package org.jresearch.gavka.srv;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.jresearch.gavka.dao.ConnectionDao;
import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.domain.ImmutableConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;

@Profile("default")
@Component
@SuppressWarnings("nls")
public class KafkaConnectionService extends AbstractConnectionService {

	@Value("${bootstrap.servers}")
	private String serverUrl;

	@Value("${schema.registry.url:#{null}}")
	private String schemaRegistryUrl;

	@Autowired
	private ConnectionDao connectionDao;

	@PostConstruct
	protected void init() {
		final Connection connection = new ImmutableConnection.Builder()
				.label("Default connection")
				.addAllBootstrapServers(Splitter.on(',').splitToList(serverUrl))
				.schemaRegistryUrl(Optional.ofNullable(schemaRegistryUrl))
				.build();
		update(connection);
	}

	@Override
	public List<Connection> connections() { return connectionDao.getConnections(); }

	@Override
	public Optional<Connection> get(final String id) { return connectionDao.getConnection(id); }

	@Override
	public Connection update(final Connection connection) {
		final Connection toSave = updateId(connection);
		connectionDao.updateConnection(toSave);
		return toSave;
	}

	private static Connection updateId(final Connection connection) { return connection.getId().isEmpty() ? new ImmutableConnection.Builder().from(connection).id(UUID.randomUUID().toString()).build() : connection; }

	@Override
	public boolean remove(final String id) {
		connectionDao.removeConnection(id);
		return true;
	}

}
