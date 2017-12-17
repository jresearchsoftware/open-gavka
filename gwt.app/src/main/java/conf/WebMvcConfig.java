package conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	// @Bean
	// RequestMappingHandlerMapping restHandlerMapping() {
	// final RequestMappingHandlerMapping result = new
	// RequestMappingHandlerMapping();
	// result.setDetectHandlerMethodsInAncestorContexts(true);
	// return result;
	// }

}
