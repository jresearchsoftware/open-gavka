package org.jresearch.gavka.srv;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.jresearch.gavka.domain.Connection;

public interface ConnectionService {

	enum KafkaVersion {
		LATEST
	}

	List<Connection> connections();

	Optional<Connection> get(String conectionId);

	boolean update(Connection connection);

	boolean remove(String id);

	Optional<Properties> getKafkaConnectionProperties(String conectionId, KafkaVersion ver);

}