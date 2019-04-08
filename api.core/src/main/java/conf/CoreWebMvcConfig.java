package conf;

import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SuppressWarnings({ "static-method", "nls" })
public class CoreWebMvcConfig implements WebMvcConfigurer {

	@Override
	public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
		configurer.setDefaultTimeout(-1);
		configurer.setTaskExecutor(asyncTaskExecutor());
	}

	@Bean
	public AsyncTaskExecutor asyncTaskExecutor() {
		return new SimpleAsyncTaskExecutor("gavkaExport");
	}

}
