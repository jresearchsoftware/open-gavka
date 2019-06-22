package org.jresearch.gavka.gwt.core.client.module.message.widget;

import java.util.Stack;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.REST;
import org.jresearch.commons.gwt.client.mvc.AbstractMethodCallback;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.module.message.srv.GavkaMessageRestService;
import org.jresearch.gavka.rest.api.MessageParameters;
import org.jresearch.gavka.rest.api.MessagePortion;
import org.jresearch.gavka.rest.api.PagingParameters;
import org.jresearch.gavka.rest.api.RequestMessagesParameters;

import com.google.common.collect.ImmutableList;

public class MessageDataSource {

	@Nonnull
	private final GavkaMessageRestService srv;
	@Nonnull
	private final Bus bus;

	private final int currentAmount = 100;
	@Nonnull
	private final Stack<PagingParameters> pages;
	private boolean reload = false;
	private boolean loading = false;
	private MessageParameters currentPrameters = null;

	@Inject
	public MessageDataSource(@Nonnull final GavkaMessageRestService srv, @Nonnull final Bus bus) {
		this.srv = srv;
		this.bus = bus;
		pages = new Stack<>();
		initPages();
	}

	public void load(final MessageParameters messageParameters, final MessageDataLoadCallback callback) {
		if (!loading) {
			reload = false;
			loading = true;
			if (!messageParameters.equals(currentPrameters)) {
				initPages();
				currentPrameters = messageParameters;
			}
			final RequestMessagesParameters parameters = new RequestMessagesParameters();
			parameters.setMessageParameters(messageParameters);
			parameters.setPagingParameters(pages.peek());
			REST.withCallback(new AbstractMethodCallback<MessagePortion>(bus) {
				@Override
				public void onSuccess(final Method method, final MessagePortion result) {
					loading = false;
					if (result != null) {
						final PagingParameters pagingParameters = new PagingParameters();
						pagingParameters.setAmount(currentAmount);
						pagingParameters.setPartitionOffsets(result.getPartitionOffsets());
						pages.push(pagingParameters);
						callback.onLoad(result.getMessages());
					} else {
						callback.onLoad(ImmutableList.of());
					}
				}

				@Override
				public void onFailure(final Method method, final Throwable caught) {
					loading = false;
					callback.onLoad(ImmutableList.of());
					super.onFailure(method, caught);
				}
			}).call(srv).get(parameters);
		}
	}

	public boolean isPreviousePartExist() { return pages.size() > 2; }

	private void initPages() {
		pages.clear();
		pages.add(new PagingParameters(currentAmount, ImmutableList.of()));
	}

	public void reset() {
		if (pages.size() != 1) {
			reload = true;
			initPages();
		}
	}

	public void next() {
		reload = true;
		pages.peek().setAmount(currentAmount);
	}

	public void prev() {
		reload = true;
		pages.pop();
		pages.pop();
	}

	/** Check the parameters and return <code>true</code> if data need to reload */
	public boolean isReloadNeed(@Nonnull final MessageParameters parameters) {
		return reload || !parameters.equals(currentPrameters);
	}

}
