package org.jresearch.gavka.domain;

import java.util.List;

public class Connection {

	private String id;
	private String label;
	private String icon = "device_hub";
	private String color = "TEAL LIGHTEN 5";
	private List<String> bootstrapServers;
	private String schemaRegistryUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public List<String> getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(List<String> bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	public String getSchemaRegistryUrl() {
		return schemaRegistryUrl;
	}

	public void setSchemaRegistryUrl(String schemaRegistryUrl) {
		this.schemaRegistryUrl = schemaRegistryUrl;
	}

}
