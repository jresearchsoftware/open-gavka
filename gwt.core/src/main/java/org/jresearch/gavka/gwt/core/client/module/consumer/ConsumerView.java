package org.jresearch.gavka.gwt.core.client.module.consumer;

import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.datatable.CellRenderer.CellInfo;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.TableRow;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import org.dominokit.domino.ui.grid.Column;
import org.dominokit.domino.ui.grid.Row;
import org.dominokit.domino.ui.grid.Row_16;
import org.dominokit.domino.ui.loaders.Loader;
import org.dominokit.domino.ui.loaders.LoaderEffect;
import org.dominokit.domino.ui.utils.TextNode;
import org.jresearch.commons.gwt.client.mvc.AbstractView;
import org.jresearch.commons.gwt.client.mvc.INotificator;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.module.consumer.srv.GavkaConsumerRestService;
import org.jresearch.gavka.gwt.core.client.module.consumer.widget.ConsumerDataSource;
import org.jresearch.gavka.rest.data.GroupInfo;
import org.jresearch.gavka.rest.data.PartitionInfo;
import org.jresearch.gavka.rest.data.TopicRestInfo;

import com.google.common.collect.ImmutableList;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.NumberFormat;

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import elemental2.dom.Node;

public class ConsumerView extends AbstractView<ConsumerController> {

	enum PartitionsColumnName {
		PARTITION,
		START_OFFSET,
		END_OFFSET,
	}

	enum GroupsColumnName {
		GROUP,
		PARTITION,
		OFFSET,
		LAG,
	}

	private static final NumberFormat NUMBER_FORMAT = NumberFormat.getDecimalFormat();

	@Nonnull
	private final Card tableCard;
	@Nonnull
	private final LocalListDataStore<PartitionInfo> partitionDataStore;
	@Nonnull
	private final LocalListDataStore<GroupInfo> groupDataStore;
	@Nonnull
	private final ConsumerDataSource consumerDataSource;
	@Nonnull
	private final Loader partitionLoader;
	@Nonnull
	private final Loader groupLoader;
	@Nonnull
	private final DataTable<PartitionInfo> partitionTable;
	@Nonnull
	private final DataTable<GroupInfo> groupTable;
	@Nonnull
	private final Row_16 partitionNoDataElement;
	@Nonnull
	private final Row_16 groupNoDataElement;

	@SuppressWarnings("null")
	public ConsumerView(@Nonnull final INotificator notificator, @Nonnull final GavkaConsumerRestService srv, @Nonnull final Bus bus, @Nonnull final String connectionId, @Nonnull final String topic) {
		super(null, notificator);
		this.consumerDataSource = new ConsumerDataSource(connectionId, topic, srv, bus);
		partitionNoDataElement = Row.of16Colmns().appendChild(Column.span16().appendChild(DomGlobal.document.createTextNode("No data")));

		final TableConfig<PartitionInfo> partitionTableConfig = createPartitionTableConfig();

		partitionDataStore = new LocalListDataStore<>();
		(partitionTable = new DataTable<>(partitionTableConfig, partitionDataStore)).bodyElement().appendChild(partitionNoDataElement);
		partitionLoader = Loader.create(partitionTable.asElement(), LoaderEffect.WIN8);

		groupNoDataElement = Row.of16Colmns().appendChild(Column.span16().appendChild(DomGlobal.document.createTextNode("No data")));

		final TableConfig<GroupInfo> groupTableConfig = createGroupTableConfig();

		groupDataStore = new LocalListDataStore<>();
		(groupTable = new DataTable<>(groupTableConfig, groupDataStore)).bodyElement().appendChild(groupNoDataElement);
		groupLoader = Loader.create(groupTable.asElement(), LoaderEffect.WIN8);

		tableCard = Card.create()
				.appendChild(partitionTable.styler(s -> s.setMarginBottom(1 + Unit.EM.getType())))
				.appendChild(groupTable.styler(s -> s.setMarginBottom(1 + Unit.EM.getType())));
	}

	private void refresh() {
		groupLoader.start();
		partitionLoader.start();
		groupDataStore.setData(ImmutableList.of());
		groupDataStore.load();
		partitionDataStore.setData(ImmutableList.of());
		partitionDataStore.load();
		consumerDataSource.load(this::onLoad);
	}

	private void onLoad(final TopicRestInfo data) {
		groupDataStore.setData(data.groupInfo());
		groupDataStore.load();
		partitionDataStore.setData(data.partitionInfo());
		partitionDataStore.load();
		groupLoader.stop();
		partitionLoader.stop();
		if (data.groupInfo().isEmpty()) {
			groupTable.bodyElement().appendChild(groupNoDataElement);
		}
		if (data.partitionInfo().isEmpty()) {
			partitionTable.bodyElement().appendChild(partitionNoDataElement);
		}
	}

	@SuppressWarnings("null")
	@Override
	public HTMLElement getContent() {
		return tableCard.asElement();
	}

	private static TableConfig<PartitionInfo> createPartitionTableConfig() {
		return new TableConfig<PartitionInfo>()
				.addColumn(ColumnConfig.<PartitionInfo>create(PartitionsColumnName.PARTITION.name(), "Partition")
						.setCellRenderer(cell -> renderNumber(cell, PartitionInfo::partitionNumber)))
				.addColumn(ColumnConfig.<PartitionInfo>create(PartitionsColumnName.START_OFFSET.name(), "Start offset")
						.setCellRenderer(cell -> renderNumber(cell, PartitionInfo::startOffset)))
				.addColumn(ColumnConfig.<PartitionInfo>create(PartitionsColumnName.END_OFFSET.name(), "End offset")
						.setCellRenderer(cell -> renderNumber(cell, PartitionInfo::endOffset)));
	}

	private static TableConfig<GroupInfo> createGroupTableConfig() {
		return new TableConfig<GroupInfo>()
				.addColumn(ColumnConfig.<GroupInfo>create(GroupsColumnName.GROUP.name(), "Group")
						.setCellRenderer(cell -> renderText(cell, GroupInfo::groupId)))
				.addColumn(ColumnConfig.<GroupInfo>create(GroupsColumnName.PARTITION.name(), "Partition")
						.setCellRenderer(cell -> renderNumber(cell, GroupInfo::partition)))
				.addColumn(ColumnConfig.<GroupInfo>create(GroupsColumnName.OFFSET.name(), "Offset")
						.setCellRenderer(cell -> renderNumber(cell, GroupInfo::currentOffset)))
				.addColumn(ColumnConfig.<GroupInfo>create(GroupsColumnName.LAG.name(), "Lag")
						.setCellRenderer(cell -> renderNumber(cell, GroupInfo::lag)));
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

}
