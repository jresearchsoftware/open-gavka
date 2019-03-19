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

	/** {@link #export(MessageParameters)} method id */
	String M_R_EXPORT = "/export"; //$NON-NLS-1$

	/** {@link #topics(String)} method id */
	String M_R_TOPICS = "/topics/{connectionId}"; //$NON-NLS-1$

	/** {@link #keyFormats(String)} method id */
	String M_R_KEY_FORMATS = "/keyFormats/{connectionId}"; //$NON-NLS-1$

	/** {@link #messageFormats(String)} method id */
	String M_R_MESSAGE_FORMATS = "/messageFormats/{connectionId}"; //$NON-NLS-1$

	/** {@link #connections()} method id */
	String M_R_CONNECTIONS = "/connections"; //$NON-NLS-1$

	MessagePortion get(RequestMessagesParameters parameters);

	List<String> topics(String connectionId);

	List<KeyFormat> keyFormats(String connectionId);

	List<MessageFormat> messageFormats(String connectionId);

	List<ConnectionLabel> connections();
}
