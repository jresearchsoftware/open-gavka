package org.jresearch.gavka.srv;

import java.util.List;
import java.util.Optional;

import org.jresearch.gavka.domain.Connection;

public interface ConnectionService {

	List<Connection> connections();

	Optional<Connection> get(String id);

	boolean add(Connection connection);

	boolean remove(String id);

}
