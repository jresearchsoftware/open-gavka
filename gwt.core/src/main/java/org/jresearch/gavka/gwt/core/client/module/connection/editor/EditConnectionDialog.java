package org.jresearch.gavka.gwt.core.client.module.connection.editor;

import static java.util.Objects.*;
import static org.jboss.gwt.elemento.core.Elements.*;

import java.util.function.Consumer;

import javax.inject.Inject;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.forms.FieldsGrouping;
import org.dominokit.domino.ui.forms.SuggestBox;
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
	SuggestBox icon;
	TextBox color;

	private Consumer<Connection> onCreateHandler = c -> {
	};

	private FieldsGrouping fieldsGrouping = FieldsGrouping.create();
	private Connection connection;

	private HtmlContentBuilder<HTMLDivElement> colorMark = div().style("width: 2rem; height: 2rem;");

	@Inject
	public EditConnectionDialog(IconSuggestBoxStore iconStore) {

		id = TextBox.create("Id")
				.setRequired(true)
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setPlaceholder("Connection id")
				.floating()
				.setLeftAddon(Icons.ALL.label());

		label = TextBox.create("Label")
				.setRequired(true)
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setPlaceholder("Connection label")
				.floating()
				.setLeftAddon(Icons.ALL.description());

		icon = SuggestBox.create("Icon", iconStore)
				.setRequired(true)
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setPlaceholder("Connection icon")
				.floating()
				.setLeftAddon(Icons.ALL.description())
				.addChangeHandler(this::onIcon);

		color = TextBox.create("Color")
				.setRequired(true)
				.setAutoValidation(true)
				.groupBy(fieldsGrouping)
				.setPlaceholder("Connection color")
				.floating()
				.setLeftAddon(Icons.ALL.description())
				.setRightAddon(colorMark)
				.addChangeHandler(this::onColor);

		modalDialog = ModalDialog.create()
				.setAutoClose(false)
				.appendChild(Row.create()
						.fullSpan(column -> column.appendChild(id)))
				.appendChild(Row.create()
						.fullSpan(column -> column.appendChild(label)))
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
			color.invalidate("Color doesn't exist");
		}
	}

	private void onIcon(String value) {
		try {
			icon.setRightAddon(Icons.of(value));
			icon.clearInvalid();
		} catch (IllegalArgumentException e) {
			icon.invalidate("Color doesn't exist");
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