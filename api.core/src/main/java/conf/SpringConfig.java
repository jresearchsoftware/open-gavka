package conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableScheduling
public class SpringConfig {

	@SuppressWarnings("static-method")
	@Bean
	public ObjectMapper yamlMapper() {
		return new ObjectMapper(new YAMLFactory())
				.registerModule(new JavaTimeModule())
				.registerModule(new Jdk8Module())
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

}