package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.dominokit.domino.ui.forms.Select;
import org.dominokit.domino.ui.forms.SelectOption;
import org.dominokit.domino.ui.icons.MdiIcon;

public class MdIconSelect extends Select<String> {

	public MdIconSelect(String label) {
		super(label, toSelectOprions(EnumSet.allOf(MdIconEnum.class)));
	}

	private static List<SelectOption<String>> toSelectOprions(EnumSet<MdIconEnum> colors) {
		return colors.stream().map(MdIconEnum::getIcon).map(MdIconSelect::toSelectOption).collect(Collectors.toList());
	}

	private static SelectOption<String> toSelectOption(MdiIcon icon) {
		SelectOption<String> option = SelectOption.create(icon.getName(), icon.getName());
		option.getValueContainer().appendChild(icon);
		return option;
	}

	public static Select<String> create(String label) {
		return new MdIconSelect(label);
	}
}
