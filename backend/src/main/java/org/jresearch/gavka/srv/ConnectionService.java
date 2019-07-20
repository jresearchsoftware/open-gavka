package org.jresearch.gavka.srv;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.Nonnull;

import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.domain.ConnectionList;

public interface ConnectionService {

	enum KafkaVersion {
		LATEST
	}

	@Nonnull
	List<Connection> connections();

	@Nonnull
	Optional<Connection> get(@Nonnull String connectionId);

	@Nonnull
	Connection update(@Nonnull Connection connection);

	boolean remove(@Nonnull String id);

	@Nonnull
	Optional<Properties> getKafkaConnectionProperties(@Nonnull String connectionId, @Nonnull KafkaVersion ver);

	/**
	 * Imports connections from given list<br>
	 * Connections will be added to existing. Skip connections exist in DB already
	 *
	 * @param connections to import
	 */
	void importConnections(@Nonnull ConnectionList connections);

	/**
	 * Export all connection.
	 *
	 * @return
	 */
	@Nonnull
	ConnectionList exportConnections();

}
