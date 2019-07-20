package org.jresearch.gavka.gwt.core.client.module.connection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.grid.Column;
import org.dominokit.domino.ui.grid.Row;
import org.dominokit.domino.ui.grid.Row_12;
import org.dominokit.domino.ui.header.BlockHeader;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.modals.ModalDialog;
import org.dominokit.domino.ui.style.Color;
import org.dominokit.domino.ui.utils.TextNode;
import org.fusesource.restygwt.client.REST;
import org.jboss.gwt.elemento.core.Elements;
import org.jboss.gwt.elemento.core.IsElement;
import org.jresearch.commons.gwt.client.mvc.AbstractView;
import org.jresearch.commons.gwt.client.mvc.GwtMethodCallback;
import org.jresearch.commons.gwt.client.mvc.INotificator;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.domain.ModifiableConnection;
import org.jresearch.gavka.gwt.core.client.module.connection.editor.EditConnectionDialog;
import org.jresearch.gavka.gwt.core.client.module.connection.editor.MdColorSelect;
import org.jresearch.gavka.gwt.core.client.module.connection.editor.MdIconSelect;
import org.jresearch.gavka.gwt.core.client.module.connection.srv.GavkaConnectionRestService;

import elemental2.dom.DomGlobal;
import elemental2.dom.Event;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;

@Singleton
public class ConnectionView extends AbstractView<ConnectionController> {

	@SuppressWarnings("null")
	@Nonnull
	private final HTMLDivElement element = Elements.div().asElement();
	@Nonnull
	private final GavkaConnectionRestService gavkaConnectionRestService;
	@Nonnull
	private final Button addButton;
	@Nonnull
	private final EditConnectionDialog conectionEditor;
	@Nonnull
	private final Bus bus;
	@Nonnull
	private final List<Row_12> connectionRows = new ArrayList<>();

	@SuppressWarnings("null")
	@Inject
	public ConnectionView(@Nonnull final INotificator notificator, @Nonnull final ConnectionController controller, @Nonnull final GavkaConnectionRestService gavkaConnectionRestService, @Nonnull final Bus bus, @Nonnull final EditConnectionDialog conectionEditor) {
		super(controller, notificator);
		this.gavkaConnectionRestService = gavkaConnectionRestService;
		this.bus = bus;
		this.conectionEditor = conectionEditor;
		conectionEditor.onSave(this::save);
		element.appendChild(BlockHeader.create("Connections", "List of configured connections. To add new use the plus icon in the bottom right conner.").asElement());
		addButton = Button.create(Icons.ALL.add())
				.setBackground(Color.THEME)
				.setContent("ADD CONNECTION")
				.styler(style -> style
						.setPosition("fixed")
						.setBottom("20px")
						.setRight("20px")
						.setProperty("z-index", "9999"))
				.addClickListener(this::add)
				.hide();
		DomGlobal.document.body.appendChild(addButton.asElement());
		controller.refreshConnections();
	}

	private void add(final Event evt) {
		final ModifiableConnection connection = ModifiableConnection.create().setColor(MdColorSelect.any()).setIcon(MdIconSelect.any());
		edit(connection);
	}

	private void save(@Nonnull final ModifiableConnection connection) {
		REST.withCallback(new GwtMethodCallback<>(bus, this::load)).call(gavkaConnectionRestService).save(connection);
	}

	private void load(@SuppressWarnings("unused") final Connection added) {
		controller().refreshConnections();
	}

	private void remove(@Nonnull final Connection connection) {
		REST.withCallback(new GwtMethodCallback<>(bus, this::onRemove)).call(gavkaConnectionRestService).remove(connection.getId());
	}

	private void onRemove(final Boolean removed) {
		controller().refreshConnections();
	}

	void edit(final ModifiableConnection connection) {
		conectionEditor.edit(connection);
		conectionEditor.getModalDialog().open();
	}

	@Override
	public void onShow() {
		super.onShow();
		addButton.show();
	}

	@Override
	public void onHide() {
		addButton.hide();
		super.onHide();
	}

	@SuppressWarnings("boxing")
	public void updateConnections(final List<Connection> connections) {
		connectionRows.forEach(Row_12::remove);
		connectionRows.clear();
		final AtomicInteger counter = new AtomicInteger(0);
		connections
				.stream()
				.collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 4)).values()
				.stream()
				.map(this::toRow)
				.map(Row_12::asElement)
				.forEachOrdered(element::appendChild);
	}

	private Row_12 toRow(final List<Connection> connections) {
		final Row_12 result = Row.create();
		connections.stream().map(this::toColumn).forEach(result::addColumn);
		connectionRows.add(result);
		return result;
	}

	private Column toColumn(final Connection connection) {
		return Column
				.span3()
				.appendChild(toBox(connection));
	}

	private IsElement<?> toBox(final Connection connection) {
		return Card
				.create(connection.getLabel(), connection.getBootstrapServers().stream().findAny().orElse("No bootstrap servers"))
				.hide()
				.setHeaderBackground(getColor(connection))
				.addClickListener(e -> edit(ModifiableConnection.create().from(connection)))
				.setHeaderLogo(getIcon(connection).size48())
				.addHeaderAction(Icons.ALL.delete(), e -> remove(e, connection));
	}

	private void remove(final Event evt, final Connection connection) {
		evt.stopPropagation();
		final ModalDialog modal = ModalDialog.create("Remove connection");
		modal.setAutoClose(true)
				.appendChild(TextNode.of("Are you sure to remove the connection?"))
				.appendFooterChild(Button.create("YES").linkify().addClickListener(e -> {
					modal.close();
					remove(connection);
				}))
				.appendFooterChild(Button.create("NO").linkify().addClickListener(e -> modal.close()))
				.setModalColor(Color.ORANGE)
				.open();
	}

	private static MdiIcon getIcon(final Connection connection) {
		return MdiIcon.create(connection.getIcon());
	}

	private static Color getColor(final Connection connection) {
		return Color.of(connection.getColor());
	}

	@Override
	public HTMLElement getContent() { return element; }

}
