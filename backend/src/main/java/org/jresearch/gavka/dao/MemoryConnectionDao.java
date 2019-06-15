package org.jresearch.gavka.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.jresearch.gavka.domain.Connection;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableList;

import one.util.streamex.StreamEx;

@Repository
@Profile("nodb")
@SuppressWarnings("null")
public class MemoryConnectionDao implements IConnectionDao {

	@Nonnull
	private final List<Connection> connections = new ArrayList<>();

	@Override
	@Nonnull
	public List<Connection> getConnections() { return ImmutableList.copyOf(connections); }

	@Override
	public void updateConnection(@Nonnull final Connection connection) {
		getConnection(connection.getId()).ifPresent(connections::remove);
		connections.add(connection);
	}

	@Override
	public Optional<Connection> getConnection(@Nonnull final String id) {
		return StreamEx
				.of(connections)
				.filterBy(Connection::getId, id)
				.findAny();
	}

	@Override
	public void removeConnection(@Nonnull final String id) {
		final Optional<Connection> conn = getConnection(id);
		conn.ifPresent(connections::remove);
	}

	@Override
	public Optional<Connection> getByLabel(final String label) {
		return StreamEx
				.of(connections)
				.filterBy(Connection::getLabel, label)
				.findAny();
	}

}
