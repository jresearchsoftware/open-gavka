package org.jresearch.gavka.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.domain.ImmutableConnection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import conf.BackendConfig;
import conf.BackendTestConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { BackendConfig.class, BackendTestConfig.class })
@SuppressWarnings("nls")
class PostgressConnectionDaoTest {

	@Autowired
	private IConnectionDao dao;

	@Test
	void testConnectionDao() {
		final List<Connection> connections01 = dao.getConnections();
		assertNotNull(connections01);

		final int sizeBefore = connections01.size();

		final String id = UUID.randomUUID().toString();
		final ImmutableConnection connection = new ImmutableConnection.Builder()
				.addBootstrapServers("boot")
				.addSchemaRegistryUrl("schemaregistry")
				.color("color")
				.icon("icon")
				.id(id)
				.label("labellabellabel")
				.putProperties("key", "value")
				.build();
		dao.updateConnection(connection);

		final List<Connection> connections02 = dao.getConnections();
		assertNotNull(connections02);

		assertEquals(sizeBefore + 1, connections02.size());

		final Optional<Connection> connection2 = dao.getConnection(id);

		assertTrue(connection2.isPresent());

		assertEquals(connection, connection2.get());

		final ImmutableConnection connection3 = new ImmutableConnection.Builder()
				.from(connection2.get())
				.color("color2")
				.build();

		dao.updateConnection(connection3);

		final Optional<Connection> byLabel = dao.getByLabel("labellabellabel");

		assertTrue(byLabel.isPresent());

		assertEquals(connection.getId(), byLabel.get().getId());
		assertEquals("color2", byLabel.get().getColor());

		dao.removeConnection(id);

		final List<Connection> connections03 = dao.getConnections();
		assertNotNull(connections03);

		assertEquals(sizeBefore, connections03.size());
	}

}
