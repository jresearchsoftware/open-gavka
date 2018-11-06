package org.jresearch.gavka.gwt.core.client.module.message.widget;

import static org.jboss.gwt.elemento.core.Elements.*;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.events.SearchClearedEvent;
import org.dominokit.domino.ui.datatable.events.TableEvent;
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
import org.jboss.gwt.elemento.core.EventType;
import org.jboss.gwt.elemento.core.builder.HtmlContentBuilder;
import org.jresearch.commons.gwt.client.mvc.GwtMethodCallback;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.commons.gwt.client.tool.Dates;
import org.jresearch.commons.gwt.client.widget.Uis;
import org.jresearch.commons.gwt.shared.model.time.GwtLocalDateModel;
import org.jresearch.commons.gwt.shared.model.time.GwtLocalDateTimeModel;
import org.jresearch.commons.gwt.shared.model.time.GwtLocalTimeModel;
import org.jresearch.gavka.domain.KeyFormat;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.domain.MessageFormat;
import org.jresearch.gavka.gwt.core.client.module.message.srv.GavkaMessageRestService;
import org.jresearch.gavka.rest.api.MessageParameters;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.user.client.TakesValue;
import com.google.inject.Inject;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;

public class FilterBarPlugin implements DataTablePlugin<Message> {

	private final HtmlContentBuilder<HTMLDivElement> div = div()
			.css("header")
			.style("padding-bottom: 5px;");

	private int autoSearchDelay = 5_000;
	@Nonnull
	private DataTable<Message> dataTable;
	@Nonnull
	private final Select<String> topicBox;
	@Nonnull
	private final Select<KeyFormat> keyFormatBox;
	@Nonnull
	private final Select<MessageFormat> messageFormatBox;
	@Nonnull
	private final TextBox keyBox;
	@Nonnull
	private final DateBox dateBox;
	@Nonnull
	private final TimeBox timeBox;
	private boolean autoSearch = true;
	private final Timer autoSearchTimer;
	private final EventListener autoSearchEventListener = this::autoSearch;
	@Nonnull
	private Button searchBtn;

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
				.setCursor(Cursor.POINTER.getCssName())
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
		clearIconTopic.addClickListener(e -> clearSelect(topicBox, e));

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
		keyFormatBox.selectAt(0);
		EnumSet.allOf(MessageFormat.class).stream().map(e -> SelectOption.create(e, e.name())).forEach(messageFormatBox::appendChild);
		messageFormatBox.selectAt(0);

		clearIconKeyFormat.addClickListener(e -> clearSelect(keyFormatBox, e));
		clearIconMassageFormat.addClickListener(e -> clearSelect(messageFormatBox, e));

		final MdiIcon clearFiltersIcon = Icons.MDI_ICONS.filter_remove_mdi()
				.setTooltip("Clear filters")
				.size18()
				.clickable()
				.addClickListener(this::clearFilters);

		final Row_12 row2 = Row.create()
				.addColumn(Column.span3().appendChild(keyFormatBox))
				.addColumn(Column.span3().appendChild(messageFormatBox))
				.addColumn(Column.span1().offset(10).appendChild((searchBtn = Button.create("Search")).disable().addClickListener(this::doSearch)))
				.addColumn(Column.span1().offset(11).appendChild(clearFiltersIcon))
				.styler(FilterBarPlugin::zerroBottomMargin);

		div.add(row1).add(row2);

		setAutoSearch(true);
	}

	private void doSearch(final Event evt) {
		doSearch();
	}

	private void clearFilters(final Event evt) {
		dataTable.getSearchContext().clear();
		doSearch();
	}

	public void addTopics(final List<String> topics) {
		topics.stream().map(t -> SelectOption.create(t, t)).forEach(topicBox::appendChild);
		topicBox.selectAt(0);
		searchBtn.enable();
		doSearch();
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

	private void clearSelect(final Select<?> box, final Event evt) {
		evt.stopPropagation();
		box.selectAt(0);
		autoSearchTimer.cancel();
		doSearch();
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

	public boolean isAutoSearch() {
		return autoSearch;
	}

	public FilterBarPlugin setAutoSearch(final boolean autoSearch) {
		this.autoSearch = autoSearch;

		if (autoSearch) {
			keyBox.addEventListener(EventType.input.getName(), autoSearchEventListener);
			dateBox.addEventListener(EventType.input.getName(), autoSearchEventListener);
			timeBox.addEventListener(EventType.input.getName(), autoSearchEventListener);
			messageFormatBox.addEventListener(EventType.input.getName(), autoSearchEventListener);
			keyFormatBox.addEventListener(EventType.input.getName(), autoSearchEventListener);
			topicBox.addEventListener(EventType.input.getName(), autoSearchEventListener);
		} else {
			autoSearchTimer.cancel();
			keyBox.removeEventListener(EventType.input.getName(), autoSearchEventListener);
			dateBox.removeEventListener(EventType.input.getName(), autoSearchEventListener);
			timeBox.removeEventListener(EventType.input.getName(), autoSearchEventListener);
			messageFormatBox.removeEventListener(EventType.input.getName(), autoSearchEventListener);
			keyFormatBox.removeEventListener(EventType.input.getName(), autoSearchEventListener);
			topicBox.removeEventListener(EventType.input.getName(), autoSearchEventListener);
		}

		return this;
	}

	public int getAutoSearchDelay() {
		return autoSearchDelay;
	}

	public void setAutoSearchDelay(final int autoSearchDelayInMillies) {
		this.autoSearchDelay = autoSearchDelayInMillies;
	}

	private void doSearch() {
		dataTable.getSearchContext().fireSearchEvent();
	}

	@Override
	public void handleEvent(final TableEvent event) {
		if (SearchClearedEvent.SEARCH_EVENT_CLEARED.equals(event.getType())) {
			handleClearEvent(keyBox);
			handleClearEvent(dateBox);
			handleClearEvent(timeBox);
			messageFormatBox.selectAt(0);
			keyFormatBox.selectAt(0);
			topicBox.selectAt(0);
		}
	}

	private static void handleClearEvent(final ValueBox<?, ?, ?> box) {
		box.pauseChangeHandlers();
		box.clear();
		box.resumeChangeHandlers();
	}

	public MessageParameters getMessageParameters() {
		final MessageParameters messageParameters = new MessageParameters();
		messageParameters.setTopic(getTopic());
		messageParameters.setFrom(getFrom().orElse(null));
		messageParameters.setKey(getKeyValue());
		messageParameters.setKeyFormat(getKeyFormat());
		messageParameters.setMessageFormat(getMessageFormat());
		return messageParameters;
	}

	@SuppressWarnings("null")
	@Nonnull
	private String getTopic() {
		return Optional.of(topicBox)
				.map(Select::getValue)
				.orElse(Uis.NOTHING);
	}

	@SuppressWarnings("null")
	@Nonnull
	private Optional<GwtLocalDateTimeModel> getFrom() {
		final GwtLocalDateModel gwtDate = Optional.of(dateBox)
				.map(TakesValue::getValue)
				.map(Dates::toLocalDate)
				.orElse(null);
		final GwtLocalTimeModel gwtTime = Optional.of(timeBox)
				.map(TakesValue::getValue)
				.map(Dates::toLocalTime)
				.orElseGet(GwtLocalTimeModel::new);
		return Optional.ofNullable(gwtDate == null ? null : new GwtLocalDateTimeModel(gwtDate, gwtTime));
	}

	private String getKeyValue() {
		return Optional.of(keyBox)
				.map(TakesValue::getValue)
				.orElse(Uis.NOTHING);
	}

	private MessageFormat getMessageFormat() {
		return Optional.of(messageFormatBox)
				.map(Select::getValue)
				.orElse(MessageFormat.values()[0]);
	}

	private KeyFormat getKeyFormat() {
		return Optional
				.of(keyFormatBox)
				.map(Select::getValue)
				.orElse(KeyFormat.values()[0]);
	}

}
