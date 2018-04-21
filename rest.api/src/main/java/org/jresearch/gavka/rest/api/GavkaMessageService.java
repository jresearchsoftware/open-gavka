package org.jresearch.gavka.rest.api;

import java.util.List;

import org.jresearch.gavka.domain.Message;

public interface GavkaMessageService {

	String SRV_ROOT = "/rest"; //$NON-NLS-1$

	/** Service id */
	String SRV_PATH = "/messages"; //$NON-NLS-1$

	/** {@link #get(RequestMessagesParameters)} method id */
	String M_R_GET = "/get"; //$NON-NLS-1$

	/** {@link #topics()} method id */
	String M_R_TOPICS = "/topics"; //$NON-NLS-1$

	List<Message> get(RequestMessagesParameters parameters);

	List<String> topics();

}
