package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.dominokit.domino.ui.tag.store.LocalTagsStore;
import org.jresearch.gavka.gwt.core.client.resource.GavkaRs;

public class PropertyTagsStore extends LocalTagsStore<Entry<String, String>> {

	@Override
	public String getDisplayValue(final Entry<String, String> value) {
		return GavkaRs.FMT.property(value.getKey(), value.getValue());
	}

	@Override
	public Map<String, Entry<String, String>> filter(final String searchValue) {
		return Collections.emptyMap();
	}
}
