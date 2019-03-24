package org.jresearch.gavka.web;

import java.time.LocalDateTime;
import java.util.Optional;

import org.jresearch.gavka.domain.KeyFormat;
import org.jresearch.gavka.domain.MessageFilter;
import org.jresearch.gavka.domain.MessageFormat;
import org.jresearch.gavka.rest.api.GavkaMessageService;
import org.jresearch.gavka.srv.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Controller
@RequestMapping(GavkaMessageService.SRV_PATH)
public class ExportController {

	@Autowired
	private MessageService messageService;

	@SuppressWarnings("nls")
	@PostMapping(GavkaMessageService.M_R_EXPORT)
	public ResponseEntity<StreamingResponseBody> export(@RequestParam final String connectionId, @RequestParam final Optional<String> topic, @RequestParam final Optional<String> key, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") final Optional<LocalDateTime> from, @RequestParam final Optional<KeyFormat> keyFormat, @RequestParam final Optional<MessageFormat> messageFormat) {
		final MessageFilter filter = toMessageFilter(topic, key, from, keyFormat, messageFormat);
		final ContentDisposition disposition = ContentDisposition.builder("attachment").filename("gavka.export").build();
		return ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
				.body(out -> messageService.exportMessages(connectionId, out, filter));
	}

	private static MessageFilter toMessageFilter(final Optional<String> topic, final Optional<String> key, final Optional<LocalDateTime> from, final Optional<KeyFormat> keyFormat, final Optional<MessageFormat> messageFormat) {
		final MessageFilter result = new MessageFilter();
		from.ifPresent(result::setFrom);
		key.ifPresent(result::setKey);
		keyFormat.ifPresent(result::setKeyFormat);
		messageFormat.ifPresent(result::setMessageFormat);
		topic.ifPresent(result::setTopic);
		return result;

	}

}
