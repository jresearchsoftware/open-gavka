package org.jresearch.gavka.gwt.core.client.module.message.widget;

import java.util.List;

import org.jresearch.gavka.domain.Message;

@FunctionalInterface
public interface MessageDataLoadCallback {

	void onLoad(List<Message> messages);

}
