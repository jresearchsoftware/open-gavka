package org.jresearch.gavka.gwt.core.client.module.message.widget;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

import javax.annotation.Nonnull;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.REST;
import org.jresearch.commons.gwt.client.mvc.AbstractMethodCallback;
import org.jresearch.commons.gwt.client.mvc.GwtMethodCallback;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.commons.gwt.client.tool.Dates;
import org.jresearch.commons.gwt.client.tool.GwtDeferredTask;
import org.jresearch.commons.gwt.client.widget.Uis;
import org.jresearch.commons.gwt.shared.model.time.GwtLocalDateModel;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.gwt.core.client.module.message.srv.GavkaMessageRestService;
import org.jresearch.gavka.rest.api.MessageParameters;
import org.jresearch.gavka.rest.api.MessagePortion;
import org.jresearch.gavka.rest.api.PagingParameters;
import org.jresearch.gavka.rest.api.RequestMessagesParameters;

import com.google.common.collect.ImmutableList;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tractionsoftware.gwt.user.client.ui.UTCDateBox;

@SuppressWarnings("nls")
@Singleton
public class MessagePage extends Composite {

	// @formatter:off
	interface Binder extends UiBinder<DockLayoutPanel, MessagePage> {/* nothing */}
	// @formatter:on

	@UiField(provided = true)
	DataGrid<Message> messages;
	@UiField
	Button nextBtn;
	@UiField
	Button prevBtn;
	@UiField
	ListBox topic;
	@UiField
	UTCDateBox to;
	@UiField
	UTCDateBox from;
	@UiField
	CheckBox avro;

	private final GwtDeferredTask refreshTask = new GwtDeferredTask(this::refresh);
	private final GavkaMessageRestService srv;
	@Nonnull
	private final Bus bus;
	@Nonnull
	private final Stack<PagingParameters> pages;
	private final int currentAmount = 100;

	@Inject
	protected MessagePage(@Nonnull final Binder binder, final GavkaMessageRestService srv, @Nonnull final Bus bus) {
		this.srv = srv;
		this.bus = bus;
		pages = new Stack<>();
		pages.push(new PagingParameters(currentAmount, ImmutableList.of()));
		messages = createDatagrid();
		initWidget(binder.createAndBindUi(this));
		setStyleName("MessagePage");
		REST.withCallback(new GwtMethodCallback<>(bus, this::addTopics)).call(srv).topics();
	}

	@UiHandler("nextBtn")
	void onNextBtn(@SuppressWarnings("unused") final ClickEvent event) {
		pages.peek().setAmount(getCurrentAmount());
		refresh();
	}

	@UiHandler("prevBtn")
	void onPrevBtn(@SuppressWarnings("unused") final ClickEvent event) {
		pages.pop();
		refresh();
	}

	@UiHandler("topic")
	void onTopic(@SuppressWarnings("unused") final ChangeEvent event) {
		refreshTask.defer(750);
	}

	@UiHandler("from")
	void onFrom(@SuppressWarnings("unused") final ValueChangeEvent<Long> event) {
		refreshTask.defer(750);
	}

	@UiHandler("to")
	void onTo(@SuppressWarnings("unused") final ValueChangeEvent<Long> event) {
		refreshTask.defer(750);
	}

	@UiHandler("avro")
	void onAvto(@SuppressWarnings("unused") final ValueChangeEvent<Boolean> event) {
		refreshTask.defer(750);
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

		dataGrid.addColumn(colKey, "Key");
		dataGrid.setColumnWidth(colKey, 30, Unit.PCT);
		dataGrid.addColumn(colValue, "Value");
		dataGrid.setColumnWidth(colValue, 30, Unit.PCT);
		dataGrid.addColumn(colOffset, "Offset");
		dataGrid.setColumnWidth(colOffset, 10, Unit.PCT);

		new AsyncDataProvider<Message>() {
			@Override
			protected void onRangeChanged(final HasData<Message> display) {
				final RequestMessagesParameters parameters = new RequestMessagesParameters();
				final MessageParameters messageParameters = new MessageParameters();
				messageParameters.setTopic(getTopic());
				messageParameters.setFrom(getFrom());
				messageParameters.setTo(getTo());
				messageParameters.setAvro(isAvro());
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
	protected GwtLocalDateModel getFrom() {
		return Optional
				.ofNullable(from)
				.map(HasValue::getValue)
				.map(Dates::toLocalDate)
				.orElse(Dates.today());
	}

	@SuppressWarnings("null")
	@Nonnull
	protected GwtLocalDateModel getTo() {
		return Optional
				.ofNullable(to)
				.map(HasValue::getValue)
				.map(Dates::toLocalDate)
				.orElse(Dates.today());
	}

	protected boolean isAvro() {
		return Optional
				.ofNullable(avro)
				.map(HasValue::getValue)
				.orElse(Boolean.FALSE)
				.booleanValue();
	}

	public void refresh() {
		messages.setRowData(0, ImmutableList.<Message> of());
		messages.setVisibleRangeAndClearData(new Range(0, getCurrentAmount()), true);
	}

	public void addTopics(final List<String> topics) {
		topics.stream().forEach(topic::addItem);
	}

}
