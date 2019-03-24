package org.jresearch.gavka.gwt.core.client.module.connection.srv;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.fusesource.restygwt.client.DirectRestService;
import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.rest.api.GavkaConnectionService;

public interface GavkaConnectionRestService extends GavkaConnectionService, DirectRestService {

	@GET
	@Path(SRV_ROOT + SRV_PATH + M_R_GET)
	@Override
	List<Connection> get();

	@POST
	@Path(SRV_ROOT + SRV_PATH + M_P_SAVE)
	@Override
	boolean save(Connection connection);

	@POST
	@Path(SRV_ROOT + SRV_PATH + M_P_REMOVE)
	@Override
	boolean remove(String connectionId);

}
