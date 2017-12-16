package org.jresearch.gavka.gwt.core.client.module.message;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.mvc.AbstractView;
import org.jresearch.gavka.gwt.core.client.module.message.widget.MessagePage;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MessageView extends AbstractView<MessageController> {

	@Nonnull
	private final MessagePage page;

	@Inject
	public MessageView(@Nonnull final MessagePage page, @Nonnull final MessageController controller) {
		super(controller);
		this.page = page;
	}

	@Override
	public Widget getContent() {
		return page;
	}

}
