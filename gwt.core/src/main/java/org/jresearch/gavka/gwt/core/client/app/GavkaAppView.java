package org.jresearch.gavka.gwt.core.client.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.google.common.base.Splitter;
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
		private final String connectionId;
		private final String topic;

		private NavClickHandler(final String moduleId, final String connectionId, final String topic) {
			this.moduleId = moduleId;
			this.connectionId = connectionId;
			this.topic = topic;
		}

		@Override
		public void handleEvent(final Event evt) {
			bus.fire(new ModuleEvent(GafkaModule.id(moduleId, connectionId, topic)));
		}
	}

	private final class TabClickHandler implements EventListener {

		@Nonnull
		private final String connectionTopicId;

		private TabClickHandler(@Nonnull final String connectionTopicId) {
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
	private final Map<String, Tab> tabs = new HashMap<>();
	private final Map<String, TreeItem> topicNodes = new HashMap<>();
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
	private final Map<String, TabsPanel> tabsPanels = new HashMap<>();

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

	@SuppressWarnings("boxing")
	@Override
	public boolean updateChildContent(final String viewId, final HTMLElement content) {
		final Optional<String> tabId = GafkaModule.getConnectionTopicName(viewId);
		return tabId.map(id -> updateChildContent(id, viewId, content)).orElse(Boolean.FALSE).booleanValue();
	}

	private boolean updateChildContent(final String tabId, final String viewId, final HTMLElement content) {
		final Tab connectionTab = tabs.computeIfAbsent(tabId, this::createConnectionTab);
		tabsPanel.activateTab(connectionTab);
		final Tab tab = tabs.get(viewId);
		if (tab != null) {
			tabsPanels.get(tabId).activateTab(tab);
			final DominoElement<HTMLDivElement> container = tab.getContentContainer();
			if (!container.hasChildNodes()) {
				container.appendChild(content);
			}
		}
		return true;
	}

	private Tab createConnectionTab(final String tabId) {
		final Optional<String> topicName = GafkaModule.getTopicName(tabId);
		return topicName.map(name -> createConnectionTab(name, tabId)).orElse(null);
	}

	private Tab createConnectionTab(final String topicName, final String tabId) {
		final Tab connectionTab = Tab.create(topicName)
				.addClickListener(new TabClickHandler(tabId));
		final TabsPanel moduleTabs = TabsPanel.create();
		tabsPanels.put(tabId, moduleTabs);
		connectionTab.appendChild(moduleTabs);
		tabModules.stream().map(m -> createTab(tabId, m)).forEachOrdered(moduleTabs::appendChild);
		tabsPanel.appendChild(connectionTab);
		return connectionTab;
	}

	private Tab createTab(final String tabId, final GafkaModule<?> tabModule) {
		final Tab result = Tab.create(tabModule.getName());
		tabs.put(String.join(".", tabModule.getModuleId(), tabId), result);
		final List<String> list = Splitter.on('.').splitToList(tabId);
		result.addClickListener(new NavClickHandler(tabModule.getModuleId(), list.get(0), list.get(1)));
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

	public void addTopic(final ConnectionLabel connection, final String topic) {
		// wrong parameters or already registered
		if (connection == null || topic == null || topics.containsEntry(connection, topic)) {
			return;
		}
		topics.put(connection, topic);
		TreeItem connectionNode = connectionNodes.get(connection);
		if (connectionNode == null) {
			connectionNode = addConnection(connection);
		}
		final TreeItem topicNode = TreeItem.create(topic)
				.addClickListener(new NavClickHandler(defaultModule, connection.getId(), topic));
		topicNodes.put(String.join(".", connection.getId(), topic), topicNode);
		connectionNode.appendChild(topicNode);
	}

	public void selectTopic(@Nonnull final String connectionTopicId) {
		final TreeItem treeItem = topicNodes.get(connectionTopicId);
		if (treeItem != null) {
			treeItem.show(true).activate(true);
		}

	}

}
