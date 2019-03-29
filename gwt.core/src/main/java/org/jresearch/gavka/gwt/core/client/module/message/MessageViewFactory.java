package org.jresearch.gavka.gwt.core.client.module.message;

import com.google.inject.assistedinject.Assisted;

public interface MessageViewFactory {

	MessageView create(@Assisted("connectionId") String connectionId, @Assisted("topic") String topic);

}
