package org.jresearch.gavka.web;

import java.util.List;

import org.jresearch.commons.gwt.server.rest.BaseSpringController;
import org.jresearch.commons.gwt.server.tool.ServerDates;
import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.domain.ImmutableMessageFilter;
import org.jresearch.gavka.domain.KeyFormat;
import org.jresearch.gavka.domain.MessageFilter;
import org.jresearch.gavka.domain.MessageFormat;
import org.jresearch.gavka.rest.api.ConnectionLabel;
import org.jresearch.gavka.rest.api.GavkaMessageService;
import org.jresearch.gavka.rest.api.ImmutableConnectionLabel;
import org.jresearch.gavka.rest.api.MessageParameters;
import org.jresearch.gavka.rest.api.MessagePortion;
import org.jresearch.gavka.rest.api.PagingParameters;
import org.jresearch.gavka.rest.api.RequestMessagesParameters;
import org.jresearch.gavka.srv.ConnectionService;
import org.jresearch.gavka.srv.MessageRetrievalException;
import org.jresearch.gavka.srv.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableList;

import one.util.streamex.StreamEx;

@RestController
@RequestMapping(GavkaMessageService.SRV_PATH)
public class GavkaController extends BaseSpringController implements GavkaMessageService {

	@Autowired
	private ConnectionService connectionService;
	@Autowired
	private MessageService messageService;

	@Override
	@PostMapping(M_R_GET)
	public MessagePortion get(@RequestBody final RequestMessagesParameters parameters) {
		try {
			final MessageParameters messageParameters = parameters.getMessageParameters();
			final PagingParameters pagingParameters = parameters.getPagingParameters();
			return messageService.getMessages(messageParameters.connection(), pagingParameters, toMessageFilter(messageParameters));
		} catch (final MessageRetrievalException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("null")
	private static MessageFilter toMessageFilter(final MessageParameters parameters) {
		return ImmutableMessageFilter
				.builder()
				.from(parameters.from().map(f -> ServerDates.localDateTime(f.getDate(), f.getTime())))
				.key(parameters.key())
				.keyFormat(parameters.keyFormat())
				.messageFormat(parameters.messageFormat())
				.topic(parameters.topic())
				.valuePattern(parameters.valuePattern())
				.build();
	}

	@Override
	@GetMapping(M_R_TOPICS)
	public List<String> topics(@PathVariable final String connectionId) {
		return messageService.getMessageTopics(connectionId);
	}

	@Override
	@GetMapping(M_R_KEY_FORMATS)
	public List<KeyFormat> keyFormats(@PathVariable final String connectionId) {
		return ImmutableList.copyOf(KeyFormat.values());
	}

	@Override
	@GetMapping(M_R_MESSAGE_FORMATS)
	public List<MessageFormat> messageFormats(@PathVariable final String connectionId) {
		return ImmutableList.copyOf(MessageFormat.values());
	}

	@Override
	@GetMapping(M_R_CONNECTIONS)
	public List<ConnectionLabel> connections() {
		return StreamEx.of(connectionService.connections()).map(GavkaController::map).toList();
	}

	private static ConnectionLabel map(final Connection conn) {
		return new ImmutableConnectionLabel.Builder().id(conn.getId()).label(conn.getLabel()).build();

	}

}
