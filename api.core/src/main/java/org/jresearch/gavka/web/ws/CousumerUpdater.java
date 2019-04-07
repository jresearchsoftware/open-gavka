package org.jresearch.gavka.web.ws;

import javax.annotation.PostConstruct;

import org.jresearch.gavka.srv.ConnectionService;
import org.jresearch.gavka.srv.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CousumerUpdater {

	@Autowired
	private MessageService messageService;
	@Autowired
	private ConnectionService connectionService;
	@Autowired
	private ConsumerWebSocketHandler consumerWebSocketHandler;

	@PostConstruct
	private void ini() {
		connectionService.connections();
		// TODO Auto-generated method stub

	}

	public void check() {

	}

}
