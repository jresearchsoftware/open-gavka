package org.jresearch.gavka.gwt.core.client.module.message.widget;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.REST;
import org.jresearch.commons.gwt.client.mvc.AbstractMethodCallback;
import org.jresearch.commons.gwt.client.mvc.GwtMethodCallback;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.commons.gwt.client.tool.Dates;
import org.jresearch.commons.gwt.client.tool.GwtDeferredTask;
import org.jresearch.commons.gwt.client.widget.Uis;
import org.jresearch.commons.gwt.shared.model.time.GwtLocalDateModel;
import org.jresearch.commons.gwt.shared.model.time.GwtLocalDateTimeModel;
import org.jresearch.commons.gwt.shared.model.time.GwtLocalTimeModel;
import org.jresearch.gavka.domain.KeyFormat;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.domain.MessageFormat;
import org.jresearch.gavka.gwt.core.client.module.message.srv.GavkaMessageRestService;
import org.jresearch.gavka.rest.api.MessageParameters;
import org.jresearch.gavka.rest.api.MessagePortion;
import org.jresearch.gavka.rest.api.PagingParameters;
import org.jresearch.gavka.rest.api.RequestMessagesParameters;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.tractionsoftware.gwt.user.client.ui.UTCDateBox;
import com.tractionsoftware.gwt.user.client.ui.UTCTimeBox;

@SuppressWarnings("nls")
@Singleton
public class MessagePage extends Composite {

	private static final int AUTOREFRESH_TIME = 10_000;

	// @formatter:off
	interface Binder extends UiBinder<DockLayoutPanel, MessagePage> {/* nothing */}
	// @formatter:on

	@UiField(provided = true)
	DataGrid<Message> messages;
	@UiField
	Button searchBtn;
	@UiField
	Button nextBtn;
	@UiField
	Button prevBtn;
	@UiField
	ListBox topic;
	@UiField
	TextBox key;
	@UiField
	ListBox keyFormat;
	@UiField
	ListBox messageFormat;
	@UiField
	UTCDateBox date;
	@UiField
	UTCTimeBox time;
	@UiField
	Hidden hiddenFrom;
	@UiField
	FormPanel exportForm;
	@UiField
	Button exportBtn;

	private final GwtDeferredTask refreshTask = new GwtDeferredTask(this::refreshOnSearch);
	@Nonnull
	private final GavkaMessageRestService srv;
	@Nonnull
	private final Bus bus;
	@Nonnull
	private final Stack<PagingParameters> pages;
	private final int currentAmount = 100;

	@Inject
	protected MessagePage(@Nonnull final Binder binder, @Nonnull final GavkaMessageRestService srv, @Nonnull final Bus bus) {
		this.srv = srv;
		this.bus = bus;
		pages = new Stack<>();
		pages.push(new PagingParameters(currentAmount, ImmutableList.of()));
		messages = createDatagrid();
		initWidget(binder.createAndBindUi(this));
		setStyleName("MessagePage");
		REST.withCallback(new GwtMethodCallback<>(bus, this::addTopics)).call(srv).topics();
		EnumSet.allOf(KeyFormat.class).stream().map(Enum::name).forEach(keyFormat::addItem);
		EnumSet.allOf(MessageFormat.class).stream().map(Enum::name).forEach(messageFormat::addItem);
		exportForm.setMethod(FormPanel.METHOD_POST);
	}

	@SuppressWarnings("null")
	@UiHandler("exportBtn")
	void onExportBtn(@SuppressWarnings("unused") final ClickEvent event) {
		final Optional<GwtLocalDateTimeModel> fromDate = getFrom();
		if (fromDate.isPresent()) {
			hiddenFrom.setValue(Dates.printDateTime(fromDate.get()));
		}
		exportForm.submit();
	}

	@UiHandler("searchBtn")
	void onSearchBtn(@SuppressWarnings("unused") final ClickEvent event) {
		refreshTask.defer(0);
	}

	@UiHandler("nextBtn")
	void onNextBtn(@SuppressWarnings("unused") final ClickEvent event) {
		refreshTask.cancel();
		pages.peek().setAmount(getCurrentAmount());
		refresh();
	}

	@UiHandler("prevBtn")
	void onPrevBtn(@SuppressWarnings("unused") final ClickEvent event) {
		if (pages.size() > 2) {
			refreshTask.cancel();
			pages.pop();
			pages.pop();
			refresh();
		} else {
			prevBtn.setEnabled(false);
		}
	}

	@UiHandler("topic")
	void onTopic(@SuppressWarnings("unused") final ChangeEvent event) {
		refreshTask.defer(AUTOREFRESH_TIME);
	}

	@UiHandler("key")
	void onKey(@SuppressWarnings("unused") final ChangeEvent event) {
		refreshTask.defer(AUTOREFRESH_TIME);
	}

	@UiHandler("date")
	void onDate(@SuppressWarnings("unused") final ValueChangeEvent<Long> event) {
		refreshTask.defer(AUTOREFRESH_TIME);
	}

	@UiHandler("time")
	void onTime(@SuppressWarnings("unused") final ValueChangeEvent<Long> event) {
		refreshTask.defer(AUTOREFRESH_TIME);
	}

	@UiHandler("keyFormat")
	void onKeyFormat(@SuppressWarnings("unused") final ChangeEvent event) {
		refreshTask.defer(AUTOREFRESH_TIME);
	}

	@UiHandler("messageFormat")
	void onMessageFormat(@SuppressWarnings("unused") final ChangeEvent event) {
		refreshTask.defer(AUTOREFRESH_TIME);
	}

	private DataGrid<Message> createDatagrid() {
		final DataGrid<Message> dataGrid = new DataGrid<>(30);

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

		// timestamp (UTC)
		final Column<Message, Date> colTimeStampUtc = new Column<Message, Date>(new DateCell(DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT), com.google.gwt.i18n.client.TimeZone.createTimeZone(0))) {
			@Override
			public Date getValue(final Message object) {
				return new Date(object.getTimestamp());
			}
		};

		// timestamp (Browser)
		final Column<Message, Date> colTimeStampLocal = new Column<Message, Date>(new DateCell(DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT))) {
			@Override
			public Date getValue(final Message object) {
				return new Date(object.getTimestamp());
			}
		};

		dataGrid.addColumn(colKey, "Key");
		dataGrid.setColumnWidth(colKey, 30, Unit.PCT);
		dataGrid.addColumn(colValue, "Value");
		dataGrid.setColumnWidth(colValue, 30, Unit.PCT);
		dataGrid.addColumn(colOffset, "Offset");
		dataGrid.setColumnWidth(colOffset, 10, Unit.PCT);
		dataGrid.addColumn(colPartition, "Partition");
		dataGrid.setColumnWidth(colPartition, 10, Unit.PCT);
		dataGrid.addColumn(colTimeStampLocal, "Timestamp (Browser)");
		dataGrid.setColumnWidth(colTimeStampLocal, 20, Unit.PCT);
		dataGrid.addColumn(colTimeStampUtc, "Timestamp (UTC)");
		dataGrid.setColumnWidth(colTimeStampUtc, 20, Unit.PCT);

		dataGrid.setEmptyTableWidget(new HTML("No records"));

		new AsyncDataProvider<Message>() {
			@Override
			protected void onRangeChanged(final HasData<Message> display) {
				final RequestMessagesParameters parameters = new RequestMessagesParameters();
				final MessageParameters messageParameters = new MessageParameters();
				messageParameters.setTopic(getTopic());
				messageParameters.setFrom(getFrom().orElse(null));
				messageParameters.setKey(getKeyValue());
				messageParameters.setKeyFormat(getKeyFormat());
				messageParameters.setMessageFormat(getMessageFormat());
				parameters.setMessageParameters(messageParameters);
				parameters.setPagingParameters(pages.peek());
				REST.withCallback(new AbstractMethodCallback<MessagePortion>(bus) {
					@Override
					public void onSuccess(final Method method, final MessagePortion result) {
						if (result != null) {
							final PagingParameters pagingParameters = new PagingParameters();
							pagingParameters.setAmount(getCurrentAmount());
							pagingParameters.setPartitionOffsets(result.getPartitionOffsets());
							pages.push(pagingParameters);
							prevBtn.setEnabled(pages.size() > 2);
							final List<Message> msgs = result.getMessages();
							dataGrid.setRowCount(msgs.size());
							updateRowData(0, msgs);
						}
					}
				}).call(srv).get(parameters);
			}

		}.addDataDisplay(dataGrid);

		return dataGrid;

	}

	protected String getKeyValue() {
		return Optional
				.ofNullable(key)
				.map(TextBox::getValue)
				.orElse(Uis.NOTHING);
	}

	protected MessageFormat getMessageFormat() {
		return Optional
				.ofNullable(messageFormat)
				.map(ListBox::getSelectedValue)
				.map(MessageFormat::valueOf)
				.orElse(MessageFormat.values()[0]);
	}

	protected KeyFormat getKeyFormat() {
		return Optional
				.ofNullable(keyFormat)
				.map(ListBox::getSelectedValue)
				.map(KeyFormat::valueOf)
				.orElse(KeyFormat.values()[0]);
	}

	private int getCurrentAmount() {
		return currentAmount;
	}

	@SuppressWarnings("null")
	@Nonnull
	protected String getTopic() {
		return Optional
				.ofNullable(topic)
				.map(ListBox::getSelectedValue)
				.orElse(Uis.NOTHING);
	}

	@SuppressWarnings("null")
	@Nonnull
	protected Optional<GwtLocalDateTimeModel> getFrom() {
		final GwtLocalDateModel gwtDate = Optional
				.ofNullable(date)
				.map(HasValue::getValue)
				.map(Dates::toLocalDate)
				.orElse(null);
		final GwtLocalTimeModel gwtTime = Optional
				.ofNullable(time)
				.map(HasValue::getValue)
				.map(Ints::saturatedCast)
				.map(GwtLocalTimeModel::new)
				.orElseGet(GwtLocalTimeModel::new);
		return Optional.ofNullable(gwtDate == null ? null : new GwtLocalDateTimeModel(gwtDate, gwtTime));
	}

	public void refreshOnSearch() {
		pages.clear();
		pages.add(new PagingParameters(currentAmount, ImmutableList.of()));
		refresh();
	}

	public void refresh() {
		messages.setRowData(0, ImmutableList.<Message>of());
		messages.setVisibleRangeAndClearData(new Range(0, getCurrentAmount()), true);
	}

	public void addTopics(final List<String> topics) {
		topics.stream().forEach(topic::addItem);
	}

}
