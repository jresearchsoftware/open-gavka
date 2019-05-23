package conf;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan("org.jresearch.gavka")
@SuppressWarnings("nls")
public class BackendConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() { return new PropertySourcesPlaceholderConfigurer(); }

	@Bean
	public static Flyway gavkaMigration(final DataSource dataSource) {
		final Flyway flyway = new Flyway(configure(dataSource));
		flyway.migrate();
		return flyway;
	}

	private static org.flywaydb.core.api.configuration.Configuration configure(final DataSource dataSource) {
		final FluentConfiguration configuration = new FluentConfiguration();
		configuration.dataSource(dataSource);
		configuration.table("schema_version_gavka");
		configuration.locations("dbGavka");
		configuration.baselineOnMigrate(true);
		configuration.baselineVersion("0000");
		configuration.placeholderPrefix("{{");
//		configuration.validateOnMigrate(false);
		return configuration;
	}
}
