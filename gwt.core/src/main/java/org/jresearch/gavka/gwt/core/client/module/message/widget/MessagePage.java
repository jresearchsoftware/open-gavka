package org.jresearch.gavka.gwt.core.client.module.message.widget;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.fusesource.restygwt.client.Method;
import org.jresearch.commons.gwt.client.mvc.AbstractMethodCallback;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.commons.gwt.client.tool.GwtDeferredTask;
import org.jresearch.commons.gwt.client.widget.Uis;
import org.jresearch.gavka.domain.LogUiLevel;
import org.jresearch.gavka.domain.LogUiLogger;
import org.jresearch.gavka.gwt.core.client.module.message.srv.LogUiLoggerService;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
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
	DataGrid<LogUiLogger> loggers;
	@UiField(provided = true)
	SimplePager pager;
	@UiField
	TextBox filter;
	@UiField
	CheckBox inherited;

	private final GwtDeferredTask refreshTask = new GwtDeferredTask(this::updateData);
	private final LogUiLoggerService srv;
	private final Bus bus;
	private String lastFilterValue = Uis.NOTHING;
	private boolean lastInheritedValue = true;

	@Inject
	protected MessagePage(@Nonnull final Binder binder, final LogUiLoggerService srv, final Bus bus) {
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

	private DataGrid<LogUiLogger> createDatagrid() {
		final DataGrid<LogUiLogger> dataGrid = new DataGrid<>(30);

		// Name
		final TextColumn<LogUiLogger> colLogger = new TextColumn<LogUiLogger>() {
			@Override
			public String getValue(final LogUiLogger object) {
				return object.getName();
			}
		};

		// EffectiveLevel
		final TextColumn<LogUiLogger> colEffLevel = new TextColumn<LogUiLogger>() {
			@Override
			public String getValue(final LogUiLogger object) {
				return LogUiLevel.stringValue(object.getEffectiveLevel());
			}
		};

		// Level
		final List<String> levels = LogUiLevel.stringValues();
		final SelectionCell levelCell = new SelectionCell(levels);
		final Column<LogUiLogger, String> colLevel = new Column<LogUiLogger, String>(levelCell) {
			@Override
			public String getValue(final LogUiLogger object) {
				return LogUiLevel.stringValue(object.getLevel());
			}
		};
		colLevel.setFieldUpdater(new FieldUpdater<LogUiLogger, String>() {
			@Override
			public void update(final int index, final LogUiLogger object, final String value) {
				final LogUiLevel original = object.getLevel();
				final LogUiLevel newLevel = LogUiLevel.value(value);
				if (!Objects.equal(original, newLevel)) {
					object.setLevel(newLevel);
					srv.set(object, new AbstractMethodCallback<Boolean>(bus) {
						@Override
						public void onSuccess(final Method method, final Boolean response) {
							if (response != null && response.booleanValue()) {
								refresh();
							} else {
								object.setLevel(original);
							}
						}

						@Override
						public void onFailure(final Method method, final Throwable caught) {
							object.setLevel(original);
							super.onFailure(method, caught);
						}
					});
				}
			}
		});

		// Additive
		final Column<LogUiLogger, Boolean> cplAdditive = new Column<LogUiLogger, Boolean>(new CheckboxCell(false, false)) {
			@Override
			public Boolean getValue(final LogUiLogger object) {
				return Boolean.valueOf(object.isAdditive());
			}
		};

		dataGrid.addColumn(colLogger, "Name");
		dataGrid.setColumnWidth(colLogger, 90, Unit.PCT);
		dataGrid.addColumn(colEffLevel, "Effective level");
		dataGrid.setColumnWidth(colEffLevel, 100, Unit.PX);
		dataGrid.addColumn(colLevel, "Level");
		dataGrid.setColumnWidth(colLevel, 120, Unit.PX);
		dataGrid.addColumn(cplAdditive, "Additive");
		dataGrid.setColumnWidth(cplAdditive, 80, Unit.PX);

		new AsyncDataProvider<LogUiLogger>() {
			@Override
			protected void onRangeChanged(final HasData<LogUiLogger> display) {
				final Range range = display.getVisibleRange();
				final int start = range.getStart();
				srv.get(getFilter(), getInherited(), new AbstractMethodCallback<List<LogUiLogger>>(bus) {
					@Override
					public void onSuccess(final Method method, final List<LogUiLogger> result) {
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
		loggers.setRowData(0, ImmutableList.<LogUiLogger> of());
		loggers.setVisibleRangeAndClearData(new Range(0, loggers.getPageSize()), true);
	}

}
