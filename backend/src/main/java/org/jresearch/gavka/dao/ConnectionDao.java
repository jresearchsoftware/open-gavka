package org.jresearch.gavka.dao;

import static org.jresearch.gavka.dao.jooq.tables.Connection.*;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jresearch.gavka.dao.jooq.tables.records.ConnectionRecord;
import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.domain.ImmutableConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import one.util.streamex.StreamEx;

@Repository
public class ConnectionDao {

	private static final char SEPARATOR = ',';
	private static final Splitter OMIT_EMPTY_STRINGS = Splitter.on(SEPARATOR).omitEmptyStrings();

	@Autowired
	private DSLContext dslContext;

	public List<Connection> getConnections() {
		final Result<ConnectionRecord> result = dslContext.fetch(CONNECTION);
		return StreamEx.of(result).map(ConnectionDao::map).toList();
	}

	@SuppressWarnings("null")
	public void updateConnection(@Nonnull final Connection connection) {
		Preconditions.checkNotNull(connection.getId());
		final ConnectionRecord record = getConnectionRecord(connection.getId()).orElseGet(this::newOne);
		record.setBootstrapServers(toString(connection.getBootstrapServers()));
		record.setColor(connection.getColor());
		record.setIcon(connection.getIcon());
		record.setId(connection.getId());
		record.setLabel(connection.getLabel());
		record.setSchemaRegistryUrl(connection.getSchemaRegistryUrl().orElse(null));
		record.store();
	}

	private ConnectionRecord newOne() { return dslContext.newRecord(CONNECTION); }

	private Optional<ConnectionRecord> getConnectionRecord(@Nonnull final String id) { return Optional.ofNullable(dslContext.fetchOne(CONNECTION, CONNECTION.ID.eq(id))); }

	public Optional<Connection> getConnection(@Nonnull final String id) { return getConnectionRecord(id).map(ConnectionDao::map); }

	public void removeConnection(@Nonnull final String id) { dslContext.delete(CONNECTION).where(CONNECTION.ID.eq(id)).execute(); }

	@SuppressWarnings("null")
	public static Connection map(final ConnectionRecord record) { return new ImmutableConnection.Builder()
			.id(record.getId())
			.color(record.getColor())
			.bootstrapServers(toList(record.getBootstrapServers()))
			.icon(record.getIcon())
			.label(record.getLabel())
			.schemaRegistryUrl(record.getSchemaRegistryUrl())
			.build(); }

	public static List<String> toList(final String values) { return values == null ? ImmutableList.of() : OMIT_EMPTY_STRINGS.splitToList(values); }

	public static String toString(final List<String> value) { return value == null ? null : String.join(String.valueOf(SEPARATOR), value); }
}
