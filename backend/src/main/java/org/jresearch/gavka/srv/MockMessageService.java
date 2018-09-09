package org.jresearch.gavka.srv;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.domain.MessageFilter;
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
	public MessagePortion getMessages(final PagingParameters pagingParameters, final MessageFilter filter) {
		final List<Message> messages = Messages.getMessages();
		return new MessagePortion(ImmutableList.of(), messages);
	}

	@Override
	public List<String> getMessageTopics() {
		return Messages.getTopics();
	}

	@Override
	public void exportMessages(final OutputStream bos, final MessageFilter filter) throws IOException {
		final List<Message> messages = Messages.getMessages();
		for (final Message message : messages) {
			bos.write(message.toString().getBytes());
		}
	}

}
