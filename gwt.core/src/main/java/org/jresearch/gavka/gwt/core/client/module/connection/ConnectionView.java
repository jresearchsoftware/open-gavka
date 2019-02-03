package org.jresearch.gavka.gwt.core.client.module.connection;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.dominokit.domino.ui.cards.Card;
import org.jresearch.commons.gwt.client.mvc.AbstractView;
import org.jresearch.gavka.gwt.core.client.module.connection.srv.GavkaConnectionRestService;

import elemental2.dom.HTMLElement;

@Singleton
public class ConnectionView extends AbstractView<ConnectionController> {

	@Nonnull
	private final Card mainCard;
	private GavkaConnectionRestService gavkaConnectionRestService;

	@SuppressWarnings("null")
	@Inject
	public ConnectionView(@Nonnull final ConnectionController controller, @Nonnull final GavkaConnectionRestService gavkaConnectionRestService) {
		super(controller);
		this.gavkaConnectionRestService = gavkaConnectionRestService;
		mainCard = Card.create();
	}

	@SuppressWarnings("null")
	@Override
	public HTMLElement getContent() {
		return mainCard.asElement();
	}

}
