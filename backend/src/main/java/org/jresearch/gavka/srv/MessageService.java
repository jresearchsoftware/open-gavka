package org.jresearch.gavka.srv;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.jresearch.gavka.domain.MessageFilter;
import org.jresearch.gavka.rest.api.MessagePortion;
import org.jresearch.gavka.rest.api.PagingParameters;

public interface MessageService {

	MessagePortion getMessages(PagingParameters pagingParameters, MessageFilter messageFilter);

	List<String> getMessageTopics();

	void exportMessages(OutputStream bos, MessageFilter filter) throws IOException;

}
