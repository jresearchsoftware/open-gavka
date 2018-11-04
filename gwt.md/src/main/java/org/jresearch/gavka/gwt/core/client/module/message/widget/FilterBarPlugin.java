package org.jresearch.gavka.gwt.core.client.module.message.widget;

import static org.jboss.gwt.elemento.core.Elements.*;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nonnull;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.events.SearchClearedEvent;
import org.dominokit.domino.ui.datatable.events.TableEvent;
import org.dominokit.domino.ui.datatable.model.Category;
import org.dominokit.domino.ui.datatable.model.Filter;
import org.dominokit.domino.ui.datatable.model.SearchContext;
import org.dominokit.domino.ui.datatable.plugins.DataTablePlugin;
import org.dominokit.domino.ui.datepicker.DateBox;
import org.dominokit.domino.ui.forms.FormElement;
import org.dominokit.domino.ui.forms.Select;
import org.dominokit.domino.ui.forms.SelectOption;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.forms.ValueBox;
import org.dominokit.domino.ui.grid.Column;
import org.dominokit.domino.ui.grid.Row;
import org.dominokit.domino.ui.grid.Row_12;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.style.Style;
import org.dominokit.domino.ui.timepicker.ClockStyle;
import org.dominokit.domino.ui.timepicker.TimeBox;
import org.dominokit.domino.ui.timepicker.TimeBox.PickerStyle;
import org.fusesource.restygwt.client.REST;
import org.gwtproject.timer.client.Timer;
import org.jboss.gwt.elemento.core.builder.HtmlContentBuilder;
import org.jresearch.commons.gwt.client.mvc.GwtMethodCallback;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.domain.KeyFormat;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.domain.MessageFormat;
import org.jresearch.gavka.gwt.core.client.module.message.srv.GavkaMessageRestService;

import com.google.inject.Inject;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;

public class FilterBarPlugin implements DataTablePlugin<Message> {

	private final HtmlContentBuilder<HTMLDivElement> div = div()
			.css("header")
			.style("padding-bottom: 5px;");

	private final int autoSearchDelay = 20000;
	private DataTable<Message> dataTable;
	private final Select<String> topicBox;
	private final Select<KeyFormat> keyFormatBox;
	private final Select<MessageFormat> messageFormatBox;
	private final TextBox keyBox;
	private final DateBox dateBox;
	private final TimeBox timeBox;
//	private boolean autoSearch = true;
	private final Timer autoSearchTimer;
	private final EventListener autoSearchEventListener = this::autoSearch;

	@Inject
	public FilterBarPlugin(@Nonnull final GavkaMessageRestService srv, @Nonnull final Bus bus) {
		REST.withCallback(new GwtMethodCallback<>(bus, this::addTopics)).call(srv).topics();

		autoSearchTimer = new Timer() {
			@Override
			public void run() {
				doSearch();
			}
		};

		final Icon clearIconKey = Icons.ALL.clear()
				.setTooltip("Clear")
				.style()
				.setCursor("pointer")
				.get();
		final Icon clearIconDate = clearIconKey.copy();
		final Icon clearIconTime = clearIconKey.copy();
		final Icon clearIconTopic = clearIconKey.copy();
		final Icon clearIconKeyFormat = clearIconKey.copy();
		final Icon clearIconMassageFormat = clearIconKey.copy();

		topicBox = Select.<String>create("Topic")
				.setRightAddon(clearIconTopic)
				.styler(FilterBarPlugin::zerroBottomMargin);
		keyBox = TextBox.create("Key")
				.setRightAddon(clearIconKey)
				.styler(FilterBarPlugin::zerroBottomMargin);
		dateBox = DateBox.create("Date (UTC)")
				.setRightAddon(clearIconDate)
				.styler(FilterBarPlugin::zerroBottomMargin);
		timeBox = TimeBox.create("Time (UTC)", null)
				.setPickerStyle(PickerStyle.MODAL)
				.setRightAddon(clearIconTime)
				.styler(FilterBarPlugin::zerroBottomMargin);
		timeBox.getTimePicker().setClockStyle(ClockStyle._24);
		timeBox.clear();

		clearIconKey.addClickListener(e -> clearBox(keyBox, e));
		clearIconDate.addClickListener(e -> clearBox(dateBox, e));
		clearIconTime.addClickListener(e -> clearBox(timeBox, e));
		clearIconTopic.addClickListener(e -> clearBox(topicBox, e));

		final Row_12 row1 = Row.create()
				.addColumn(Column.span3().appendChild(topicBox))
				.addColumn(Column.span3().appendChild(keyBox))
				.addColumn(Column.span3().appendChild(dateBox))
				.addColumn(Column.span3().appendChild(timeBox))
				.styler(FilterBarPlugin::zerroBottomMargin);

		keyFormatBox = Select.<KeyFormat>create("Key format")
				.setRightAddon(clearIconKeyFormat)
				.styler(FilterBarPlugin::zerroBottomMargin);
		messageFormatBox = Select.<MessageFormat>create("Message format")
				.setRightAddon(clearIconMassageFormat)
				.styler(FilterBarPlugin::zerroBottomMargin);

		EnumSet.allOf(KeyFormat.class).stream().map(e -> SelectOption.create(e, e.name())).forEach(keyFormatBox::appendChild);
		EnumSet.allOf(MessageFormat.class).stream().map(e -> SelectOption.create(e, e.name())).forEach(messageFormatBox::appendChild);

		clearIconKeyFormat.addClickListener(e -> clearBox(keyFormatBox, e));
		clearIconMassageFormat.addClickListener(e -> clearBox(messageFormatBox, e));

		final MdiIcon clearFiltersIcon = Icons.MDI_ICONS.filter_remove_mdi()
				.setTooltip("Clear filters")
				.size18()
				.clickable()
				.addClickListener(this::clearFilters);

		final Row_12 row2 = Row.create()
				.addColumn(Column.span3().appendChild(keyFormatBox))
				.addColumn(Column.span3().appendChild(messageFormatBox))
				.addColumn(Column.span1().offset(10).appendChild(Button.create("Search")))
				.addColumn(Column.span1().offset(11).appendChild(clearFiltersIcon))
				.styler(FilterBarPlugin::zerroBottomMargin);

		div.add(row1).add(row2);

//		setAutoSearch(true);
	}

	private void clearFilters(final Event evt) {
		dataTable.getSearchContext().clear().fireSearchEvent();
	}

	public void addTopics(final List<String> topics) {
		topics.stream().map(t -> SelectOption.create(t, t)).forEach(topicBox::appendChild);
	}

	private static void zerroBottomMargin(final Style style) {
		style.setMarginBottom("0");
	}

	@Override
	public void onBeforeAddTable(final DataTable<Message> dt) {
		dataTable = dt;
		dt.addTableEventListner(SearchClearedEvent.SEARCH_EVENT_CLEARED, this);
		dt.asElement().appendChild(div.asElement());
	}

	private void clearBox(final FormElement<?, ?> box, final Event evt) {
		evt.stopPropagation();
		box.clear();
		autoSearchTimer.cancel();
		doSearch();
	}

	private void autoSearch(final Event evt) {
		autoSearchTimer.cancel();
		autoSearchTimer.schedule(autoSearchDelay);
	}

//	public boolean isAutoSearch() {
//		return autoSearch;
//	}

//	public MessageFilterAction<T> setAutoSearch(final boolean autoSearch) {
//		this.autoSearch = autoSearch;
//
//		if (autoSearch) {
//			textBox.addEventListener("input", autoSearchEventListener);
//		} else {
//			textBox.removeEventListener("input", autoSearchEventListener);
//			autoSearchTimer.cancel();
//		}
//
//		textBox.addEventListener(EventType.keypress.getName(), evt -> {
//			if (ElementUtil.isEnterKey(Js.uncheckedCast(evt))) {
//				doSearch();
//			}
//		});
//
//		return this;
//	}

//	public int getAutoSearchDelay() {
//		return autoSearchDelay;
//	}

//	public void setAutoSearchDelay(final int autoSearchDelayInMillies) {
//		this.autoSearchDelay = autoSearchDelayInMillies;
//	}

	private void doSearch() {
		final SearchContext<Message> searchContext = dataTable.getSearchContext();
		final Category search = Category.SEARCH;
		searchContext.removeByCategory(search);
		searchContext.add(Filter.create("*", keyBox.getValue(), Category.SEARCH))
				.fireSearchEvent();
	}

	@Override
	public void handleEvent(final TableEvent event) {
		if (SearchClearedEvent.SEARCH_EVENT_CLEARED.equals(event.getType())) {
			handleClearEvent(keyBox);
			handleClearEvent(dateBox);
			handleClearEvent(timeBox);
			messageFormatBox.clear();
			keyFormatBox.clear();
			topicBox.clear();
		}
	}

	private static void handleClearEvent(final ValueBox box) {
		box.pauseChangeHandlers();
		box.clear();
		box.resumeChangeHandlers();
	}

//	@Override
//	public Node asElement(final DataTable<T> dataTable) {
//		this.dataTable = dataTable;
//		dataTable.addTableEventListner(SearchClearedEvent.SEARCH_EVENT_CLEARED, this);
//		return element.asElement();
//	}
}
