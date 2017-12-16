package org.jresearch.gavka.gwt.core.client.module.appender.widget;

import java.util.List;

import javax.annotation.Nonnull;

import org.fusesource.restygwt.client.Method;
import org.jresearch.commons.gwt.client.mvc.AbstractMethodCallback;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.commons.gwt.client.tool.DeferredTask;
import org.jresearch.commons.gwt.client.widget.Uis;
import org.jresearch.gavka.domain.LogUiAppender;
import org.jresearch.gavka.gwt.core.client.module.appender.srv.LogUiAppenderService;

import com.google.common.collect.ImmutableList;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AppenderPage extends Composite {

	// @formatter:off
	interface Binder extends UiBinder<DockLayoutPanel, AppenderPage> {/* nothing */}
	// @formatter:on

	@UiField(provided = true)
	DataGrid<LogUiAppender> appenders;
	@UiField(provided = true)
	SimplePager pager;
	@UiField
	ListBox type;

	private final DeferredTask loadTask = new DeferredTask() {

		private final String lastFilterValue = Uis.NOTHING;

		@Override
		public void run() {
			if (type.getItemCount() == 0) {
				loadTypes();
			}
		}
	};
	private final LogUiAppenderService srv;
	@Nonnull
	private final Bus bus;

	@Inject
	protected AppenderPage(@Nonnull final Binder binder, final LogUiAppenderService srv, final @Nonnull Bus bus) {
		this.srv = srv;
		this.bus = bus;
		appenders = createDatagrid();
		initWidget(binder.createAndBindUi(this));
		setStyleName("AppenderPage");
		loadTypes();
	}

	private void loadTypes() {
		loadTask.cancel();
		srv.types(new AbstractMethodCallback<List<String>>(bus) {
			@Override
			public void onSuccess(final Method method, final List<String> response) {
				if (response.isEmpty()) {
					loadTask.defer(1000);
				} else {
					for (final String string : response) {
						type.addItem(string);
					}
				}
			}
		});
	}

	private DataGrid<LogUiAppender> createDatagrid() {
		final DataGrid<LogUiAppender> dataGrid = new DataGrid<>(30);

		// Name
		final TextColumn<LogUiAppender> colName = new TextColumn<LogUiAppender>() {
			@Override
			public String getValue(final LogUiAppender object) {
				return object.getName();
			}
		};

		// ClassName
		final TextColumn<LogUiAppender> colClassName = new TextColumn<LogUiAppender>() {
			@Override
			public String getValue(final LogUiAppender object) {
				return object.getClassName();
			}
		};

		dataGrid.addColumn(colName, "Name");
		dataGrid.setColumnWidth(colName, 90, Unit.PCT);
		dataGrid.addColumn(colClassName, "Type");
		dataGrid.setColumnWidth(colClassName, 30, Unit.PCT);

		new AsyncDataProvider<LogUiAppender>() {
			@Override
			protected void onRangeChanged(final HasData<LogUiAppender> display) {
				final Range range = display.getVisibleRange();
				final int start = range.getStart();
				srv.get(new AbstractMethodCallback<List<LogUiAppender>>(bus) {
					@Override
					public void onSuccess(final Method method, final List<LogUiAppender> result) {
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

	public void refresh() {
		appenders.setRowData(0, ImmutableList.<LogUiAppender> of());
		appenders.setVisibleRangeAndClearData(new Range(0, appenders.getPageSize()), true);
	}

}
