package org.jresearch.gavka.gwt.core.client.module.appender.srv;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.jresearch.gavka.domain.LogUiAppender;

public interface LogUiAppenderService extends RestService {

	@GET
	@Path("/rest/appenders")
	void get(MethodCallback<List<LogUiAppender>> callback);

	@GET
	@Path("/rest/appenderTypes")
	void types(MethodCallback<List<String>> callback);

	@POST
	@Path("/rest/appender")
	void set(LogUiAppender loger, MethodCallback<Boolean> callback);

}
