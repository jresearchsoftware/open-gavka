package org.jresearch.gavka.gwt.core.client.module.message.widget;

import static org.jboss.gwt.elemento.core.Elements.*;
import static org.jboss.gwt.elemento.core.InputType.*;

import java.util.Date;
import java.util.EnumSet;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.events.SearchClearedEvent;
import org.dominokit.domino.ui.datatable.events.TableEvent;
import org.dominokit.domino.ui.datatable.plugins.DataTablePlugin;
import org.dominokit.domino.ui.datepicker.DateBox;
import org.dominokit.domino.ui.datepicker.DatePicker.DateSelectionHandler;
import org.dominokit.domino.ui.forms.Select;
import org.dominokit.domino.ui.forms.Select.SelectionHandler;
import org.dominokit.domino.ui.forms.SelectOption;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.forms.ValueBox;
import org.dominokit.domino.ui.grid.Column;
import org.dominokit.domino.ui.grid.Row;
import org.dominokit.domino.ui.grid.Row_12;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.style.Style;
import org.dominokit.domino.ui.timepicker.TimeBox;
import org.dominokit.domino.ui.timepicker.TimePicker;
import org.dominokit.domino.ui.timepicker.TimePicker.TimeSelectionHandler;
import org.dominokit.domino.ui.utils.HasChangeHandlers.ChangeHandler;
import org.gwtproject.i18n.shared.DateTimeFormatInfo;
import org.gwtproject.timer.client.Timer;
import org.jboss.gwt.elemento.core.EventType;
import org.jboss.gwt.elemento.core.builder.HtmlContentBuilder;
import org.jboss.gwt.elemento.core.builder.InputBuilder;
import org.jresearch.commons.gwt.client.tool.Dates;
import org.jresearch.commons.gwt.client.widget.Uis;
import org.jresearch.commons.gwt.shared.model.time.GwtLocalDateModel;
import org.jresearch.commons.gwt.shared.model.time.GwtLocalDateTimeModel;
import org.jresearch.commons.gwt.shared.model.time.GwtLocalTimeModel;
import org.jresearch.gavka.domain.KeyFormat;
import org.jresearch.gavka.domain.Message;
import org.jresearch.gavka.domain.MessageFormat;
import org.jresearch.gavka.rest.api.ImmutableMessageParameters;
import org.jresearch.gavka.rest.api.MessageParameters;
import org.jresearch.gavka.rest.data.GafkaCoordinates;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLFormElement;
import elemental2.dom.HTMLInputElement;

public class FilterBarPlugin implements DataTablePlugin<Message> {

	public static final String INHERIT = "inherit"; //$NON-NLS-1$

	private final HtmlContentBuilder<HTMLDivElement> div = div()
			.css("header")
			.style("padding-bottom: 5px;");

	private int autoSearchDelay = 5_000;

	@Nonnull
	private DataTable<Message> dataTable;
	@Nonnull
	private final Select<KeyFormat> keyFormatBox;
	@Nonnull
	private final Select<MessageFormat> messageFormatBox;
	@Nonnull
	private final TextBox keyBox;
	@Nonnull
	private final TextBox valuePatternBox;
	@Nonnull
	private final DateBox dateBox;
	@Nonnull
	private final TimeBox timeBox;
	private boolean autoSearch = true;
	private final Timer autoSearchTimer;
	private final EventListener autoSearchEventListener = this::autoSearch;
	private final DateSelectionHandler dateHandler = this::autoSearch;
	private final TimeSelectionHandler timeHandler = this::autoSearch;
	private final ChangeHandler<String> stringHandler = this::autoSearch;
	private final SelectionHandler<KeyFormat> keySelHandler = this::autoSearch;
	private final SelectionHandler<MessageFormat> msgSelHandler = this::autoSearch;

	@Nonnull
	private Button searchBtn;

	private HTMLInputElement hiddenFrom;
	private HTMLInputElement hiddenTopic;
	private HTMLInputElement hiddenKeyFormat;
	private HTMLInputElement hiddenMessageFormat;
	private HTMLInputElement hiddenConnectionId;

	@Nonnull
	private HtmlContentBuilder<HTMLFormElement> exportForm;
	@Nonnull
	private final GafkaCoordinates gafkaCoordinates;

	@SuppressWarnings("null")
	public FilterBarPlugin(@Nonnull final GafkaCoordinates gafkaCoordinates) {
		this.gafkaCoordinates = gafkaCoordinates;
		autoSearchTimer = new Timer() {
			@Override
			public void run() {
				doSearch();
			}
		};

		keyBox = FilterTextBox.create("Key", "key", this::clear);
		valuePatternBox = FilterTextBox.create("Value pattern", "valuePattern", this::clear);
		dateBox = FilterDateBox.create("Date (UTC)", this::clear);
		timeBox = FilterTimeBox.create("Time (UTC)", this::clear);

		final Row_12 row1 = Row.create()
				.addColumn(Column.span4().appendChild(keyBox))
				.addColumn(Column.span4().appendChild(valuePatternBox))
				.addColumn(Column.span4().appendChild(dateBox))
				.styler(FilterBarPlugin::zerroBottomMargin);

		keyFormatBox = FilterSelect.<KeyFormat>create("Key format", "keyFormat", this::clear);
		messageFormatBox = FilterSelect.<MessageFormat>create("Message format", "messageFormat", this::clear);

		EnumSet.allOf(KeyFormat.class).stream().map(e -> SelectOption.create(e, e.name())).forEach(keyFormatBox::appendChild);
		keyFormatBox.selectAt(0);
		EnumSet.allOf(MessageFormat.class).stream().map(e -> SelectOption.create(e, e.name())).forEach(messageFormatBox::appendChild);
		messageFormatBox.selectAt(0);

		final MdiIcon clearFiltersIcon = Icons.MDI_ICONS.filter_remove_mdi()
				.setTooltip("Clear filters")
				.size18()
				.clickable()
				.addClickListener(this::clearFilters);

		final Row_12 row2 = Row.create()
				.addColumn(Column.span4().appendChild(keyFormatBox))
				.addColumn(Column.span4().appendChild(messageFormatBox))
				.addColumn(Column.span2().appendChild(timeBox))
				.addColumn(Column.span1().offset(10).appendChild((searchBtn = Button.create("Search")).disable().addClickListener(this::doSearch)))
				.addColumn(Column.span1().offset(11).appendChild(clearFiltersIcon))
				.styler(FilterBarPlugin::zerroBottomMargin);

		// export form
		div.add((exportForm = createForm()).add(row1).add(row2));

		setAutoSearch(true);
	}

	private HtmlContentBuilder<HTMLFormElement> createForm() {
		final HtmlContentBuilder<HTMLFormElement> form = form();
		final HTMLFormElement formEl = form.element();
		formEl.action = "/api/rest/messages/export";
		formEl.method = "POST";
		final InputBuilder<HTMLInputElement> from = input(hidden);
		hiddenFrom = from.element();
		hiddenFrom.name = "from";
		final InputBuilder<HTMLInputElement> topic = input(hidden);
		hiddenTopic = topic.element();
		hiddenTopic.name = "topic";
		final InputBuilder<HTMLInputElement> keyFormat = input(hidden);
		hiddenKeyFormat = keyFormat.element();
		hiddenKeyFormat.name = "keyFormat";
		final InputBuilder<HTMLInputElement> messageFormat = input(hidden);
		hiddenMessageFormat = messageFormat.element();
		hiddenMessageFormat.name = "messageFormat";
		final InputBuilder<HTMLInputElement> connectionId = input(hidden);
		hiddenConnectionId = connectionId.element();
		hiddenConnectionId.name = "connectionId";
		return form.add(from).add(topic).add(messageFormat).add(keyFormat).add(connectionId);
	}

	private void doSearch(final Event evt) {
		evt.preventDefault();
		doSearch();
	}

	private void clearFilters(final Event evt) {
		dataTable.getSearchContext().clear();
		doSearch();
	}

	protected static void zerroBottomMargin(final Style<?, ?> style) {
		style.setMarginBottom("0"); //$NON-NLS-1$
	}

	protected static void filterFieldStyle(final Style<?, ?> style) {
		style
				.setBackgroundColor(INHERIT)
				.setHeight(INHERIT)
				.setPaddingTop(INHERIT);

	}

	@Override
	public void onBeforeAddTable(final DataTable<Message> dt) {
		dataTable = dt;
		dt.addTableEventListner(SearchClearedEvent.SEARCH_EVENT_CLEARED, this);
		dt.element().appendChild(div.element());
		searchBtn.enable();
		autoSearchTimer.schedule(0);
	}

	private void clear(final Event evt) {
		evt.stopPropagation();
		autoSearchTimer.cancel();
		doSearch();
	}

	@SuppressWarnings("unused")
	private void autoSearch(final Date date, final DateTimeFormatInfo dateTimeFormatInfo) {
		autoSearch(null);
	}

	@SuppressWarnings("unused")
	private void autoSearch(final Date time, final DateTimeFormatInfo dateTimeFormatInfo, final TimePicker picker) {
		autoSearch(null);
	}

	@SuppressWarnings("unused")
	private void autoSearch(final Object evt) {
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
			keyBox.addChangeHandler(stringHandler);
			valuePatternBox.addEventListener(EventType.input.getName(), autoSearchEventListener);
			valuePatternBox.addChangeHandler(stringHandler);
			dateBox.getDatePicker().addDateSelectionHandler(dateHandler);
			timeBox.getTimePicker().addTimeSelectionHandler(timeHandler);
			messageFormatBox.addEventListener(EventType.input.getName(), autoSearchEventListener);
			messageFormatBox.addSelectionHandler(msgSelHandler);
			keyFormatBox.addEventListener(EventType.input.getName(), autoSearchEventListener);
			keyFormatBox.addSelectionHandler(keySelHandler);
		} else {
			autoSearchTimer.cancel();
			keyBox.removeEventListener(EventType.input.getName(), autoSearchEventListener);
			keyBox.removeChangeHandler(stringHandler);
			valuePatternBox.removeEventListener(EventType.input.getName(), autoSearchEventListener);
			valuePatternBox.removeChangeHandler(stringHandler);
			dateBox.getDatePicker().removeDateSelectionHandler(dateHandler);
			timeBox.getTimePicker().removeTimeSelectionHandler(timeHandler);
			messageFormatBox.removeEventListener(EventType.input.getName(), autoSearchEventListener);
			messageFormatBox.removeSelectionHandler(msgSelHandler);
			keyFormatBox.removeEventListener(EventType.input.getName(), autoSearchEventListener);
			keyFormatBox.removeSelectionHandler(keySelHandler);
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
			handleClearEvent(valuePatternBox);
			handleClearEvent(dateBox);
			handleClearEvent(timeBox);
			messageFormatBox.selectAt(0);
			keyFormatBox.selectAt(0);
		}
	}

	private static void handleClearEvent(final ValueBox<?, ?, ?> box) {
		box.pauseChangeHandlers();
		box.clear();
		box.resumeChangeHandlers();
	}

	public MessageParameters getMessageParameters() {
		return new ImmutableMessageParameters.Builder()
				.connection(gafkaCoordinates.connectionId())
				.from(getFrom())
				.key(getKeyValue())
				.keyFormat(getKeyFormat())
				.messageFormat(getMessageFormat())
				.topic(gafkaCoordinates.topic())
				.valuePattern(getValuePattern())
				.build();
	}

	@SuppressWarnings("null")
	@Nonnull
	private Optional<GwtLocalDateTimeModel> getFrom() {
		final Date dateVal = dateBox.getValue();
		if (dateVal == null) {
			return Optional.empty();
		}
		final GwtLocalDateModel gwtDate = Dates.toLocalDate(dateVal);
		final Date timeVal = timeBox.getValue();
		final GwtLocalTimeModel gwtTime = timeVal != null ? Dates.toLocalTime(timeVal) : new GwtLocalTimeModel();
		return Optional.of(new GwtLocalDateTimeModel(gwtDate, gwtTime));
	}

	@Nonnull
	private String getKeyValue() {
		final String value = keyBox.getValue();
		return value == null ? Uis.NOTHING : value;
	}

	@Nonnull
	private String getValuePattern() {
		final String value = valuePatternBox.getValue();
		return value == null ? Uis.NOTHING : value;
	}

	@Nonnull
	private MessageFormat getMessageFormat() {
		final MessageFormat value = messageFormatBox.getValue();
		return value == null ? MessageFormat.values()[0] : value;
	}

	@Nonnull
	private KeyFormat getKeyFormat() {
		final KeyFormat value = keyFormatBox.getValue();
		return value == null ? KeyFormat.values()[0] : value;
	}

	public void export() {
		getFrom().map(Dates::printDateTime).ifPresent(this::setFrom);
		hiddenTopic.value = gafkaCoordinates.topic();
		hiddenKeyFormat.value = getKeyFormat().name();
		hiddenMessageFormat.value = getMessageFormat().name();
		hiddenConnectionId.value = gafkaCoordinates.connectionId();
		exportForm.element().submit();
	}

	private void setFrom(final String from) {
		hiddenFrom.value = from;
	}

}
