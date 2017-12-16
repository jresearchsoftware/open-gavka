package org.jresearch.gavka.gwt.core.client.module.message.widget;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.fusesource.restygwt.client.Method;
import org.jresearch.commons.gwt.client.mvc.AbstractMethodCallback;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.commons.gwt.client.tool.GwtDeferredTask;
import org.jresearch.commons.gwt.client.widget.Uis;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.gwt.core.client.module.message.srv.GavkaMessageService;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MessagePage extends Composite {

	private static final Joiner JOIN_APPENDERS = Joiner.on(", "); //$NON-NLS-1$

	// @formatter:off
	interface Binder extends UiBinder<DockLayoutPanel, MessagePage> {/* nothing */}
	// @formatter:on

	@UiField(provided = true)
	DataGrid<Message> loggers;
	@UiField(provided = true)
	SimplePager pager;
	@UiField
	TextBox filter;
	@UiField
	CheckBox inherited;

	private final GwtDeferredTask refreshTask = new GwtDeferredTask(this::updateData);
	private final GavkaMessageService srv;
	private final Bus bus;
	private String lastFilterValue = Uis.NOTHING;
	private boolean lastInheritedValue = true;

	@Inject
	protected MessagePage(@Nonnull final Binder binder, final GavkaMessageService srv, final Bus bus) {
		this.srv = srv;
		this.bus = bus;
		loggers = createDatagrid();
		initWidget(binder.createAndBindUi(this));
		setStyleName("AppenderPage");
	}

	private void updateData() {
		if (lastInheritedValue != getInherited() || !Objects.equal(lastFilterValue, getFilter())) {
			lastFilterValue = getFilter();
			lastInheritedValue = getInherited();
			refresh();
		}
	}

	@UiHandler("inherited")
	void onInherited(@SuppressWarnings("unused") final ValueChangeEvent<Boolean> event) {
		refreshTask.defer(750);
	}

	@UiHandler("filter")
	void onChange(@SuppressWarnings("unused") final KeyUpEvent event) {
		refreshTask.defer(750);
	}

	@UiHandler("filter")
	void onValueChange(@SuppressWarnings("unused") final ValueChangeEvent<String> event) {
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
				final Range range = display.getVisibleRange();
				final int start = range.getStart();
				srv.get(getFilter(), new AbstractMethodCallback<List<Message>>(bus) {
					@Override
					public void onSuccess(final Method method, final List<Message> result) {
						dataGrid.setRowCount(result.size());
						updateRowData(start, result.subList(start, result.size()));
					}
				});
			}
		}.addDataDisplay(dataGrid);

		pager = new SimplePager();
		pager.setDisplay(dataGrid);

		return dataGrid;

	}

	protected boolean getInherited() {
		return Optional.ofNullable(inherited).map(CheckBox::getValue).orElse(Boolean.TRUE).booleanValue();
	}

	@Nonnull
	protected String getFilter() {
		if (filter != null) {
			final String value = filter.getValue();
			if (value != null) {
				return value;
			}
		}
		return "";
	}

	public void refresh() {
		loggers.setRowData(0, ImmutableList.<Message> of());
		loggers.setVisibleRangeAndClearData(new Range(0, loggers.getPageSize()), true);
	}

}
