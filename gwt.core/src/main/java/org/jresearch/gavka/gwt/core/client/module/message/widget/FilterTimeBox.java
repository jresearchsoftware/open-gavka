package org.jresearch.gavka.gwt.core.client.module.message.widget;

import org.dominokit.domino.ui.grid.flex.FlexItem;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.timepicker.ClockStyle;
import org.dominokit.domino.ui.timepicker.TimeBox;

import com.google.gwt.dom.client.Style.Cursor;

import elemental2.dom.Event;
import elemental2.dom.EventListener;

public class FilterTimeBox extends TimeBox {

	private final Icon clearIcon = Icons.ALL.clear()
			.setTooltip("Clear")
			.style()
			.setCursor(Cursor.POINTER.getCssName())
			.get();
	private final EventListener clearListener;

	public FilterTimeBox(final String label, final EventListener clearListener) {
		super(label, null);
		this.clearListener = clearListener;
		addRightAddOn(clearIcon);
		fieldContainer.styler(FilterBarPlugin::filterFieldStyle);
		clearIcon.addClickListener(this::clearBox);
		getTimePicker().setClockStyle(ClockStyle._24);
		clear();
	}

	public static TimeBox create(final String label, final EventListener clearListener) {
		return new FilterTimeBox(label, clearListener)
				.setPickerStyle(PickerStyle.MODAL)
				.styler(FilterBarPlugin::zerroBottomMargin);
	}

	private void clearBox(final Event evt) {
		clear();
		clearListener.handleEvent(evt);
	}

	@Override
	protected FlexItem createMandatoryAddOn() {
		return null;
	}
}
