package org.jresearch.gavka.gwt.core.client.module.message.srv;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.jresearch.gavka.domain.Message;

public interface GavkaMessageService extends RestService {

	@GET
	@Path("/rest/messages/{topic}")
	void get(@PathParam("topic") String topic, MethodCallback<List<Message>> callback);

}
