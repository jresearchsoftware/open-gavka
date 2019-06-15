package org.jresearch.gavka.srv;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;

import org.jresearch.gavka.dao.IConnectionDao;
import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.domain.ImmutableConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;

@Profile("!nokafka")
@Component
@SuppressWarnings("nls")
public class KafkaConnectionService extends AbstractConnectionService {

	@Value("${bootstrap.servers}")
	private String serverUrl;

	@Value("${schema.registry.url:#{null}}")
	private String schemaRegistryUrl;

	@Autowired
	private IConnectionDao iConnectionDao;

	@PostConstruct
	protected void init() {
		final Optional<Connection> defaultConnection = iConnectionDao.getByLabel("Default connection");

		@SuppressWarnings("null")
		final Connection connection = defaultConnection
				.map(new ImmutableConnection.Builder()::from)
				.orElseGet(ImmutableConnection.Builder::new)
				.label("Default connection")
				.bootstrapServers(Splitter.on(',').splitToList(serverUrl))
				.schemaRegistryUrl(Optional.ofNullable(schemaRegistryUrl))
				.build();
		update(connection);
	}

	@Override
	public List<Connection> connections() {
		return iConnectionDao.getConnections();
	}

	@Override
	public Optional<Connection> get(final String id) {
		return iConnectionDao.getConnection(id);
	}

	@Override
	public Connection update(final Connection connection) {
		final Connection toSave = updateId(connection);
		iConnectionDao.updateConnection(toSave);
		return toSave;
	}

	@Nonnull
	private static Connection updateId(@Nonnull final Connection connection) {
		return connection.getId().isEmpty() ? new ImmutableConnection.Builder().from(connection).id(UUID.randomUUID().toString()).build() : connection;
	}

	@Override
	public boolean remove(final String id) {
		iConnectionDao.removeConnection(id);
		return true;
	}

}
