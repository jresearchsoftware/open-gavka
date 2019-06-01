package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.dominokit.domino.ui.forms.Select;
import org.dominokit.domino.ui.forms.SelectOption;
import org.dominokit.domino.ui.style.Color;

import com.google.gwt.user.client.Random;

public class MdColorSelect extends Select<String> {

	public MdColorSelect(final String label) { super(label, toSelectOprions(EnumSet.allOf(ColorEnum.class))); }

	private static List<SelectOption<String>> toSelectOprions(final EnumSet<ColorEnum> colors) { return colors.stream().map(ColorEnum::getColor).map(MdColorSelect::toSelectOption).collect(Collectors.toList()); }

	private static SelectOption<String> toSelectOption(final Color color) { return SelectOption.create(color.getName(), color.getName()).setBackground(color); }

	public static Select<String> create(final String label) { return new MdColorSelect(label); }

	public static String any() {
		final ColorEnum[] values = ColorEnum.values();
		return values[Random.nextInt(values.length)].getColor().getName();
	}

}
