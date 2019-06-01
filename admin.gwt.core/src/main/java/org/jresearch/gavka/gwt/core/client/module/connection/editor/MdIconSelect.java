package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import org.dominokit.domino.ui.forms.Select;
import org.dominokit.domino.ui.forms.SelectOption;
import org.dominokit.domino.ui.icons.MdiIcon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.gwt.user.client.Random;

public class MdIconSelect extends Select<String> {

	public MdIconSelect(final String label) { super(label, toSelectOprions(MdIcons.VALUES)); }

	private static List<SelectOption<String>> toSelectOprions(final Collection<MdIcons> icons) {
		final Builder<SelectOption<String>> builder = ImmutableList.<SelectOption<String>>builder();
		for (final MdIcons icon : icons) {
			builder.add(toSelectOption(icon.icon()));
		}
		return builder.build();
//		return icons.stream().map(MdIcons::getIcon).map(MdIconSelect::toSelectOption).collect(Collectors.toList());
	}

	@Nonnull
	private static SelectOption<String> toSelectOption(final MdiIcon icon) {
		final SelectOption<String> option = SelectOption.create(icon.getName(), icon.getName());
		option.getValueContainer().appendChild(icon);
		return option;
	}

	public static Select<String> create(final String label) { return new MdIconSelect(label); }

	public static String any() {
		final List<MdIcons> values = MdIcons.VALUES;
		return values.get(Random.nextInt(values.size())).getIconName();
	}

}
