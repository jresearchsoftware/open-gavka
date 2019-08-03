package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import javax.annotation.Nonnull;

import org.dominokit.domino.ui.chips.Chip;
import org.dominokit.domino.ui.popover.PopupPosition;
import org.dominokit.domino.ui.popover.Tooltip;
import org.dominokit.domino.ui.style.Unit;
import org.dominokit.domino.ui.tag.TagsInput;
import org.jresearch.commons.gwt.client.mvc.event.Bus;

@SuppressWarnings("unchecked")
public class PropertyInput extends TagsInput<Property> {

	@Nonnull
	private final Bus bus;

	public PropertyInput(final String label, @Nonnull final Bus bus) {
		super(label, new PropertyTagsStore());
		this.bus = bus;
	}

	public static PropertyInput create(@Nonnull final String label, @Nonnull final Bus bus) {
		return new PropertyInput(label, bus);
	}

	@SuppressWarnings("boxing")
	@Override
	public void appendChip(final Chip chip, final Property value) {
		if (value != null) {
			chip.addClickListener(e -> bus.fire(new PropertySelectEvent(value)));
		}
		chip.getTextContainer().styler(s -> s.setMaxWidth(Unit.px.of(250)).setProperty("text-overflow", "ellipsis")); //$NON-NLS-1$ //$NON-NLS-2$
		Tooltip.create(chip, value.value()).position(PopupPosition.TOP);
		super.appendChip(chip, value);
	}

}
