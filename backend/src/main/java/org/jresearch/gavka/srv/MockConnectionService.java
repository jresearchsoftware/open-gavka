package org.jresearch.gavka.srv;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.domain.ImmutableConnection;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

import one.util.streamex.StreamEx;

@Profile("nokafka")
@Component
public class MockConnectionService extends AbstractConnectionService {

	@Nonnull
	private final List<Connection> connections = new ArrayList<>();

	public MockConnectionService() { connections.add(new ImmutableConnection.Builder().label("con1Label").build()); }

	@Override
	public List<Connection> connections() { return ImmutableList.copyOf(connections); }

	@Override
	public Optional<Connection> get(final String id) { return StreamEx
			.of(connections)
			.filterBy(Connection::getId, id)
			.findAny(); }

	@Override
	public boolean update(final Connection connection) {
		get(connection.getId()).ifPresent(connections::remove);
		return connections.add(connection);
	}

	@Override
	public boolean remove(final String id) {
		final Optional<Connection> conn = get(id);
		conn.ifPresent(connections::remove);
		return conn.isPresent();
	}

}
