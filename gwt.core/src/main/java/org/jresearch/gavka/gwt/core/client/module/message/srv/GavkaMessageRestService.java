package org.jresearch.gavka.gwt.core.client.module.message.srv;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.fusesource.restygwt.client.DirectRestService;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.rest.api.GavkaMessageService;
import org.jresearch.gavka.rest.api.RequestMessagesParameters;

public interface GavkaMessageRestService extends GavkaMessageService, DirectRestService {

	@POST
	@Path(SRV_ROOT + SRV_PATH + M_R_GET)
	@Override
	List<Message> get(RequestMessagesParameters parameters);

	@GET
	@Path(SRV_ROOT + SRV_PATH + M_R_TOPICS)
	@Override
	List<String> topics();

}
