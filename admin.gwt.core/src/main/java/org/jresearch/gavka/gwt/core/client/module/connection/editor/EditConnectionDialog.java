package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import static java.util.Objects.*;
import static org.jboss.gwt.elemento.core.Elements.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.forms.FieldsGrouping;
import org.dominokit.domino.ui.forms.Select;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.grid.Row;
import org.dominokit.domino.ui.grid.Row_12;
import org.dominokit.domino.ui.grid.flex.FlexAlign;
import org.dominokit.domino.ui.grid.flex.FlexItem;
import org.dominokit.domino.ui.grid.flex.FlexLayout;
import org.dominokit.domino.ui.header.BlockHeader;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.loaders.Loader;
import org.dominokit.domino.ui.loaders.LoaderEffect;
import org.dominokit.domino.ui.modals.IsModalDialog;
import org.dominokit.domino.ui.modals.ModalDialog;
import org.dominokit.domino.ui.modals.Window;
import org.dominokit.domino.ui.style.Color;
import org.dominokit.domino.ui.style.Style;
import org.dominokit.domino.ui.style.Unit;
import org.dominokit.domino.ui.tag.TagsInput;
import org.dominokit.domino.ui.utils.TextNode;
import org.fusesource.restygwt.client.REST;
import org.jboss.gwt.elemento.core.Elements;
import org.jboss.gwt.elemento.core.EventType;
import org.jboss.gwt.elemento.core.builder.HtmlContentBuilder;
import org.jresearch.commons.gwt.client.mvc.GwtMethodCallback;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.domain.ConnectionCheck;
import org.jresearch.gavka.domain.ListCheck;
import org.jresearch.gavka.domain.ModifiableConnection;
import org.jresearch.gavka.domain.SimpleCheck;
import org.jresearch.gavka.gwt.core.client.module.connection.srv.GavkaConnectionRestService;
import org.jresearch.gavka.gwt.core.client.resource.GavkaRs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.Text;

public class EditConnectionDialog implements Editor<ModifiableConnection>, PropertySelectHandler {

	private final Driver driver = GWT.create(Driver.class);

	interface Driver extends SimpleBeanEditorDriver<ModifiableConnection, EditConnectionDialog> {
		// nothing
	}

	private final ModalDialog modalDialog;

	TextBox id;
	TextBox label;
	Select<String> icon;
	Select<String> color;
	@Ignore
	TextBox bootstrapServersString;
	StringsEditorWrapper bootstrapServers;
	@Ignore
	TextBox schemaRegistryUrlString;
	StringsEditorWrapper schemaRegistryUrl;
	@Ignore
	TagsInput<Property> propertyTags;
	@Ignore
	TextBox propertyKey;
	@Ignore
	TextBox propertyValue;
	PropertiesEditor properties;

	private final Text testResult = TextNode.of(GavkaRs.MSG.testResultNotTested());
	private final FlexItem detailsLink = FlexItem.create().appendChild(Elements.a().textContent(GavkaRs.MSG.testResultDetailLink()).style("margin-left: 10px;").asElement()).hide(); //$NON-NLS-1$
	private EventListener detailListener;

	private Consumer<ModifiableConnection> onCreateHandler = c -> {
		// nothing
	};

	private final FieldsGrouping fieldsGrouping = FieldsGrouping.create();
	private ModifiableConnection connection;

	private final HtmlContentBuilder<HTMLDivElement> colorMark = div().style("width: 2rem; height: 2rem;"); //$NON-NLS-1$

	@Nonnull
	private final GavkaConnectionRestService srv;
	@Nonnull
	private final Bus bus;
	@Nonnull
	private final Row_12 testRow;

	@SuppressWarnings({ "boxing", "null" })
	@Inject
	public EditConnectionDialog(@Nonnull final Bus bus, @Nonnull final GavkaConnectionRestService srv) {

		this.bus = bus;
		this.srv = srv;
		bus.addHandler(PropertySelectEvent.TYPE, this);

		id = TextBox.create()
				.groupBy(fieldsGrouping)
				.setPlaceholder(GavkaRs.MSG.idPlaceholder())
				.floating()
				.setReadOnly(true)
				.setLeftAddon(Icons.ALL.barcode_scan_mdi());

		label = TextBox.create(GavkaRs.MSG.labelField())
				.setRequired(true)
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setPlaceholder(GavkaRs.MSG.labelPlaceholder())
				.floating()
				.setLeftAddon(Icons.ALL.label_mdi());

		icon = MdIconSelect.create(GavkaRs.MSG.iconField())
				.setRequired(true)
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setLeftAddon(Icons.ALL.paw_mdi())
				.addChangeHandler(this::onIcon);

		color = MdColorSelect.create(GavkaRs.MSG.colorField())
				.setRequired(true)
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setLeftAddon(Icons.ALL.palette_mdi())
				.setRightAddon(colorMark)
				.addChangeHandler(this::onColor);

		bootstrapServersString = TextBox.create(GavkaRs.MSG.bootstrapServersField())
				.setRequired(true)
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setPlaceholder(GavkaRs.MSG.bootstrapServersPlaceholder())
				.floating()
				.setLeftAddon(Icons.ALL.bootstrap_mdi());
		bootstrapServers = new StringsEditorWrapper(bootstrapServersString);

		schemaRegistryUrlString = TextBox.create(GavkaRs.MSG.schemaRegistryUrlField())
				.setRequired(false)
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setPlaceholder(GavkaRs.MSG.schemaRegistryUrlPlaceholder())
				.floating()
				.setLeftAddon(Icons.ALL.registered_trademark_mdi());
		schemaRegistryUrl = new StringsEditorWrapper(schemaRegistryUrlString);

		propertyTags = PropertyInput.create(GavkaRs.MSG.propertyTagsField(), bus)
				.disableUserInput()
				.groupBy(fieldsGrouping)
				.setPlaceholder(GavkaRs.MSG.propertyTagsPlaceholder())
				.floating()
				.setLeftAddon(Icons.ALL.settings_mdi());
		propertyKey = TextBox.create(GavkaRs.MSG.propertyKeyField())
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setPlaceholder(GavkaRs.MSG.propertyKeyPlaceholder())
				.floating()
				.setLeftAddon(Icons.ALL.key_mdi());
		propertyValue = TextBox.create(GavkaRs.MSG.propertyValueField())
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setPlaceholder(GavkaRs.MSG.propertyValuePlaceholder())
				.floating()
				.setLeftAddon(Icons.ALL.treasure_chest_mdi());
		final Style<HTMLElement, Button> propertyAdd = Button.createSuccess(Icons.ALL.plus_mdi()).circle().addClickListener(this::onPropertyAdd).style().setMargin(Unit.px.of(5));

		properties = new PropertiesEditor(propertyTags);

		modalDialog = ModalDialog.create()
				.setAutoClose(false)
				.appendChild(Row.create()
						.fullSpan(column -> column.appendChild(id)))
				.appendChild(Row.create()
						.fullSpan(column -> column.appendChild(label)))
				.appendChild(Row.create()
						.fullSpan(column -> column.appendChild(bootstrapServersString)))
				.appendChild(Row.create()
						.fullSpan(column -> column.appendChild(schemaRegistryUrlString)))
				.appendChild(Row.create()
						.fullSpan(column -> column.appendChild(icon)))
				.appendChild(Row.create()
						.fullSpan(column -> column.appendChild(color)))
				.appendChild(Row.create()
						.fullSpan(column -> column.appendChild(propertyTags)))
				.appendChild(Row.create()
						.span5(column -> column.appendChild(propertyKey))
						.span6(column -> column.appendChild(propertyValue))
						.span1(column -> column.appendChild(propertyAdd)))
				.appendChild(testRow = Row.create()
						.span1(c -> c.appendChild(Icons.ALL.bug_check_outline_mdi()))
						.span11(c -> c.appendChild(FlexLayout.create()
								.appendChild(FlexItem.create().appendChild(testResult))
								.appendChild(detailsLink))))
				.appendFooterChild(Button.create(Icons.ALL.check())
						.linkify()
						.setContent(GavkaRs.MSG.buttonTest().toUpperCase())
						.styler(style -> style.setMinWidth(Unit.px.of(100)))
						.addClickListener(this::onTest))
				.appendFooterChild(Button.create(Icons.ALL.clear())
						.linkify()
						.setContent(GavkaRs.MSG.buttonCancel().toUpperCase())
						.styler(style -> style.setMinWidth(Unit.px.of(100)))
						.addClickListener(this::onCancel))
				.appendFooterChild(Button.createPrimary(Icons.ALL.save())
						.setContent(GavkaRs.MSG.buttonSave().toUpperCase())
						.styler(style -> style.setMinWidth(Unit.px.of(100)))
						.addClickListener(evt -> onSave()));

		driver.initialize(this);
	}

	private void onPropertyAdd(@SuppressWarnings("unused") final Event evt) {
		propertyKey.setRequired(true);
		propertyValue.setRequired(true);
		if (propertyKey.validate().isValid() & propertyValue.validate().isValid()) {
			final List<Property> list = new ArrayList<>(propertyTags.getValue());
			@SuppressWarnings("null")
			final PropertyTuple newProp = PropertyTuple.of(propertyKey.getValue(), propertyValue.getValue());
			final int i = list.indexOf(newProp);
			if (i == -1) {
				list.add(newProp);
			} else {
				list.set(i, newProp);
			}
			propertyTags.setValue(list);
			propertyKey.setRequired(false);
			propertyValue.setRequired(false);
			propertyKey.clear();
			propertyValue.clear();
		} else {
			propertyKey.setRequired(false);
			propertyValue.setRequired(false);
		}
	}

	@SuppressWarnings("null")
	private void onTest(@SuppressWarnings("unused") final Event evt) {
		if (fieldsGrouping.validate().isValid()) {
			final ModifiableConnection toTest = driver.flush();
			fieldsGrouping.clearInvalid();
			final Loader loader = Loader.create(testRow, LoaderEffect.PROGRESS_BAR)
					.setRemoveLoadingText(true)
					.start();
			REST.withCallback(new GwtMethodCallback<ConnectionCheck>(bus, r -> onCheck(r, loader), e -> onCheckFail(e, loader))).call(srv).check(toTest);
		}
	}

	private void onCheck(final ConnectionCheck result) {
		onCheck(result, null);
	}

	private void onCheck(final ConnectionCheck result, final Loader loader) {
		if (loader != null) {
			loader.stop();
		}
		testResult.data = result.reason().orElseGet(() -> GavkaRs.MSG.testResultStatus(result.status()));
		if (detailListener != null) {
			detailsLink.removeEventListener(EventType.click, detailListener);
		}
		detailsLink.addClickListener(detailListener = e -> onDetails(result));
		detailsLink.show();
	}

	private static void onDetails(final ConnectionCheck result) {
		new Window(GavkaRs.MSG.testResultWindowsHeader())
				.setFixed()
				.setSize(IsModalDialog.ModalSize.SMALL)
				.setModal(true)
				.setHeaderBackground(getColor(result))
				.apply((self) -> self
						.appendChild(bootstrapServerCheck(result.bootstrapServerCheck()))
						.appendChild(schemaRegistryUrlCheck(result.schemaRegistryUrlCheck()))
						.appendChild(propertiesCheck(result.propertiesCheck())))
				.open();
	}

	private static HTMLDivElement propertiesCheck(final ListCheck<String> propertiesCheck) {
		final HTMLDivElement element = div().asElement();
		final boolean empty = propertiesCheck.checks().isEmpty();
		final BlockHeader header = empty ? BlockHeader.create(GavkaRs.MSG.testResultPropSectionHeader(), GavkaRs.MSG.testResultPropSectionEmpty()) : BlockHeader.create(GavkaRs.MSG.testResultPropSectionHeader());
		element.appendChild(header.asElement());
		if (!empty) {
			element.appendChild(record(GavkaRs.MSG.testResultSectionGeneral(), propertiesCheck));
			propertiesCheck.checks().stream().map(EditConnectionDialog::record).forEach(element::appendChild);
		}
		return element;
	}

	private static HTMLDivElement schemaRegistryUrlCheck(final ListCheck<String> schemaRegistryUrlCheck) {
		final HTMLDivElement element = div().style("margin-bottom: 15px;").asElement(); //$NON-NLS-1$
		final boolean empty = schemaRegistryUrlCheck.checks().isEmpty();
		final BlockHeader header = empty ? BlockHeader.create(GavkaRs.MSG.testResultSchemaRegSectionHeader(), GavkaRs.MSG.testResultSchemaRegSectionEmpty()) : BlockHeader.create(GavkaRs.MSG.testResultSchemaRegSectionHeader());
		element.appendChild(header.asElement());
		if (!empty) {
			element.appendChild(record(GavkaRs.MSG.testResultSectionGeneral(), schemaRegistryUrlCheck));
			schemaRegistryUrlCheck.checks().stream().map(EditConnectionDialog::record).forEach(element::appendChild);
		}
		return element;
	}

	private static HTMLDivElement bootstrapServerCheck(final ListCheck<String> bootstrapServerCheck) {
		final HTMLDivElement element = div().style("margin-bottom: 15px;").asElement(); //$NON-NLS-1$
		final BlockHeader header = BlockHeader.create(GavkaRs.MSG.testResultBootstrapSectionHeader());
		element.appendChild(header.asElement());
		element.appendChild(record(GavkaRs.MSG.testResultSectionGeneral(), bootstrapServerCheck));
		bootstrapServerCheck.checks().stream().map(EditConnectionDialog::record).forEach(element::appendChild);
		return element;
	}

	private static HTMLDivElement record(final SimpleCheck<String> check) {
		return record(check.subject(), check);
	}

	@SuppressWarnings("boxing")
	private static HTMLDivElement record(final String name, final SimpleCheck<?> check) {
		return FlexLayout.create()
				.appendChild(FlexItem.create().appendChild(getIcon(check).styler(s -> s.setColor(getColor(check).getHex()))).styler(s -> s.setMargin(Unit.px.of(5))))
				.appendChild(FlexItem.create().appendChild(TextNode.of(name)).styler(s -> s.setMargin(Unit.px.of(5))))
				.appendChild(FlexItem.create().appendChild(TextNode.of(getReason(check))).styler(s -> s.setMargin(Unit.px.of(5))))
				.setAlignItems(FlexAlign.CENTER)
				.asElement();
	}

	private static MdiIcon getIcon(final SimpleCheck<?> result) {
		switch (result.status()) {
		case ERROR:
			return Icons.ALL.alert_decagram_mdi();
		case OK_WITH_WARNING:
			return Icons.ALL.alert_outline_mdi();
		case OK:
			return Icons.ALL.check_all_mdi();
		default:
			return Icons.ALL.infinity_mdi();
		}
	}

	private static Color getColor(final SimpleCheck<?> result) {
		switch (result.status()) {
		case ERROR:
			return Color.RED_DARKEN_1;
		case OK_WITH_WARNING:
			return Color.ORANGE_DARKEN_1;
		case OK:
			return Color.GREEN_DARKEN_1;
		default:
			return Color.RED_DARKEN_4;
		}
	}

	private static String getReason(final SimpleCheck<?> result) {
		return result.reason().orElseGet(() -> GavkaRs.MSG.testResultStatus(result.status()));
	}

	private boolean onCheckFail(final Throwable exception, final Loader loader) {
		loader.stop();
		resetCheck(GavkaRs.MSG.testResultFailed(exception.getMessage()));
		return false;
	}

	private void onCancel(@SuppressWarnings("unused") final Event evt) {
		close();
	}

	private void onColor(final String value) {
		try {
			colorMark.asElement().style.backgroundColor = Color.of(value).getHex();
			color.clearInvalid();
		} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
			icon.invalidate(GavkaRs.MSG.invalidColor());
		}
	}

	private void onIcon(final String value) {
		try {
			icon.setRightAddon(Icons.of(value));
			icon.clearInvalid();
		} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
			icon.invalidate(GavkaRs.MSG.invalidIcon());
		}
	}

	private void onSave() {
		// Save entered but not added custom property
		if (propertyKey.getValue() != null && propertyValue.getValue() != null) {
			onPropertyAdd(null);
		}
		if (fieldsGrouping.validate().isValid()) {
			if (nonNull(onCreateHandler)) {
				onCreateHandler.accept(driver.flush());
				close();
			}
		}
	}

	private void close() {
		propertyKey.clear();
		propertyValue.clear();
		resetCheck(GavkaRs.MSG.testResultNotTested());
		modalDialog.close();
	}

	private void resetCheck(final String message) {
		testResult.data = message;
		if (detailListener != null) {
			detailsLink.removeEventListener(EventType.click, detailListener);
			detailListener = null;
		}
		detailsLink.hide();
	}

	public ModalDialog getModalDialog() {
		return modalDialog;
	}

	public void edit(final ModifiableConnection conn) {
		driver.edit(conn);
		fieldsGrouping.clearInvalid();
		this.connection = conn;
		conn.connectionCheck().ifPresent(this::onCheck);
	}

	public ModifiableConnection getConnection() {
		return this.connection;
	}

	public EditConnectionDialog onSave(final Consumer<ModifiableConnection> handler) {
		this.onCreateHandler = handler;
		return this;
	}

	@Override
	public void onPropertySelect(final PropertySelectEvent event) {
		final Property property = event.getProperty();
		if (property != null) {
			propertyKey.setValue(property.key());
			propertyValue.setValue(property.value());
		}
	}

}