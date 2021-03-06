package org.jresearch.gavka.gwt.core.client.module.message;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.INotificator;
import org.jresearch.gavka.gwt.core.client.module.GafkaFactory;
import org.jresearch.gavka.gwt.core.client.module.message.widget.MessageDataSource;
import org.jresearch.gavka.rest.data.GafkaCoordinates;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class MessageViewFactory implements GafkaFactory<MessageView> {

	private final Provider<INotificator> notificator;
	private final Provider<MessageDataSource> messageDataSource;

	@Inject
	public MessageViewFactory(@Nonnull final Provider<INotificator> notificator, @Nonnull final Provider<MessageDataSource> messageDataSource) {
		this.notificator = notificator;
		this.messageDataSource = messageDataSource;
	}

	@SuppressWarnings("null")
	@Override
	public MessageView create(@Nonnull final GafkaCoordinates coordinates) {
		return new MessageView(notificator.get(), messageDataSource.get(), coordinates);
	}

}
