package org.jresearch.gavka.srv;

import java.time.LocalDate;
import java.util.List;

import org.jresearch.commons.gwt.shared.loader.PageLoadResultBean;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.rest.api.PagingParameters;

public interface MessageService {

	PageLoadResultBean<Message> getMessages(PagingParameters pagingParameters, String topic, LocalDate from, LocalDate to, boolean avro);

	List<String> getMessageTopics();

}
