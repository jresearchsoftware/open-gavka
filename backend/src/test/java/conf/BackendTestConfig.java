package conf;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@SuppressWarnings("static-method")
public class BackendTestConfig {

	@Bean
	public DataSource getDataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5433/gavkaDev");
		dataSource.setUsername("gavkaDev");
		dataSource.setPassword("gavkaDev");
		return dataSource;
	}

}
