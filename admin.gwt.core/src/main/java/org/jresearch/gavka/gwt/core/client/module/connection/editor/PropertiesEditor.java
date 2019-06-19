package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dominokit.domino.ui.tag.TagsInput;

import com.google.gwt.editor.client.LeafValueEditor;

public class PropertiesEditor implements LeafValueEditor<Map<String, String>> {

	private final TagsInput<Property> input;

	public PropertiesEditor(final TagsInput<Property> input) {
		this.input = input;
	}

	@Override
	public Map<String, String> getValue() { return input.getValue().stream().collect(Collectors.toMap(Property::key, Property::value)); }

	@Override
	public void setValue(final Map<String, String> value) {
		final List<Property> values = value.entrySet().stream().map(e -> PropertyTuple.of(e.getKey(), e.getValue())).collect(Collectors.toList());
		input.setValue(values);
	}

}
