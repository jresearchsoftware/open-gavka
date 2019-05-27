package org.jresearch.gavka.dao;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jresearch.gavka.dao.jooq.tables.records.ConnectionRecord;
import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.domain.ImmutableConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
		final Result<ConnectionRecord> result = dslContext.fetch(org.jresearch.gavka.dao.jooq.tables.Connection.CONNECTION);
		return StreamEx.of(result).map(ConnectionDao::map).toList();
	}

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
