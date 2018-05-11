package org.jresearch.gavka.rest.api;

import java.util.List;

import org.jresearch.gavka.domain.KeyFormat;
import org.jresearch.gavka.domain.MessageFormat;

public interface GavkaMessageService {

	String SRV_ROOT = "/rest"; //$NON-NLS-1$

	/** Service id */
	String SRV_PATH = "/messages"; //$NON-NLS-1$

	/** {@link #get(RequestMessagesParameters)} method id */
	String M_R_GET = "/get"; //$NON-NLS-1$

	/** {@link #topics()} method id */
	String M_R_TOPICS = "/topics"; //$NON-NLS-1$

	/** {@link #keyFormats()} method id */
	String M_R_KEY_FORMATS = "/keyFormats"; //$NON-NLS-1$

	/** {@link #messageFormats()} method id */
	String M_R_MESSAGE_FORMATS = "/messageFormats"; //$NON-NLS-1$

	MessagePortion get(RequestMessagesParameters parameters);

	List<String> topics();

	List<KeyFormat> keyFormats();

	List<MessageFormat> messageFormats();

}
