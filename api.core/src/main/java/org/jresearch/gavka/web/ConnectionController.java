package org.jresearch.gavka.web;

import java.util.List;

import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.rest.api.GavkaConnectionService;
import org.jresearch.gavka.srv.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GavkaConnectionService.SRV_PATH)
public class ConnectionController implements GavkaConnectionService {

	@Autowired
	private ConnectionService connectionService;

	@Override
	@GetMapping(M_R_GET)
	public List<Connection> get() {
		return connectionService.connections();
	}

	@PostMapping(M_P_SAVE)
	@Override
	public boolean save(@RequestBody Connection connection) {
		return connectionService.update(connection);
	}

	@PostMapping(M_P_REMOVE)
	@Override
	public boolean remove(@RequestBody String connectionId) {
		return connectionService.remove(connectionId);
	}

}