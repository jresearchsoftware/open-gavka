package org.jresearch.gavka.gwt.core.client.module.message.widget;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.jresearch.gavka.gwt.core.client.module.message.widget.collapsible.CustomerCollapsible;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import gwt.material.design.client.data.ListDataSource;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialSplashScreen;

@SuppressWarnings("nls")
@Singleton
public class MessagePage extends Composite {

	private static final int AUTOREFRESH_TIME = 10_000;

	// @formatter:off
	interface Binder extends UiBinder<MaterialPanel, MessagePage> {/* nothing */}
	// @formatter:on

//	@UiField
//	MaterialDataTable<Data> table;

//	private final MaterialDataPager<Data> pager = new MaterialDataPager<>();

	@UiField
	MaterialNavBar appNav, searchNav;

	@UiField
	MaterialSearch search;

	@UiField
	MaterialIcon btnSearch;

	@UiField
	MaterialCollapsible starredColaps, frequentColaps;

//	    @UiField
//	    ProfileOverlay profileOverlay;

	@UiField
	MaterialSplashScreen splash;

	private final ListDataSource<Data> dataSource;

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
//		pager.setTable(table);
//		pager.setDataSource(dataSource);
//		pager.setLimit(20);

//		table.setVisibleRange(1, 10);
//		table.add(pager);

		// Now we will add our tables columns.
		// There are a number of methods that can provide custom column
		// configurations.

//		table.addColumn(new TextColumn<Data>() {
//			@Override
//			public Comparator<? super RowComponent<Data>> sortComparator() {
//				return (o1, o2) -> o1.getData().getField1().compareToIgnoreCase(o2.getData().getField1());
//			}
//
//			@Override
//			public String getValue(final Data object) {
//				return object.getField1();
//			}
//		}, "First");

//		table.addColumn(new TextColumn<Data>() {
//			@Override
//			public Comparator<? super RowComponent<Data>> sortComparator() {
//				return (o1, o2) -> o1.getData().getField2().compareToIgnoreCase(o2.getData().getField2());
//			}
//
//			@Override
//			public String getValue(final Data object) {
//				return object.getField2();
//			}
//		}, "Second");

//		ViewPort.when(Resolution.ALL_MOBILE).then(param1 -> {
//			table.setHeight("60vh");
//		}, viewPort -> {
//			table.setHeight("100%");
//			return false;
//		});
		populateUsers(DataHelper.getAllUsers());
	}

	/**
	 * Populate the starred and frequent collapsibles
	 *
	 * @param allUsers
	 */
	private void populateUsers(final List<UserDTO> allUsers) {
		starredColaps.clear();
		frequentColaps.clear();
		allUsers.stream().filter(UserDTO::isStarred).forEach(dto -> {
			starredColaps.add(new CustomerCollapsible(dto));
		});
		for (final UserDTO dto : allUsers) {
			frequentColaps.add(new CustomerCollapsible(dto));
		}
	}
}
