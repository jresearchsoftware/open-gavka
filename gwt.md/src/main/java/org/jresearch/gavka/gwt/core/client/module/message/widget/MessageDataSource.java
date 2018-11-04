package org.jresearch.gavka.gwt.core.client.module.message.widget;

import java.util.Stack;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

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

@Singleton
public class MessageDataSource {

	@Nonnull
	private final GavkaMessageRestService srv;
	@Nonnull
	private final Bus bus;

	private final int currentAmount = 100;
	@Nonnull
	private final Stack<PagingParameters> pages;

	@Inject
	public MessageDataSource(@Nonnull final GavkaMessageRestService srv, @Nonnull final Bus bus) {
		this.srv = srv;
		this.bus = bus;
		pages = new Stack<>();
		reset();
	}

	public void load(final MessageParameters messageParameters, final MessageDataLoadCallback callback) {
		final RequestMessagesParameters parameters = new RequestMessagesParameters();
		parameters.setMessageParameters(messageParameters);
		parameters.setPagingParameters(pages.peek());
		REST.withCallback(new AbstractMethodCallback<MessagePortion>(bus) {
			@Override
			public void onSuccess(final Method method, final MessagePortion result) {
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
				callback.onLoad(ImmutableList.of());
				super.onFailure(method, caught);
			}
		}).call(srv).get(parameters);
	}

	public boolean isPreviousePartExist() {
		return pages.size() > 2;
	}

	public void reset() {
		pages.clear();
		pages.add(new PagingParameters(currentAmount, ImmutableList.of()));
	}

	public void next() {
		pages.peek().setAmount(currentAmount);
	}

	public void prev() {
		pages.pop();
		pages.pop();
	}

}
