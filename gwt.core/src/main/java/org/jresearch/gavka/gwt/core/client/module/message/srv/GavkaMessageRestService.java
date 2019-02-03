package org.jresearch.gavka.gwt.core.client.module.message.srv;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.fusesource.restygwt.client.DirectRestService;
import org.jresearch.gavka.domain.KeyFormat;
import org.jresearch.gavka.domain.MessageFormat;
import org.jresearch.gavka.rest.api.GavkaMessageService;
import org.jresearch.gavka.rest.api.MessagePortion;
import org.jresearch.gavka.rest.api.RequestMessagesParameters;

public interface GavkaMessageRestService extends GavkaMessageService, DirectRestService {

	@POST
	@Path(SRV_ROOT + SRV_PATH + M_R_GET)
	@Override
	MessagePortion get(RequestMessagesParameters parameters);

	@GET
	@Path(SRV_ROOT + SRV_PATH + M_R_TOPICS)
	@Override
	List<String> topics();

	@GET
	@Path(SRV_ROOT + SRV_PATH + M_R_KEY_FORMATS)
	@Override
	List<KeyFormat> keyFormats();

	@GET
	@Path(SRV_ROOT + SRV_PATH + M_R_MESSAGE_FORMATS)
	@Override
	List<MessageFormat> messageFormats();

	@GET
	@Path(SRV_ROOT + SRV_PATH + M_R_CONNECTIONS)
	@Override
	List<String> connections();

}
