package org.jresearch.gavka.rest.api;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class RequestMessagesParameters {

	private PagingParameters pagingParameters;
	private MessageParameters messageParameters;

	/**
	 * @return the pagingParameters
	 */
	public PagingParameters getPagingParameters() {
		return pagingParameters;
	}

	/**
	 * @param pagingParameters
	 *            the pagingParameters to set
	 */
	public void setPagingParameters(final PagingParameters pagingParameters) {
		this.pagingParameters = pagingParameters;
	}

	/**
	 * @return the messageParameters
	 */
	public MessageParameters getMessageParameters() {
		return messageParameters;
	}

	/**
	 * @param messageParameters
	 *            the messageParameters to set
	 */
	public void setMessageParameters(final MessageParameters messageParameters) {
		this.messageParameters = messageParameters;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("pagingParameters", pagingParameters)
				.add("messageParameters", messageParameters)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getPagingParameters(), getMessageParameters());
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof RequestMessagesParameters) {
			final RequestMessagesParameters that = (RequestMessagesParameters) object;
			return Objects.equal(this.getPagingParameters(), that.getPagingParameters())
					&& Objects.equal(this.getMessageParameters(), that.getMessageParameters());
		}
		return false;
	}

}
