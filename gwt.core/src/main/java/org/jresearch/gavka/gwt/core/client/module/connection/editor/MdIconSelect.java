package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import org.dominokit.domino.ui.forms.Select;
import org.dominokit.domino.ui.forms.SelectOption;
import org.dominokit.domino.ui.icons.MdiIcon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class MdIconSelect extends Select<String> {

	public MdIconSelect(String label) {
		super(label, toSelectOprions(MdIcons.VALUES));
	}

	private static List<SelectOption<String>> toSelectOprions(Collection<MdIcons> icons) {
		Builder<SelectOption<String>> builder = ImmutableList.<SelectOption<String>>builder();
		for (MdIcons icon : icons) {
			builder.add(toSelectOption(icon.icon()));
		}
		return builder.build();
//		return icons.stream().map(MdIcons::getIcon).map(MdIconSelect::toSelectOption).collect(Collectors.toList());
	}

	@Nonnull
	private static SelectOption<String> toSelectOption(MdiIcon icon) {
		SelectOption<String> option = SelectOption.create(icon.getName(), icon.getName());
		option.getValueContainer().appendChild(icon);
		return option;
	}

	public static Select<String> create(String label) {
		return new MdIconSelect(label);
	}
}
