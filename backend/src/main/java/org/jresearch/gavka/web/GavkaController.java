package org.jresearch.gavka.web;

import java.util.List;

import org.jresearch.commons.gwt.server.tool.ServerDates;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.rest.api.GavkaMessageService;
import org.jresearch.gavka.rest.api.MessageParameters;
import org.jresearch.gavka.rest.api.RequestMessagesParameters;
import org.jresearch.gavka.tool.Messages;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GavkaMessageService.SRV_PATH)
public class GavkaController implements GavkaMessageService {

	@Override
	@PostMapping(M_R_GET)
	public List<Message> get(@RequestBody final RequestMessagesParameters parameters) {
		final MessageParameters messageParameters = parameters.getMessageParameters();
		return Messages.getMessages(messageParameters.getTopic(), ServerDates.localDate(messageParameters.getFrom()));
	}

	@Override
	@GetMapping(M_R_TOPICS)
	public List<String> topics() {
		return Messages.getTopics();
	}

}
