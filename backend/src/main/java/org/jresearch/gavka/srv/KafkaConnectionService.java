package org.jresearch.gavka.srv;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.jresearch.gavka.dao.IConnectionDao;
import org.jresearch.gavka.domain.CheckStatus;
import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.domain.ConnectionCheck;
import org.jresearch.gavka.domain.ConnectionParameters;
import org.jresearch.gavka.domain.ImmutableConnection;
import org.jresearch.gavka.domain.ImmutableConnectionCheck;
import org.jresearch.gavka.domain.ImmutableListCheck;
import org.jresearch.gavka.domain.ImmutableSimpleCheck;
import org.jresearch.gavka.domain.SimpleCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;

import one.util.streamex.StreamEx;

@Profile("!nokafka")
@Component
@SuppressWarnings("nls")
public class KafkaConnectionService extends AbstractConnectionService {

	private static final String NOT_IMPLEMENTED_REASON = "Check not implemented yet";

	private static final String INVALID_URL = "Invalid URL";

	private static final Integer TIMEOUT = 10000;

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConnectionService.class);

	@Value("${bootstrap.servers:}")
	private String serverUrl;

	@Value("${schema.registry.url:}")
	private String schemaRegistryUrl;

	@Autowired
	private IConnectionDao iConnectionDao;

	@PostConstruct
	protected void init() {
		if (serverUrl.isEmpty() && schemaRegistryUrl.isEmpty()) {
			return;
		}
		final Optional<Connection> defaultConnection = iConnectionDao.getByLabel("Default connection");

		@SuppressWarnings("null")
		final Connection connection = defaultConnection.map(new ImmutableConnection.Builder()::from)
				.orElseGet(ImmutableConnection.Builder::new).label("Default connection")
				.bootstrapServers(Splitter.on(',').splitToList(serverUrl))
				.schemaRegistryUrl(Splitter.on(',').splitToList(schemaRegistryUrl)).build();
		update(connection);
	}

	@Override
	public List<Connection> connections() {
		return iConnectionDao.getConnections();
	}

	@Override
	public Optional<Connection> get(final String id) {
		return iConnectionDao.getConnection(id);
	}

	@Override
	public Connection update(final Connection connection) {
		final Connection toSave = updateId(connection);
		iConnectionDao.updateConnection(toSave);
		return toSave;
	}

	@Nonnull
	private static Connection updateId(@Nonnull final Connection connection) {
		return connection.getId().isEmpty()
				? new ImmutableConnection.Builder().from(connection).id(UUID.randomUUID().toString()).build()
				: connection;
	}

	@Override
	public boolean remove(final String id) {
		iConnectionDao.removeConnection(id);
		return true;
	}

	@SuppressWarnings("null")
	@Override
	public ConnectionCheck check(final ConnectionParameters connectionParameters) {

		final ImmutableListCheck<String> bootstrapChecks = checkBootstrapServers(connectionParameters.getBootstrapServers());
		final ImmutableListCheck<String> registryChecks = checkSchemaRegistry(connectionParameters.getSchemaRegistryUrl());

		CheckStatus status = bootstrapChecks.status();
		Optional<String> reason = bootstrapChecks.reason();
		final Properties props = new Properties();
		props.putAll(connectionParameters.getProperties());
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, connectionParameters.getBootstrapServers());
		final AdminClient client = AdminClient.create(props);
		final ListTopicsOptions options = new ListTopicsOptions();
		options.timeoutMs(TIMEOUT);
		try {
			client.listTopics(options);
		} catch (final Exception e) {
			LOGGER.error("Error in creating admin client", e);
			status = CheckStatus.ERROR;
			reason = Optional.of(e.getMessage());
		}
		if (status == CheckStatus.OK && registryChecks.status() != CheckStatus.OK) {
			status = CheckStatus.OK_WITH_WARNING;
			reason = registryChecks.reason();
		}
		return new ImmutableConnectionCheck.Builder().subject(connectionParameters).status(status).reason(reason)
				.bootstrapServerCheck(bootstrapChecks).schemaRegistryUrlCheck(registryChecks)
				.propertiesCheck(
						new ImmutableListCheck.Builder<String>().subject(connectionParameters.getProperties().keySet())
								.status(CheckStatus.OK).reason(Optional.empty()).build())
				.build();
	}

	private ImmutableListCheck<String> checkBootstrapServers(final List<String> bootstraps) {
		CheckStatus status = CheckStatus.OK;
		Optional<String> reason = Optional.empty();
		final List<SimpleCheck<String>> checks = new ArrayList<>();
		long errors = 0;
		for (final String server : bootstraps) {
			final String[] tokens = server.split(":");
			try {
				final SimpleCheck<String> check = checkServer(tokens[0], Integer.parseInt(tokens[1]));
				if (check.status() == CheckStatus.ERROR) {
					errors++;
					if (errors == bootstraps.size()) {
						status = CheckStatus.ERROR;
					} else {
						status = CheckStatus.OK_WITH_WARNING;
					}
				}
				checks.add(check);
			} catch (final Exception e) {
				LOGGER.error("Runtime error in bootstraps", e);
				status = CheckStatus.ERROR;
				reason = Optional.of(INVALID_URL);
			}
		}
		return new ImmutableListCheck.Builder<String>().subject(bootstraps).status(status).reason(reason).checks(checks)
				.build();
	}

	private static ImmutableListCheck<String> checkSchemaRegistry(final List<String> servers) {
		CheckStatus status = CheckStatus.OK;
		final Optional<String> reason = Optional.empty();
		final List<SimpleCheck<String>> checks = StreamEx.of(servers).map(KafkaConnectionService::checkSchemaRegistry)
				.toImmutableList();
		final long errors = checks.stream().filter(s -> (s.status() == CheckStatus.ERROR)).count();
		if (errors > 0) {
			if (errors == servers.size()) {
				status = CheckStatus.ERROR;
			} else {
				status = CheckStatus.OK_WITH_WARNING;
			}
		}
		return new ImmutableListCheck.Builder<String>().subject(servers).status(status).reason(reason).checks(checks)
				.build();
	}

	private static SimpleCheck<String> checkServer(final String serverUrl, final int serverPort) {
		CheckStatus status = CheckStatus.OK;
		Optional<String> reason = Optional.empty();
		try (Socket s = new Socket(serverUrl, serverPort)) {
//do nothing
		} catch (final IOException ex) {
			LOGGER.error("Runtime error in server connection", ex);
			status = CheckStatus.ERROR;
			reason = Optional.of(ex.getMessage());
		}
		return new ImmutableSimpleCheck.Builder<String>().subject(serverUrl).status(status).reason(reason).build();
	}

	private static SimpleCheck<String> checkSchemaRegistry(final String serverUrl) {
		CheckStatus status = CheckStatus.ERROR;
		Optional<String> reason = Optional.empty();
		try {
			final URL url = new URL(serverUrl + "/subjects");
			HttpURLConnection.setFollowRedirects(false);
			final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			final int responseCode = httpURLConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				status = CheckStatus.OK;
			} else {
				status = CheckStatus.ERROR;
				reason = Optional.of(httpURLConnection.getResponseMessage());
			}
		} catch (final Exception e) {
			LOGGER.error("Runtime error in schema registry", e);
			reason = Optional.of(e.getMessage());
		}
		return new ImmutableSimpleCheck.Builder<String>().subject(serverUrl).status(status).reason(reason).build();
	}

	@Nonnull
	private static List<SimpleCheck<String>> listCheck(@Nonnull final Collection<String> toCheck) {
		return StreamEx.of(toCheck).map(KafkaConnectionService::stringCheck).toImmutableList();
	}

	@SuppressWarnings("null")
	@Nonnull
	private static SimpleCheck<String> stringCheck(@Nonnull final String toCheck) {
		return new ImmutableSimpleCheck.Builder<String>().subject(toCheck).status(CheckStatus.OK_WITH_WARNING)
				.reason(NOT_IMPLEMENTED_REASON).build();
	}

}
