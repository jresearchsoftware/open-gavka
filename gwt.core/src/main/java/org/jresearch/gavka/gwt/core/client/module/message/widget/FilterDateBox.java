package org.jresearch.gavka.gwt.core.client.module.message.widget;

import org.dominokit.domino.ui.datepicker.DateBox;
import org.dominokit.domino.ui.grid.flex.FlexItem;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.Icons;

import com.google.gwt.dom.client.Style.Cursor;

import elemental2.dom.Event;
import elemental2.dom.EventListener;

public class FilterDateBox extends DateBox {

	private final Icon clearIcon = Icons.ALL.clear()
			.setTooltip("Clear")
			.style()
			.setCursor(Cursor.POINTER.getCssName())
			.get();
	private final EventListener clearListener;

	public FilterDateBox(final String label, final EventListener clearListener) {
		super(label);
		this.clearListener = clearListener;
		addRightAddOn(clearIcon);
		fieldContainer.styler(FilterBarPlugin::filterFieldStyle);
		clearIcon.addClickListener(this::clearBox);
	}

	public static DateBox create(final String label, final EventListener clearListener) {
		return new FilterDateBox(label, clearListener)
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
