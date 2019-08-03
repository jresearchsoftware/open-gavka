package org.jresearch.gavka.srv;

public class KafkaException extends RuntimeException{

	private static final long serialVersionUID = 799365790285599186L;

	public KafkaException(String message, Throwable cause) {
		super(message, cause);
	}

	
}
