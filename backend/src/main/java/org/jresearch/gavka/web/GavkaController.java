package org.jresearch.gavka.web;

import java.time.LocalDate;
import java.util.List;

import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.tool.Messages;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GavkaController {

	@GetMapping("/messages/{topic}/{date}")
	public List<Message> getMessages(@PathVariable final String topic, @PathVariable @DateTimeFormat(pattern = "yyyy-M-d") final LocalDate date) {
		return Messages.getMessages(topic, date);
	}

	@GetMapping("/messages/{topic}")
	public List<Message> getMessages(@PathVariable final String topic) {
		return getMessages(topic, LocalDate.now());
	}

}
