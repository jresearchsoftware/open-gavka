package org.jresearch.gavka.srv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.jresearch.gavka.domain.CheckStatus;
import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.domain.ConnectionCheck;
import org.jresearch.gavka.domain.ConnectionParameters;
import org.jresearch.gavka.domain.ImmutableConnection;
import org.jresearch.gavka.domain.ImmutableConnectionCheck;
import org.jresearch.gavka.domain.ImmutableListCheck;
import org.jresearch.gavka.domain.ImmutableSimpleCheck;
import org.jresearch.gavka.domain.SimpleCheck;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

import one.util.streamex.StreamEx;

@Profile("nokafka")
@Component
@SuppressWarnings("nls")
public class MockConnectionService extends AbstractConnectionService {

	private static final String MOCK_REASON = "This is mock service";
	@Nonnull
	private final List<Connection> connections = new ArrayList<>();

	public MockConnectionService() {
		connections.add(new ImmutableConnection.Builder().label("con1Label").build());
	}

	@SuppressWarnings("null")
	@Override
	public List<Connection> connections() {
		return ImmutableList.copyOf(connections);
	}

	@SuppressWarnings("null")
	@Override
	public Optional<Connection> get(final String id) {
		return StreamEx
				.of(connections)
				.filterBy(Connection::getId, id)
				.findAny();
	}

	@SuppressWarnings("null")
	@Override
	public Connection update(final Connection connection) {
		get(connection.getId()).ifPresent(connections::remove);
		connections.add(connection);
		return connection;
	}

	@Override
	public boolean remove(final String id) {
		final Optional<Connection> conn = get(id);
		conn.ifPresent(connections::remove);
		return conn.isPresent();
	}

	@SuppressWarnings("null")
	@Override
	public ConnectionCheck check(final ConnectionParameters connectionParameters) {
		return new ImmutableConnectionCheck.Builder()
				.subject(connectionParameters)
				.status(CheckStatus.OK_WITH_WARNING)
				.reason(MOCK_REASON)
				.bootstrapServerCheck(new ImmutableListCheck.Builder<String>()
						.subject(connectionParameters.getBootstrapServers())
						.status(CheckStatus.OK_WITH_WARNING)
						.reason(MOCK_REASON)
						.checks(listCheck(connectionParameters.getBootstrapServers()))
						.build())
				.schemaRegistryUrlCheck(new ImmutableListCheck.Builder<String>()
						.subject(connectionParameters.getBootstrapServers())
						.status(CheckStatus.OK_WITH_WARNING)
						.reason(MOCK_REASON)
						.checks(listCheck(connectionParameters.getSchemaRegistryUrl().map(ImmutableList::of).orElseGet(ImmutableList::of)))
						.build())
				.propertiesCheck(new ImmutableListCheck.Builder<String>()
						.subject(connectionParameters.getProperties().keySet())
						.status(CheckStatus.OK_WITH_WARNING)
						.reason(MOCK_REASON)
						.checks(listCheck(connectionParameters.getProperties().keySet()))
						.build())
				.build();
	}

	@SuppressWarnings("null")
	@Nonnull
	private static List<SimpleCheck<String>> listCheck(@Nonnull final Collection<String> toCheck) {
		return StreamEx
				.of(toCheck)
				.map(MockConnectionService::stringCheck)
				.toImmutableList();
	}

	@SuppressWarnings("null")
	@Nonnull
	private static SimpleCheck<String> stringCheck(@Nonnull final String toCheck) {
		return new ImmutableSimpleCheck.Builder<String>()
				.subject(toCheck)
				.status(CheckStatus.OK_WITH_WARNING)
				.reason(MOCK_REASON)
				.build();
	}

}
