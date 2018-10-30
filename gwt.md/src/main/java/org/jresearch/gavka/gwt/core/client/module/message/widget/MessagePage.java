package org.jresearch.gavka.gwt.core.client.module.message.widget;

import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.rest.api.MessageParameters;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.table.MaterialDataTable;
import gwt.material.design.client.ui.table.cell.Column;
import gwt.material.design.client.ui.table.cell.TextColumn;
import gwt.material.design.jquery.client.api.JQueryElement;

@SuppressWarnings("nls")
@Singleton
public class MessagePage extends Composite {

	private static final int AUTOREFRESH_TIME = 10_000;

	// @formatter:off
	interface Binder extends UiBinder<MaterialContainer, MessagePage> {/* nothing */}
	// @formatter:on

	@UiField
	MaterialDataTable<Message> table;

	// FIXME fake parameters (replace with real form)
	private final MessageParameters messageParameters = new MessageParameters();

	@Nonnull
	private final MessageDataSource messageDataSource;

//	@UiField
//	MaterialCollapsible records;

	@Inject
	protected MessagePage(@Nonnull final Binder binder, @Nonnull final MessageDataSource messageDataSource) {
		this.messageDataSource = messageDataSource;
		initWidget(binder.createAndBindUi(this));

		initMaterialDataTable();

//		ViewPort.when(Resolution.ALL_MOBILE).then(param1 -> {
//			table.setHeight("60vh");
//		}, viewPort -> {
//			table.setHeight("100%");
//			return false;
//		});

		refresh();
	}

	private void refresh() {
		messageDataSource.load(messageParameters, this::updateTableData);
	}

	private void initMaterialDataTable() {
		// Key
		final TextColumn<Message> colKey = new TextColumn<Message>() {
			@Override
			public String getValue(final Message object) {
				return object.getKey();
			}
		};

		// Value
		final TextColumn<Message> colValue = new TextColumn<Message>() {
			@Override
			public String getValue(final Message object) {
				return object.getValue();
			}
		};

		// Offset
		final Column<Message, Number> colOffset = new Column<Message, Number>(new NumberCell()) {
			@Override
			public Number getValue(final Message object) {
				return Long.valueOf(object.getOffset());
			}
		};

		// partition
		final Column<Message, Number> colPartition = new Column<Message, Number>(new NumberCell()) {
			@Override
			public Number getValue(final Message object) {
				return Long.valueOf(object.getPartition());
			}
		};

		// timestamp
		final Column<Message, Date> colTimeStamp = new Column<Message, Date>(new DateCell(DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT), com.google.gwt.i18n.client.TimeZone.createTimeZone(0))) {
			@Override
			public Date getValue(final Message object) {
				return new Date(object.getTimestamp());
			}
		};

		table.addColumn(colKey, "Key");
		table.addColumn(colValue, "Value");
		table.addColumn(colOffset, "Offset");
		table.addColumn(colPartition, "Partition");
		table.addColumn(colTimeStamp, "Timestamp");

		table.addRowExpandingHandler(event -> {
			final JQueryElement section = event.getExpansion().getOverlay();
			section.css("display", "none");

			final MaterialWidget content = new MaterialWidget(event.getExpansion().getContent());
			content.add(new MaterialLabel(event.getExpansion().getModel().toString()));

//			// Clear the content first.
//			final MaterialWidget content = new MaterialWidget(
//					event.getExpansion().getContent().empty().asElement());
//
//			// Fake Async Task
//			// This is demonstrating a fake asynchronous call to load
//			// the data inside the expansion element.
//			new Timer() {
//				@Override
//				public void run() {
//					final MaterialLabel title = new MaterialLabel("Expansion Row Panel");
//					title.setFontSize("1.6em");
//					title.setDisplay(Display.BLOCK);
//					final MaterialLabel description = new MaterialLabel("This content was made from asynchronous call");
//
//					content.setPadding(20);
//					content.add(title);
//					content.add(description);
//
//					// Hide the expansion elements overlay section.
//					// The overlay is retrieved using EowExpand#getOverlay()
//					section.css("display", "none");
//				}
//			}.schedule(2000);
		});

	}

	private void updateTableData(final List<Message> msgs) {
		table.setRowData(0, msgs);
	}

}
