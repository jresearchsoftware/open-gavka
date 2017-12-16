package org.jresearch.gavka.gwt.core.client.module.message.srv;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.jresearch.gavka.domain.LogUiLogger;

public interface LogUiLoggerService extends RestService {

	@GET
	@Path("/rest/loggers/{inherited}/{filter}")
	void get(@PathParam("filter") String filter, @PathParam("inherited") boolean inherited, MethodCallback<List<LogUiLogger>> callback);

	@POST
	@Path("/rest/logger")
	void set(LogUiLogger loger, MethodCallback<Boolean> callback);

	@GET
	@Path("/rest/suggestLogger/{query}/{limit}")
	void requestSuggestion(@PathParam("query") String query, @PathParam("limit") int limit, MethodCallback<List<String>> callback);

}
