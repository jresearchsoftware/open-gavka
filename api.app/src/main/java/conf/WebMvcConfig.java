package conf;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@SuppressWarnings({ "static-method", "nls" })
public class WebMvcConfig extends CoreWebMvcConfig {
	// WAR specific
	@Bean
	public JndiObjectFactoryBean dataSource() {
		final JndiObjectFactoryBean ds = new JndiObjectFactoryBean();
		ds.setJndiName("java:comp/env/jdbc/gavkaDS");
		return ds;
	}

	@Bean
	public DSLContext dslContext(final DataSource dataSource) { return DSL.using(dataSource, SQLDialect.POSTGRES); }
}
