package org.jresearch.gavka.gwt.core.client.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.dominokit.domino.ui.Typography.Paragraph;
import org.dominokit.domino.ui.collapsible.Collapsible;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.layout.Layout;
import org.dominokit.domino.ui.style.Styles;
import org.dominokit.domino.ui.tabs.Tab;
import org.dominokit.domino.ui.tabs.TabsPanel;
import org.dominokit.domino.ui.tree.Tree;
import org.dominokit.domino.ui.tree.TreeItem;
import org.jresearch.commons.gwt.client.app.AbstractAppView;
import org.jresearch.commons.gwt.client.app.IAppModule;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.commons.gwt.client.mvc.event.module.ModuleEvent;
import org.jresearch.gavka.gwt.core.client.module.message.MessageController;
import org.jresearch.gavka.rest.api.ConnectionLabel;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.safehtml.shared.SafeHtml;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLElement;

public class GavkaAppView extends AbstractAppView<GavkaAppController> {

	private final class NavClickHandler implements EventListener {

		private final ConnectionLabel connectionLabel;
		private final String topic;

		private NavClickHandler(final ConnectionLabel connectionLabel, final String topic) {
			this.connectionLabel = connectionLabel;
			this.topic = topic;
		}

		@Override
		public void handleEvent(final Event evt) {
			bus.fire(new ModuleEvent(MessageController.id(MessageController.ID, connectionLabel.getId(), topic)));
		}
	}

	@Nonnull
	private final Layout layout;
	private final Tree connectionTree;
	private Map<ConnectionLabel, TreeItem> connectionNodes = new HashMap<>();
	private Multimap<ConnectionLabel, String> topics = HashMultimap.create();
	private String defaultModule;
	private Map<String, Tab> tabs = new HashMap<>();
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

	@Inject
	public GavkaAppView(@Nonnull final GavkaAppController controller, @Nonnull final Bus bus) {
		super(controller, bus);
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
		Tab tab = tabs.get(viewId);
		if (tab == null) {
			tab = Tab.create(getTopicName(viewId));
			tabs.put(viewId, tab);
			tabsPanel.appendChild(tab);
			tab.appendChild(TabsPanel.create()
					.appendChild(Tab.create("Messages").appendChild(content))
					.appendChild(Tab.create("Consumers").appendChild(Paragraph.create("TODO"))));
		}
		tabsPanel.activateTab(tab);
		return true;
	}

	private static String getTopicName(String viewId) {
		return viewId.substring(viewId.lastIndexOf('.'));
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

	@Nonnull
	public TreeItem addConnection(final ConnectionLabel connection) {
		TreeItem moduleNode = TreeItem.create(connection.getLabel());
		connectionNodes.put(connection, moduleNode);
		connectionTree.appendChild(moduleNode);
		return moduleNode;
	}

	public void addTopic(ConnectionLabel connection, String topic) {
		// wrong parameters or already registered
		if (connection == null || topic == null || topics.containsEntry(connection, topic)) {
			return;
		}
		topics.put(connection, topic);
		TreeItem connectionNode = connectionNodes.get(connection);
		if (connectionNode == null) {
			connectionNode = addConnection(connection);
		}
		connectionNode.appendChild(TreeItem.create(topic).addClickListener(new NavClickHandler(connection, topic)));
	}

}
