package conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;

@Configuration
@EnableScheduling
@SuppressWarnings("static-method")
public class SpringConfig {

	@Bean
	public ForwardedHeaderTransformer forwardedHeaderTransformer() {
		return new ForwardedHeaderTransformer();
	}

}