package org.jresearch.gavka.gwt.core.client.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.dominokit.domino.ui.collapsible.Collapsible;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.layout.Layout;
import org.dominokit.domino.ui.style.Styles;
import org.dominokit.domino.ui.tabs.Tab;
import org.dominokit.domino.ui.tabs.TabsPanel;
import org.dominokit.domino.ui.tree.Tree;
import org.dominokit.domino.ui.tree.TreeItem;
import org.dominokit.domino.ui.utils.DominoElement;
import org.jresearch.commons.gwt.client.app.AbstractAppView;
import org.jresearch.commons.gwt.client.app.IAppModule;
import org.jresearch.commons.gwt.client.mvc.INotificator;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.commons.gwt.client.mvc.event.module.ModuleEvent;
import org.jresearch.gavka.gwt.core.client.module.GafkaModule;
import org.jresearch.gavka.rest.api.ConnectionLabel;
import org.jresearch.gavka.rest.data.GafkaCoordinates;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.safehtml.shared.SafeHtml;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;

public class GavkaAppView extends AbstractAppView<GavkaAppController> {

	private final class NavClickHandler implements EventListener {

		private final String moduleId;
		private final GafkaCoordinates gafkaCoordinates;

		private NavClickHandler(final String moduleId, final GafkaCoordinates gafkaCoordinates) {
			this.moduleId = moduleId;
			this.gafkaCoordinates = gafkaCoordinates;
		}

		@Override
		public void handleEvent(final Event evt) {
			final ModuleEvent event = new ModuleEvent(GafkaModule.id(moduleId, gafkaCoordinates));
			event.setData(gafkaCoordinates);
			bus.fire(event);
		}
	}

	private final class TabClickHandler implements EventListener {

		@Nonnull
		private final GafkaCoordinates connectionTopicId;

		private TabClickHandler(@Nonnull final GafkaCoordinates connectionTopicId) {
			this.connectionTopicId = connectionTopicId;
		}

		@Override
		public void handleEvent(final Event evt) {
			bus.fire(new TabEvent(connectionTopicId));
		}
	}

	@Nonnull
	private final Layout layout;
	private final Tree connectionTree;
	private final Map<ConnectionLabel, TreeItem> connectionNodes = new HashMap<>();
	private final Multimap<ConnectionLabel, String> topics = HashMultimap.create();
	private String defaultModule;
	// Gafka coordinates -> to main (connection) tab
	private final Map<GafkaCoordinates, Tab> connectionTabs = new HashMap<>();
	// Module Id -> to module tab
	private final Map<String, Tab> moduleTabs = new HashMap<>();
	private final Map<GafkaCoordinates, TreeItem> topicNodes = new HashMap<>();
	private TabsPanel tabsPanel;
	private final Icon lockIcon = Icons.ALL.lock()
			.style()
			.setMarginBottom("0px")
			.setMarginTop("0px")
			.setCursor("pointer")
			.add(Styles.pull_right)
			.get();
	private boolean locked = true;
	private final Collapsible lockCollapsible = Collapsible.create(lockIcon).show();
	private final List<GafkaModule> tabModules = new ArrayList<>();
	private final Map<GafkaCoordinates, TabsPanel> tabsPanels = new HashMap<>();

	@Inject
	public GavkaAppView(@Nonnull final INotificator notificator, @Nonnull final GavkaAppController controller, @Nonnull final Bus bus) {
		super(notificator, controller, bus);
		layout = Layout.create("Gavka");
		layout.fixLeftPanelPosition();
		layout.getLeftPanel().appendChild(connectionTree = Tree.create("Connections"));
		connectionTree.getHeader().appendChild(lockIcon.asElement());
		lockIcon.addClickListener(evt -> {
			if (locked) {
				layout.unfixLeftPanelPosition();
				lockIcon.asElement().textContent = Icons.ALL.lock_open().getName();
				layout.hideLeftPanel();
				locked = false;
			} else {
				layout.fixLeftPanelPosition();
				lockIcon.asElement().textContent = Icons.ALL.lock().getName();
				locked = true;
			}
		});

		layout.setContent(tabsPanel = TabsPanel.create());
	}

	@Override
	@Nonnull
	public HTMLElement getContent() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean updateChildContent(final String viewId, final HTMLElement content) {
		final GafkaCoordinates tabId = GafkaModule.getCoordinates(viewId);
		updateChildContent(tabId, viewId, content);
		return true;
	}

	private boolean updateChildContent(final GafkaCoordinates tabId, final String viewId, final HTMLElement content) {
		final Tab connectionTab = connectionTabs.computeIfAbsent(tabId, this::createConnectionTab);
		tabsPanel.activateTab(connectionTab);
		final Tab tab = moduleTabs.get(viewId);
		if (tab != null) {
			tabsPanels.get(tabId).activateTab(tab);
			final DominoElement<HTMLDivElement> container = tab.getContentContainer();
			if (!container.hasChildNodes()) {
				container.appendChild(content);
			}
		}
		return true;
	}

	private Tab createConnectionTab(@Nonnull final GafkaCoordinates tabId) {
		final Tab connectionTab = Tab.create(tabId.topic()).addClickListener(new TabClickHandler(tabId));
		final TabsPanel moduleTabsPanel = TabsPanel.create();
		tabsPanels.put(tabId, moduleTabsPanel);
		connectionTab.appendChild(moduleTabsPanel);
		tabModules.stream().map(m -> createTab(tabId, m)).forEachOrdered(moduleTabsPanel::appendChild);
		tabsPanel.appendChild(connectionTab);
		return connectionTab;
	}

	private Tab createTab(final GafkaCoordinates tabId, final GafkaModule<?> tabModule) {
		final Tab result = Tab.create(tabModule.getName());
		moduleTabs.put(GafkaModule.id(tabModule.getModuleId(), tabId), result);
		result.addClickListener(new NavClickHandler(tabModule.getModuleId(), tabId));
		return result;
	}

	@Override
	public void showAbout() {
		// do nothing
	}

	@Override
	public boolean switchToModule(final String moduleId) {
		return true;
	}

	@Override
	public void initModules(final List<IAppModule> modules) {
		if (!modules.isEmpty()) {
			defaultModule = modules.get(0).getModuleId();
		}
		modules.stream()
				.filter(m -> m instanceof GafkaModule)
				.map(m -> (GafkaModule) m)
				.forEach(tabModules::add);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.jresearch.commons.gwt.client.app.AbstractAppView#showModule(java.lang
	 * .String)
	 */
	@Override
	public boolean showModule(final String moduleId) {
		// do nothing
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.jresearch.commons.gwt.client.app.AbstractAppView#hideModule(java.lang
	 * .String)
	 */
	@Override
	public boolean hideModule(final String moduleId) {
		// do nothing
		return true;
	}

	@Override
	protected void updateAppTitle(final SafeHtml viewTitle) {
		if (viewTitle != null) {
			layout.setTitle(viewTitle.asString());
		}
	}

	@Override
	public void showContent() {
		layout.show();
	}

	@SuppressWarnings("null")
	@Nonnull
	public TreeItem addConnection(final ConnectionLabel connection) {
		final TreeItem moduleNode = TreeItem.create(connection.getLabel());
		connectionNodes.put(connection, moduleNode);
		connectionTree.appendChild(moduleNode);
		return moduleNode;
	}

	@SuppressWarnings("null")
	public void addTopic(@Nonnull final ConnectionLabel connection, @Nonnull final String topic) {
		// wrong parameters or already registered
		if (topics.containsEntry(connection, topic)) {
			return;
		}
		topics.put(connection, topic);
		TreeItem connectionNode = connectionNodes.get(connection);
		if (connectionNode == null) {
			connectionNode = addConnection(connection);
		}
		final GafkaCoordinates coordinates = GafkaModule.create(connection.getId(), topic);
		final TreeItem topicNode = TreeItem.create(topic)
				.addClickListener(new NavClickHandler(defaultModule, coordinates));
		topicNodes.put(coordinates, topicNode);
		connectionNode.appendChild(topicNode);
	}

	public void selectTopic(@Nonnull final GafkaCoordinates gafkaCoordinates) {
		final TreeItem treeItem = topicNodes.get(gafkaCoordinates);
		if (treeItem != null) {
			treeItem.show(true).activate(true);
		}

	}

}
