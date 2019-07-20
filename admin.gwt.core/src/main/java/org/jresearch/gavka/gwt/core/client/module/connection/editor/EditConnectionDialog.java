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
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.modals.ModalDialog;
import org.dominokit.domino.ui.style.Color;
import org.dominokit.domino.ui.style.Style;
import org.dominokit.domino.ui.style.Unit;
import org.dominokit.domino.ui.tag.TagsInput;
import org.jboss.gwt.elemento.core.builder.HtmlContentBuilder;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.commons.gwt.client.widget.OptionalEditorWrapper;
import org.jresearch.gavka.domain.ModifiableConnection;
import org.jresearch.gavka.gwt.core.client.resource.GavkaRs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;

import elemental2.dom.Event;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;

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
	OptionalEditorWrapper<String> schemaRegistryUrl;
	@Ignore
	TagsInput<Property> propertyTags;
	@Ignore
	TextBox propertyKey;
	@Ignore
	TextBox propertyValue;
	PropertiesEditor properties;

	private Consumer<ModifiableConnection> onCreateHandler = c -> {
		// nothing
	};

	private final FieldsGrouping fieldsGrouping = FieldsGrouping.create();
	private ModifiableConnection connection;

	private final HtmlContentBuilder<HTMLDivElement> colorMark = div().style("width: 2rem; height: 2rem;"); //$NON-NLS-1$

	@SuppressWarnings({ "boxing", "null" })
	@Inject
	public EditConnectionDialog(@Nonnull final Bus bus) {

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

		final TextBox schemaRegistryUrlBox = TextBox.create(GavkaRs.MSG.schemaRegistryUrlField())
				.setRequired(false)
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setPlaceholder(GavkaRs.MSG.schemaRegistryUrlPlaceholder())
				.floating()
				.setLeftAddon(Icons.ALL.registered_trademark_mdi());

		schemaRegistryUrl = new OptionalEditorWrapper<>(schemaRegistryUrlBox);

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
						.fullSpan(column -> column.appendChild(schemaRegistryUrlBox)))
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

	private void onCancel(@SuppressWarnings("unused") final Event evt) {
		propertyKey.clear();
		propertyValue.clear();
		modalDialog.close();
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
				propertyKey.clear();
				propertyValue.clear();
				modalDialog.close();
			}
		}
	}

	public ModalDialog getModalDialog() { return modalDialog; }

	public void edit(final ModifiableConnection conn) {
		driver.edit(conn);
		fieldsGrouping.clearInvalid();
		this.connection = conn;
	}

	public ModifiableConnection getConnection() { return this.connection; }

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