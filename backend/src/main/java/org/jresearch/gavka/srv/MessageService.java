package org.jresearch.gavka.srv;

import java.time.LocalDate;
import java.util.List;

import org.jresearch.gavka.rest.api.MessagePortion;
import org.jresearch.gavka.rest.api.PagingParameters;

public interface MessageService {

	MessagePortion getMessages(PagingParameters pagingParameters, String topic, LocalDate from, LocalDate to, boolean avro);

	List<String> getMessageTopics();

}
