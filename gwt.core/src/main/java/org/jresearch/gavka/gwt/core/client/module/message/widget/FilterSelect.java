package org.jresearch.gavka.gwt.core.client.module.message.widget;

import org.dominokit.domino.ui.forms.Select;
import org.dominokit.domino.ui.grid.flex.FlexItem;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.style.Style;

import com.google.gwt.dom.client.Style.Cursor;

import elemental2.dom.Event;
import elemental2.dom.EventListener;

public class FilterSelect<V> extends Select<V> {

	private final Icon clearIcon = Icons.ALL.clear()
			.setTooltip("Clear")
			.style()
			.setCursor(Cursor.POINTER.getCssName())
			.get();
	private final EventListener clearListener;

	public FilterSelect(final String label, final String name, final EventListener clearListener) {
		super(label);
		this.clearListener = clearListener;
		addRightAddOn(clearIcon);
		fieldContainer.styler(FilterBarPlugin::filterFieldStyle);
		clearIcon.addClickListener(this::clearBox);
		styler(FilterBarPlugin::zerroBottomMargin);
		setName(name);
		getSelectButton().styler(FilterSelect::filterSelectStyle);
	}

	public static <V> Select<V> create(final String label, final String name, final EventListener clearListener) {
		return new FilterSelect<>(label, name, clearListener);
	}

	private void clearBox(final Event evt) {
		selectAt(0);
		clearListener.handleEvent(evt);
	}

	private static void filterSelectStyle(final Style<?, ?> style) {
		style.setHeight(FilterBarPlugin.INHERIT);
	}

	@SuppressWarnings("nls")
	@Override
	protected FlexItem createMandatoryAddOn() {
		return super.createMandatoryAddOn()
				.styler(s -> s.setHeight("0"));
	}

}
