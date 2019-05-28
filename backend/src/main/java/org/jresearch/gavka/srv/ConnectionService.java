package org.jresearch.gavka.srv;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.Nonnull;

import org.jresearch.gavka.domain.Connection;

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

}
