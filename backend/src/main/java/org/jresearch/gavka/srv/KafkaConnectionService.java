package org.jresearch.gavka.srv;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;

import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.domain.ImmutableConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import one.util.streamex.StreamEx;

@Profile("default")
@Component
public class KafkaConnectionService extends AbstractConnectionService {

	@Value("${bootstrap.servers}")
	private String serverUrl;

	@Value("${schema.registry.url:#{null}}")
	private String schemaRegistryUrl;

	@Nonnull
	private final List<Connection> connections = new ArrayList<>();

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
	public List<Connection> connections() { return ImmutableList.copyOf(connections); }

	@Override
	public Optional<Connection> get(final String id) { return StreamEx.of(connections).filterBy(Connection::getId, id).findAny(); }

	@Override
	public boolean update(final Connection connection) {
		final Connection toSave = updateId(connection);
		get(toSave.getId()).ifPresent(connections::remove);
		return connections.add(toSave);
	}

	private static Connection updateId(final Connection connection) { return connection.getId().isEmpty() ? new ImmutableConnection.Builder().from(connection).id(UUID.randomUUID().toString()).build() : connection; }

	@Override
	public boolean remove(final String id) {
		final Optional<Connection> conn = get(id);
		conn.ifPresent(connections::remove);
		return conn.isPresent();
	}

}
