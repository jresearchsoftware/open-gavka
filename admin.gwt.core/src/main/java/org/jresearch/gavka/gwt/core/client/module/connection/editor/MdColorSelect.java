package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.dominokit.domino.ui.forms.Select;
import org.dominokit.domino.ui.forms.SelectOption;
import org.dominokit.domino.ui.style.Color;

public class MdColorSelect extends Select<String> {

	public MdColorSelect(String label) {
		super(label, toSelectOprions(EnumSet.allOf(ColorEnum.class)));
	}

	private static List<SelectOption<String>> toSelectOprions(EnumSet<ColorEnum> colors) {
		return colors.stream().map(ColorEnum::getColor).map(MdColorSelect::toSelectOption).collect(Collectors.toList());
	}

	private static SelectOption<String> toSelectOption(Color color) {
		return SelectOption.create(color.getName(), color.getName()).setBackground(color);
	}

	public static Select<String> create(String label) {
		return new MdColorSelect(label);
	}
}
