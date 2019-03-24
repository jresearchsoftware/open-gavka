package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import static java.util.Objects.*;
import static org.jboss.gwt.elemento.core.Elements.*;

import java.util.function.Consumer;

import javax.inject.Inject;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.forms.FieldsGrouping;
import org.dominokit.domino.ui.forms.Select;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.grid.Row;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.modals.ModalDialog;
import org.dominokit.domino.ui.style.Color;
import org.jboss.gwt.elemento.core.builder.HtmlContentBuilder;
import org.jresearch.gavka.domain.Connection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;

import elemental2.dom.HTMLDivElement;

public class EditConnectionDialog implements Editor<Connection> {

	private final Driver driver = GWT.create(Driver.class);

	interface Driver extends SimpleBeanEditorDriver<Connection, EditConnectionDialog> {
		// nothing
	}

	private ModalDialog modalDialog;

	TextBox id;
	TextBox label;
	Select<String> icon;
	Select<String> color;
	@Ignore
	TextBox bootstrapServersString;
	StringsEditorWrapper bootstrapServers;
	TextBox schemaRegistryUrl;

	private Consumer<Connection> onCreateHandler = c -> {
	};

	private FieldsGrouping fieldsGrouping = FieldsGrouping.create();
	private Connection connection;

	private HtmlContentBuilder<HTMLDivElement> colorMark = div().style("width: 2rem; height: 2rem;");

	@Inject
	public EditConnectionDialog() {

		id = TextBox.create("Id")
				.groupBy(fieldsGrouping)
				.setPlaceholder("Connection id")
				.floating()
				.setReadOnly(true)
				.setLeftAddon(Icons.ALL.barcode_scan_mdi());

		label = TextBox.create("Label")
				.setRequired(true)
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setPlaceholder("Connection label")
				.floating()
				.setLeftAddon(Icons.ALL.label_mdi());

		icon = MdIconSelect.create("Icon")
				.setRequired(true)
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setLeftAddon(Icons.ALL.paw_mdi())
				.addChangeHandler(this::onIcon);

		color = MdColorSelect.create("Color")
				.setRequired(true)
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setLeftAddon(Icons.ALL.palette_mdi())
				.setRightAddon(colorMark)
				.addChangeHandler(this::onColor);

		bootstrapServersString = TextBox.create("Bootstrap servers")
				.setRequired(true)
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setPlaceholder("hostnames or IP's separated by comma")
				.floating()
				.setLeftAddon(Icons.ALL.bootstrap_mdi());
		bootstrapServers = new StringsEditorWrapper(bootstrapServersString);

		schemaRegistryUrl = TextBox.create("Schema registry URL")
				.setRequired(false)
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setPlaceholder("hostname or IP of a schema registry")
				.floating()
				.setLeftAddon(Icons.ALL.registered_trademark_mdi());

		modalDialog = ModalDialog.create()
				.setAutoClose(false)
				.appendChild(Row.create()
						.fullSpan(column -> column.appendChild(id)))
				.appendChild(Row.create()
						.fullSpan(column -> column.appendChild(label)))
				.appendChild(Row.create()
						.fullSpan(column -> column.appendChild(bootstrapServersString)))
				.appendChild(Row.create()
						.fullSpan(column -> column.appendChild(schemaRegistryUrl)))
				.appendChild(Row.create()
						.fullSpan(column -> column.appendChild(icon)))
				.appendChild(Row.create()
						.fullSpan(column -> column.appendChild(color)))
				.appendFooterChild(Button.create(Icons.ALL.clear())
						.linkify()
						.setContent("CANCEL")
						.styler(style -> style.setMinWidth("100px"))
						.addClickListener(evt -> modalDialog.close()))
				.appendFooterChild(Button.createPrimary(Icons.ALL.save())
						.setContent("SAVE")
						.styler(style -> style.setMinWidth("100px"))
						.addClickListener(evt -> onSave()));

		driver.initialize(this);
	}

	private void onColor(String value) {
		try {
			colorMark.asElement().style.backgroundColor = Color.of(value).getHex();
			color.clearInvalid();
		} catch (IllegalArgumentException e) {
			icon.invalidate("Color doesn't exist");
		}
	}

	private void onIcon(String value) {
		try {
			icon.setRightAddon(Icons.of(value));
			icon.clearInvalid();
		} catch (IllegalArgumentException e) {
			icon.invalidate("Icon doesn't exist");
		}
	}

	private void onSave() {
		if (fieldsGrouping.validate().isValid()) {
			if (nonNull(onCreateHandler)) {
				onCreateHandler.accept(driver.flush());
				modalDialog.close();
			}
		}
	}

	public ModalDialog getModalDialog() {
		return modalDialog;
	}

	public void edit(Connection connection) {
		driver.edit(connection);
		fieldsGrouping.clearInvalid();
		this.connection = connection;
	}

	public Connection getConnection() {
		return this.connection;
	}

	public EditConnectionDialog onSave(Consumer<Connection> onCreateHandler) {
		this.onCreateHandler = onCreateHandler;
		return this;
	}

}