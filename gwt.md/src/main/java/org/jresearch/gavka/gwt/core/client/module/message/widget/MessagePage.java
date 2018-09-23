package org.jresearch.gavka.gwt.core.client.module.message.widget;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import gwt.material.design.client.base.viewport.Resolution;
import gwt.material.design.client.base.viewport.ViewPort;
import gwt.material.design.client.data.ListDataSource;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.pager.MaterialDataPager;
import gwt.material.design.client.ui.table.MaterialDataTable;
import gwt.material.design.client.ui.table.cell.TextColumn;

@SuppressWarnings("nls")
@Singleton
public class MessagePage extends Composite {

	private static final int AUTOREFRESH_TIME = 10_000;

	// @formatter:off
	interface Binder extends UiBinder<MaterialRow, MessagePage> {/* nothing */}
	// @formatter:on

	@UiField
	MaterialDataTable<Data> table;

	private final MaterialDataPager<Data> pager = new MaterialDataPager<>();

	private ListDataSource<Data> dataSource;

	@Inject
	protected MessagePage(@Nonnull final Binder binder) {
		initWidget(binder.createAndBindUi(this));
		// Generate 20 categories
		// int rowIndex = 1;
		final List<Data> people = new ArrayList<>();
		for (int i = 1; i <= 100; i++) {
			people.add(new Data("Field 1 " + i, "Field 2 " + i));
		}

		dataSource = new ListDataSource<>();
		dataSource.add(0, people);

		/*
		 * Uncomment to make use of listbox page selection instead of integerbox
		 */
		/* pager.setPageSelection(new PageListBox()); */
		pager.setTable(table);
		pager.setDataSource(dataSource);
		pager.setLimit(20);

		table.setVisibleRange(1, 10);
		table.add(pager);

		// Now we will add our tables columns.
		// There are a number of methods that can provide custom column
		// configurations.

		table.addColumn(new TextColumn<Data>() {
			@Override
			public Comparator<? super RowComponent<Data>> sortComparator() {
				return (o1, o2) -> o1.getData().getField1().compareToIgnoreCase(o2.getData().getField1());
			}

			@Override
			public String getValue(final Data object) {
				return object.getField1();
			}
		}, "First");

		table.addColumn(new TextColumn<Data>() {
			@Override
			public Comparator<? super RowComponent<Data>> sortComparator() {
				return (o1, o2) -> o1.getData().getField2().compareToIgnoreCase(o2.getData().getField2());
			}

			@Override
			public String getValue(final Data object) {
				return object.getField2();
			}
		}, "Second");

		ViewPort.when(Resolution.ALL_MOBILE).then(param1 -> {
			table.setHeight("60vh");
		}, viewPort -> {
			table.setHeight("100%");
			return false;
		});
	}

}
