package conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SpringConfig {

	@SuppressWarnings("static-method")
	@Bean
	public YamlMapper yamlMapper() {
		return new YamlMapper();
	}

}