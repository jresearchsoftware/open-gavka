package org.jresearch.gavka.srv;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.jresearch.gavka.domain.Connection;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

import one.util.streamex.StreamEx;

@Profile("default")
@Component
public class KafkaConnectionService implements ConnectionService {

	@Nonnull
	private List<Connection> connections = new ArrayList<>();

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
		return connections.add(connection);
	}

	@Override
	public boolean remove(String id) {
		Optional<Connection> conn = get(id);
		conn.ifPresent(connections::remove);
		return conn.isPresent();
	}
}
