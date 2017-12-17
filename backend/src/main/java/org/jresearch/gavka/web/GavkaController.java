package org.jresearch.gavka.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.tool.Messages;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GavkaController {

	@GetMapping("/messages/{topic}/{date}")
	public List<Message> getMessages(@PathVariable final String topic, @PathVariable @DateTimeFormat(iso = ISO.DATE) final Optional<LocalDate> date) {
		return Messages.getMessages(topic, date.orElse(LocalDate.now()));
	}

}
