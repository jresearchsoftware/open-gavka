package org.jresearch.gavka.gwt.core.client.module.consumer;

import static org.jresearch.gavka.gwt.core.client.module.consumer.ConsumerView.ColumnName.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.datatable.CellRenderer.CellInfo;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.TableRow;
import org.dominokit.domino.ui.datatable.events.SearchEvent;
import org.dominokit.domino.ui.datatable.events.TableEvent;
import org.dominokit.domino.ui.datatable.events.TableEventListener;
import org.dominokit.domino.ui.datatable.plugins.RecordDetailsPlugin;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import org.dominokit.domino.ui.grid.Column;
import org.dominokit.domino.ui.grid.Row;
import org.dominokit.domino.ui.grid.Row_16;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.loaders.Loader;
import org.dominokit.domino.ui.loaders.LoaderEffect;
import org.dominokit.domino.ui.utils.TextNode;
import org.jresearch.commons.gwt.client.mvc.AbstractView;
import org.jresearch.commons.gwt.client.mvc.INotificator;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.gwt.core.client.module.consumer.widget.FilterBarPlugin;
import org.jresearch.gavka.gwt.core.client.module.consumer.widget.ConsumerDataSource;
import org.jresearch.gavka.gwt.core.client.module.consumer.widget.ConsumerDetails;
import org.jresearch.gavka.rest.api.MessageParameters;

import com.google.common.collect.ImmutableList;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;

import elemental2.dom.CSSProperties;
import elemental2.dom.DomGlobal;
import elemental2.dom.Event;
import elemental2.dom.HTMLElement;
import elemental2.dom.Node;

public class ConsumerView extends AbstractView<ConsumerController> implements TableEventListener {

	enum ColumnName {
		KEY,
		MESSAGE,
		OFFSET,
		PARTITION,
		TIMESTAMP_LOCAL,
		TIMESTAMP_UTC
	}

	private static final NumberFormat NUMBER_FORMAT = NumberFormat.getDecimalFormat();
	private static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT);

	@Nonnull
	private final Card tableCard;
	@Nonnull
	private final LocalListDataStore<Message> localListDataStore;
	@Nonnull
	private final ConsumerDataSource consumerDataSource;
	@Nonnull
	private Button prevBtnTop;
	@Nonnull
	private Button prevBtnBottom;
	@Nonnull
	private final Loader loader;
	@Nonnull
	private final DataTable<Message> table;
	@Nonnull
	private final Row_16 noDataElement;
	@Nonnull
	private final FilterBarPlugin filter;

	@SuppressWarnings("null")
	public ConsumerView(@Nonnull final INotificator notificator, @Nonnull final ConsumerDataSource consumerDataSource, @Nonnull final String connectionId, @Nonnull final String topic) {
		super(null, notificator);
		this.filter = new FilterBarPlugin(connectionId, topic);
		noDataElement = Row.of16Colmns().appendChild(Column.span16().appendChild(DomGlobal.document.createTextNode("No data")));
		this.consumerDataSource = consumerDataSource;

		final TableConfig<Message> tableConfig = createBasicTableConfig()
				.addPlugin(filter)
				.addPlugin(new RecordDetailsPlugin<>(cell -> new ConsumerDetails(cell).asElement(), Icons.ALL.folder_open(), Icons.ALL.folder()));

		localListDataStore = new LocalListDataStore<>();
		(table = new DataTable<>(tableConfig, localListDataStore)).bodyElement().appendChild(noDataElement);
		loader = Loader.create(table.asElement(), LoaderEffect.WIN8);
		tableCard = Card.create()
				.appendChild(Row.of32Colmns()
						.appendChild(Column.span1()
								.appendChild((prevBtnTop = Button.create("Prev")
										.disable()
										.addClickListener(this::onPrevBtn))))
						.appendChild(Column.span1()
								.appendChild(Button.create("Next")
										.addClickListener(this::onNextBtn)))
						.appendChild(Column.span1().offset(31)
								.appendChild(Button.create("Export")
										.addClickListener(this::onExportBtn))))
				.appendChild(table.styler(s -> s.setMarginBottom(1 + Unit.EM.getType())))
				.appendChild(Row.of32Colmns()
						.appendChild(Column.span1()
								.appendChild((prevBtnBottom = Button.create("Prev")
										.disable()
										.addClickListener(this::onPrevBtn))))
						.appendChild(Column.span1()
								.appendChild(Button.create("Next")
										.addClickListener(this::onNextBtn)))
						.appendChild(Column.span1().offset(31)
								.appendChild(Button.create("Export")
										.addClickListener(this::onExportBtn))));
		table.addTableEventListner(SearchEvent.SEARCH_EVENT, this);
	}

	private void onExportBtn(@SuppressWarnings("unused") final Event evt) {
		filter.export();
	}

	private void onNextBtn(@SuppressWarnings("unused") final Event evt) {
		consumerDataSource.next();
		refresh();
	}

	private void onPrevBtn(@SuppressWarnings("unused") final Event evt) {
		if (consumerDataSource.isPreviousePartExist()) {
			consumerDataSource.prev();
			refresh();
		} else {
			prevBtnTop.disable();
			prevBtnBottom.disable();
		}
	}

	private void refresh() {
		final MessageParameters parameters = filter.getMessageParameters();
		if (consumerDataSource.isReloadNeed(parameters)) {
			loader.start();
			localListDataStore.setData(ImmutableList.of());
			localListDataStore.load();
			consumerDataSource.load(parameters, this::onLoad);
		}
	}

	private void onLoad(final List<Message> data) {
		localListDataStore.setData(data);
		localListDataStore.load();
		prevBtnTop.setDisabled(!consumerDataSource.isPreviousePartExist());
		prevBtnBottom.setDisabled(!consumerDataSource.isPreviousePartExist());
		loader.stop();
		if (data.isEmpty()) {
			table.bodyElement().appendChild(noDataElement);
		}
	}

	@SuppressWarnings("null")
	@Override
	public HTMLElement getContent() {
		return tableCard.asElement();
	}

	private static TableConfig<Message> createBasicTableConfig() {
		final TableConfig<Message> tableConfig = new TableConfig<>();
		final ColumnConfig<Message> messageColumnConfig = ColumnConfig.<Message>create(MESSAGE.name(), "Message");
		messageColumnConfig.styleCell(e -> {
			e.style.width = CSSProperties.WidthUnionType.of("60%");
			e.style.maxWidth = CSSProperties.MaxWidthUnionType.of("300px");
			e.style.overflow = "hidden";
			e.style.whiteSpace = "nowrap";
			e.style.textOverflow = "ellipsis";
		});
		tableConfig
//				.setFixedBodyHeight("calc(100vh - 460px)")
				.addColumn(ColumnConfig.<Message>create(KEY.name(), "Key")
						.setWidth(30 + Unit.PCT.getType())
						.setCellRenderer(cell -> renderText(cell, Message::getKey)))
				.addColumn(messageColumnConfig
						.setCellRenderer(cell -> renderText(cell, Message::getValue)))
				.addColumn(ColumnConfig.<Message>create(TIMESTAMP_LOCAL.name(), "Timestamp (Browser)")
						.setCellRenderer(cell -> renderLocalDate(cell, Message::getTimestamp)))
				.addColumn(ColumnConfig.<Message>create(TIMESTAMP_UTC.name(), "Timestamp (UTC)")
						.setCellRenderer(cell -> renderUtcDate(cell, Message::getTimestamp)))
				.addColumn(ColumnConfig.<Message>create(PARTITION.name(), "Partition")
						.setCellRenderer(cell -> renderNumber(cell, Message::getPartition)))
				.addColumn(ColumnConfig.<Message>create(OFFSET.name(), "Offset")
						.setCellRenderer(cell -> renderNumber(cell, Message::getOffset)));
		return tableConfig;
	}

	private static <R> Node renderLocalDate(final CellInfo<R> cellInfo, final Function<R, Long> valueAccessor) {
		return renderDate(cellInfo, null, valueAccessor);
	}

	private static <R> Node renderUtcDate(final CellInfo<R> cellInfo, final Function<R, Long> valueAccessor) {
		return renderDate(cellInfo, TimeZone.createTimeZone(0), valueAccessor);
	}

	@SuppressWarnings("null")
	private static <R> Node renderDate(final CellInfo<R> cellInfo, final TimeZone timeZone, final Function<R, Long> valueAccessor) {
		return record(cellInfo).map(valueAccessor).map(Date::new).map(d -> DATE_FORMAT.format(d, timeZone)).map(TextNode::of).orElseGet(TextNode::empty);
	}

	private static <R> Node renderText(final CellInfo<R> cellInfo, final Function<R, String> valueAccessor) {
		return record(cellInfo).map(valueAccessor).map(TextNode::of).orElseGet(TextNode::empty);
	}

	private static <R> Node renderNumber(final CellInfo<R> cellInfo, final Function<R, Number> valueAccessor) {
		return record(cellInfo).map(valueAccessor).map(NUMBER_FORMAT::format).map(TextNode::of).orElseGet(TextNode::empty);
	}

	private static <R> Optional<R> record(final CellInfo<R> cellInfo) {
		return Optional.of(cellInfo).map(CellInfo::getTableRow).map(TableRow::getRecord);
	}

	@Override
	public void handleEvent(final TableEvent event) {
		if (event.getType().equals(SearchEvent.SEARCH_EVENT)) {
			refresh();
		}
	}
}
