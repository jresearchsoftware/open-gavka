package org.jresearch.gavka.rest.api;

import org.jresearch.gavka.rest.data.TopicRestInfo;

public interface GavkaConsumerService extends GavkaBaseService {

	/** Service id */
	String SRV_PATH = "/consumer"; //$NON-NLS-1$

	/** {@link #get(String, String)} method id */
	String M_R_GET = "/get/{connectionId}/{topic}"; //$NON-NLS-1$

	TopicRestInfo get(String connectionId, String topic);

}
