package org.jresearch.gavka.srv;

import java.time.LocalDate;
import java.util.List;

import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.rest.api.MessagePortion;
import org.jresearch.gavka.rest.api.PagingParameters;
import org.jresearch.gavka.tool.Messages;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

@Profile("nokafka")
@Component
public class MockMessageService extends AbstractMessageService {

	@Override
	public MessagePortion getMessages(final PagingParameters pagingParameters, final String topic, final LocalDate from, final LocalDate to, final boolean avro) {
		final List<Message> messages = Messages.getMessages(topic, from, to, avro);
		return new MessagePortion(ImmutableList.of(), messages);
	}

	@Override
	public List<String> getMessageTopics() {
		return Messages.getTopics();
	}

}
