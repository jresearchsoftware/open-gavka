package org.jresearch.gavka.dao;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.jresearch.gavka.domain.Connection;

public interface IConnectionDao {

	@Nonnull
	List<Connection> getConnections();

	void updateConnection(@Nonnull Connection connection);

	@Nonnull
	Optional<Connection> getConnection(@Nonnull String id);

	void removeConnection(@Nonnull String id);

	@Nonnull
	Optional<Connection> getByLabel(@Nonnull String label);

}
