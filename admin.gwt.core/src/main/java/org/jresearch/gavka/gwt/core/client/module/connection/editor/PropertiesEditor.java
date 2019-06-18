package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.dominokit.domino.ui.tag.TagsInput;

import com.google.gwt.editor.client.LeafValueEditor;

public class PropertiesEditor implements LeafValueEditor<Map<String, String>> {

	private final TagsInput<Entry<String, String>> input;

	public PropertiesEditor(final TagsInput<Map.Entry<String, String>> input) {
		this.input = input;
	}

	@Override
	public Map<String, String> getValue() { return input.getValue().stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue)); }

	@Override
	public void setValue(final Map<String, String> value) {
		input.setValue(new ArrayList<>(value.entrySet()));
	}

}
