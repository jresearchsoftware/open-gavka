package org.jresearch.gavka.rest.api;

public class ConnectionLabel {

	private String id;
	private String label;

	public ConnectionLabel() {

		// For JSON
	}

	public ConnectionLabel(final String id, final String label) {
		this.id = id;
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

}
