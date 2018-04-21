package org.jresearch.gavka.srv;

import java.time.LocalDate;
import java.util.List;

import org.jresearch.commons.gwt.shared.loader.PageLoadResultBean;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.rest.api.PagingParameters;
import org.jresearch.gavka.tool.Messages;
import org.springframework.stereotype.Component;

@Component
public class MessageService {

	@SuppressWarnings({ "static-method", "null" })
	public PageLoadResultBean<Message> getMessages(final PagingParameters pagingParameters, final String topic, final LocalDate from, final LocalDate to, final boolean avro) {
		final List<Message> messages = Messages.getMessages(topic, from, to, avro);
		final int offset = pagingParameters.getOffset();
		return new PageLoadResultBean<>(offset, messages.size(), messages.subList(offset, offset + pagingParameters.getAmount()));
	}

	@SuppressWarnings("static-method")
	public List<String> getMessageTopics() {
		return Messages.getTopics();
	}

}
