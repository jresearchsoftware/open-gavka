package org.jresearch.gavka.gwt.core.client.module.connection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.grid.Column;
import org.dominokit.domino.ui.grid.Row;
import org.dominokit.domino.ui.grid.Row_12;
import org.dominokit.domino.ui.header.BlockHeader;
import org.dominokit.domino.ui.icons.BaseIcon;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.infoboxes.InfoBox;
import org.dominokit.domino.ui.style.Color;
import org.fusesource.restygwt.client.REST;
import org.jboss.gwt.elemento.core.Elements;
import org.jresearch.commons.gwt.client.mvc.AbstractView;
import org.jresearch.commons.gwt.client.mvc.GwtMethodCallback;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.gwt.core.client.module.connection.editor.EditConnectionDialog;
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
	public ConnectionView(@Nonnull final ConnectionController controller, @Nonnull final GavkaConnectionRestService gavkaConnectionRestService, @Nonnull Bus bus, @Nonnull EditConnectionDialog conectionEditor) {
		super(controller);
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

	private void add(Event evt) {
		edit(new Connection());

	}

	private void save(Connection connection) {
		REST.withCallback(new GwtMethodCallback<>(bus, this::load)).call(gavkaConnectionRestService).save(connection);
	}

	private void load(@SuppressWarnings("unused") Boolean added) {
		controller.refreshConnections();
	}

	void edit(Connection connection) {
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
	public void updateConnections(List<Connection> connections) {
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

	private Row_12 toRow(List<Connection> connections) {
		Row_12 result = Row.create();
		connections.stream().map(this::toColumn).forEach(result::addColumn);
		connectionRows.add(result);
		return result;
	}

	private Column toColumn(Connection connection) {
		return Column
				.span3()
				.appendChild(toInfoBox(connection));
	}

	private InfoBox toInfoBox(Connection connection) {
		return InfoBox.create(getIcon(connection), connection.getId(), connection.getLabel())
				.setBackground(getColor(connection))
				.setHoverEffect(InfoBox.HoverEffect.ZOOM)
				.addClickListener(e -> edit(connection));
	}

	private static BaseIcon<?> getIcon(Connection connection) {
		return Icons.of(connection.getIcon());
	}

	private static Color getColor(Connection connection) {
		return Color.of(connection.getColor());
	}

	@Override
	public HTMLElement getContent() {
		return element;
	}

}
