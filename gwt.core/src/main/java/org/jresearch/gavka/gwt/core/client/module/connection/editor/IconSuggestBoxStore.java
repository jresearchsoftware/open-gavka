package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import java.util.EnumSet;

import javax.inject.Inject;

import org.dominokit.domino.ui.forms.LocalSuggestBoxStore;
import org.dominokit.domino.ui.forms.SuggestItem;

public class IconSuggestBoxStore extends LocalSuggestBoxStore {

	@Inject
	public IconSuggestBoxStore() {
		EnumSet.allOf(MdiIconEnum.class)
				.stream()
				.map(IconSuggestBoxStore::toItem)
				.forEach(this::addSuggestion);
	}

	private static SuggestItem toItem(MdiIconEnum icon) {
		return new SuggestItem(icon.getIcon().getName(), icon.getIcon());
	}

}
