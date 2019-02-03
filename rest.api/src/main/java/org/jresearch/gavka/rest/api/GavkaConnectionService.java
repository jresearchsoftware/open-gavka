package org.jresearch.gavka.rest.api;

import java.util.List;

import org.jresearch.gavka.domain.Connection;

public interface GavkaConnectionService {

	String SRV_ROOT = "/rest"; //$NON-NLS-1$

	/** Service id */
	String SRV_PATH = "/connection"; //$NON-NLS-1$

	/** {@link #get()} method id */
	String M_R_GET = "/get"; //$NON-NLS-1$

	List<Connection> get();

}
