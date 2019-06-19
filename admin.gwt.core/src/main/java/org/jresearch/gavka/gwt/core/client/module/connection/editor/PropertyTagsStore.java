package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import java.util.Collections;
import java.util.Map;

import org.dominokit.domino.ui.tag.store.LocalTagsStore;
import org.jresearch.gavka.gwt.core.client.resource.GavkaRs;

public class PropertyTagsStore extends LocalTagsStore<Property> {

	@Override
	public String getDisplayValue(final Property value) {
		return GavkaRs.FMT.property(value.key(), value.value());
	}

	@Override
	public Map<String, Property> filter(final String searchValue) {
		return Collections.emptyMap();
	}

}
