package org.jresearch.gavka.gwt.core.client.module.consumer.widget;

import org.dominokit.domino.ui.datatable.CellRenderer;
import org.dominokit.domino.ui.grid.Column;
import org.dominokit.domino.ui.grid.Row;
import org.dominokit.domino.ui.style.Styles;
import org.dominokit.domino.ui.utils.TextNode;
import org.jboss.gwt.elemento.core.IsElement;
import org.jresearch.gavka.domain.Message;

import elemental2.dom.HTMLElement;

public class ConsumerDetails implements IsElement<HTMLElement> {

	private final Row rowElement = Row.create()
			.style().add(Styles.margin_0).get();
	private final CellRenderer.CellInfo<Message> cell;

	public ConsumerDetails(final CellRenderer.CellInfo<Message> cell) {
		this.cell = cell;
		initDetails();
	}

	private void initDetails() {
		final Message msg = cell.getTableRow().getRecord();

		rowElement
				.addColumn(Column.span12()
						.appendChild(TextNode.of(msg.getValue())));
	}

	@Override
	public HTMLElement asElement() {
		return rowElement.asElement();
	}
}