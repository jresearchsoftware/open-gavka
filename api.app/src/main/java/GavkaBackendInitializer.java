import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class GavkaBackendInitializer implements WebApplicationInitializer {

	@SuppressWarnings({ "resource", "nls" })
	@Override
	public void onStartup(final ServletContext container) {
		final AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation("conf");

		container.addListener(new ContextLoaderListener(context));

		final DispatcherServlet servlet = new DispatcherServlet(context);
		servlet.setEnableLoggingRequestDetails(true);

		final ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", servlet);
		dispatcher.setAsyncSupported(true);

		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/rest/*", "/ws/*");
	}
}
