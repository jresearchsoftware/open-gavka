package org.jresearch.gavka.web;

import java.time.LocalDateTime;
import java.util.Optional;

import org.jresearch.commons.gwt.server.rest.BaseSpringController;
import org.jresearch.gavka.domain.ImmutableMessageFilter;
import org.jresearch.gavka.domain.KeyFormat;
import org.jresearch.gavka.domain.MessageFilter;
import org.jresearch.gavka.domain.MessageFormat;
import org.jresearch.gavka.rest.api.GavkaMessageService;
import org.jresearch.gavka.srv.MessageRetrievalException;
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
public class ExportController extends BaseSpringController {

	public static final String DISPOSITION = ContentDisposition.builder("attachment").filename("gavka.export").build().toString();

	@Autowired
	private MessageService messageService;

	@SuppressWarnings("nls")
	@PostMapping(GavkaMessageService.M_R_EXPORT)
	public ResponseEntity<StreamingResponseBody> export(@RequestParam final String connectionId, @RequestParam final Optional<String> valuePattern, @RequestParam final Optional<String> topic, @RequestParam final Optional<String> key, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") final Optional<LocalDateTime> from, @RequestParam final Optional<KeyFormat> keyFormat, @RequestParam final Optional<MessageFormat> messageFormat) {
		final MessageFilter filter = toMessageFilter(valuePattern, topic, key, from, keyFormat, messageFormat);
		return ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, DISPOSITION)
				.body(out -> {
					try {
						messageService.exportMessages(connectionId, out, filter);
					} catch (final MessageRetrievalException e) {
						throw new RuntimeException(e);
					}
				});
	}

	@SuppressWarnings({ "null", "nls" })
	private static MessageFilter toMessageFilter(final Optional<String> valuePattern, final Optional<String> topic, final Optional<String> key, final Optional<LocalDateTime> from, final Optional<KeyFormat> keyFormat, final Optional<MessageFormat> messageFormat) {
		return ImmutableMessageFilter
				.builder()
				.from(from)
				.key(key.orElse(""))
				.keyFormat(keyFormat.orElse(KeyFormat.values()[0]))
				.messageFormat(messageFormat.orElse(MessageFormat.values()[0]))
				.topic(topic.orElse(""))
				.valuePattern(valuePattern.orElse(""))
				.build();
	}

}
