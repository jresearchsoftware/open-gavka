package org.jresearch.gavka.srv;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.jresearch.gavka.domain.Connection;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

import one.util.streamex.StreamEx;

@Profile("nokafka")
@Component
public class MockConnectionService extends AbstractConnectionService {

	@Nonnull
	private List<Connection> connections = new ArrayList<>();

	public MockConnectionService() {
		Connection connection = new Connection();
		connection.setId(UUID.randomUUID().toString());
		connection.setLabel("con1Label");
		connections.add(connection);
	}

	@Override
	public List<Connection> connections() {
		return ImmutableList.copyOf(connections);
	}

	@Override
	public Optional<Connection> get(String id) {
		return StreamEx.of(connections).filterBy(Connection::getId, id).findAny();
	}

	@Override
	public boolean update(Connection connection) {
		String id = connection.getId();
		if (id == null || id.isEmpty()) {
			connection.setId(UUID.randomUUID().toString());
		} else {
			get(id).ifPresent(connections::remove);
		}
		return connections.add(connection);
	}

	@Override
	public boolean remove(String id) {
		Optional<Connection> conn = get(id);
		conn.ifPresent(connections::remove);
		return conn.isPresent();
	}

}
