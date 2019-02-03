package conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
public class WebMvcConfig extends CoreWebMvcConfig {
	// WAR specific
}
