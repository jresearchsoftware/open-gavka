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

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.table.MaterialDataTable;
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
	MaterialDataTable<Data> table;

//	private final MaterialDataPager<Data> pager = new MaterialDataPager<>();

//	@UiField
//	MaterialCollapsible records;

//	    @UiField
//	    ProfileOverlay profileOverlay;

	@Inject
	protected MessagePage(@Nonnull final Binder binder) {
		initWidget(binder.createAndBindUi(this));
		final List<Data> people = new ArrayList<>();
		for (int i = 1; i <= 100; i++) {
			people.add(new Data("Field 1 " + i, "Field 2 " + i));
		}

		table.setRowData(0, people);

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

//		ViewPort.when(Resolution.ALL_MOBILE).then(param1 -> {
//			table.setHeight("60vh");
//		}, viewPort -> {
//			table.setHeight("100%");
//			return false;
//		});
//		populateUsers(DataHelper.getAllUsers());
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

//	/**
//	 * Populate the starred and frequent collapsibles
//	 *
//	 * @param allUsers
//	 */
//	private void populateUsers(final List<UserDTO> allUsers) {
//		records.clear();
//		allUsers.stream().map(CustomerCollapsible::new).forEach(records::add);
//	}
}
