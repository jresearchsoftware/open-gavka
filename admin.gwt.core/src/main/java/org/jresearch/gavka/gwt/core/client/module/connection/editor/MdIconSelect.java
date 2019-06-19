package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.dominokit.domino.ui.badges.Badge;
import org.dominokit.domino.ui.forms.Select;
import org.dominokit.domino.ui.forms.SelectOption;
import org.dominokit.domino.ui.forms.SelectOptionGroup;
import org.dominokit.domino.ui.icons.MdiByTagFactory;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.icons.MdiTags;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.gwt.user.client.Random;

public class MdIconSelect extends Select<String> {

	public MdIconSelect(final String label) {
		super(label);
		toOptions().forEach(this::addGroup);
	}

	private static List<SelectOptionGroup<String>> toOptions() {
		final Builder<SelectOptionGroup<String>> builder = ImmutableList.<SelectOptionGroup<String>>builder();
		for (final String tag : MdiTags.TAGS) {
			final SelectOptionGroup<String> group = toSelectGroup(tag);
			toSelectOptions(MdiByTagFactory.get(tag)).forEach(group::appendChild);
			builder.add(group);
		}
		return builder.build();
	}

	@SuppressWarnings("null")
	@Nonnull
	private static List<SelectOption<String>> toSelectOptions(final List<Supplier<MdiIcon>> icons) {
		final Builder<SelectOption<String>> builder = ImmutableList.<SelectOption<String>>builder();
		for (final Supplier<MdiIcon> icon : icons) {
			builder.add(toSelectOption(icon.get()));
		}
		return builder.build();
	}

	@Nonnull
	private static SelectOptionGroup<String> toSelectGroup(final String tag) {
		return SelectOptionGroup.<String>create(Badge.create(tag).setBackground(MdColorSelect.anyColor()));
	}

	@Nonnull
	private static SelectOption<String> toSelectOption(final MdiIcon icon) {
		final SelectOption<String> option = SelectOption.create(icon.getName(), icon.getName());
		option.getValueContainer().appendChild(icon);
		return option;
	}

	public static Select<String> create(final String label) {
		return new MdIconSelect(label);
	}

	public static String any() {
		final List<String> tags = MdiTags.TAGS;
		final String anyTag = tags.get(Random.nextInt(tags.size()));
		final List<Supplier<MdiIcon>> icons = MdiByTagFactory.get(anyTag);
		return icons.get(Random.nextInt(icons.size())).get().getName();
	}

}
