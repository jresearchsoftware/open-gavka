package org.jresearch.gavka.gwt.core.client.module.consumer.srv;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.DirectRestService;
import org.jresearch.gavka.rest.api.GavkaConsumerService;
import org.jresearch.gavka.rest.data.TopicRestInfo;

public interface GavkaConsumerRestService extends GavkaConsumerService, DirectRestService {

	@GET
	@Path(SRV_ROOT + SRV_PATH + M_R_GET)
	@Override
	TopicRestInfo get(@PathParam("connectionId") String connectionId, @PathParam("topic") String topic);

}
