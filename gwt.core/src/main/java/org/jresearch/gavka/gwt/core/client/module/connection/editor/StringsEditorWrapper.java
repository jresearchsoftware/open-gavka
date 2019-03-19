package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import java.util.List;

import javax.annotation.Nonnull;

import org.dominokit.domino.ui.forms.TextBox;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.gwt.editor.client.LeafValueEditor;

public class StringsEditorWrapper implements LeafValueEditor<List<String>> {

	private static final char SEPARATOR = ',';
	private static final Splitter OMIT_EMPTY_STRINGS = Splitter.on(SEPARATOR).omitEmptyStrings();

	private final TextBox toWrap;

	public StringsEditorWrapper(@Nonnull final TextBox toWrap) {
		this.toWrap = toWrap;
	}

	@Override
	public List<String> getValue() {
		final String values = toWrap.getValue();
		return values == null ? ImmutableList.of() : OMIT_EMPTY_STRINGS.splitToList(values);
	}

	@Override
	public void setValue(final List<String> value) {
		toWrap.setValue(value == null ? null : String.join(String.valueOf(SEPARATOR), value));
	}

}
