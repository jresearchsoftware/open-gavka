package org.jresearch.gavka.gwt.core.client.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.dominokit.domino.ui.layout.Layout;
import org.dominokit.domino.ui.style.Unit;
import org.dominokit.domino.ui.tree.Tree;
import org.dominokit.domino.ui.tree.TreeItem;
import org.jresearch.commons.gwt.client.app.AbstractAppView;
import org.jresearch.commons.gwt.client.app.IAppModule;
import org.jresearch.commons.gwt.client.mvc.INotificator;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.commons.gwt.client.mvc.event.module.ModuleEvent;
import org.jresearch.commons.gwt.shared.tools.Strings;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.safehtml.shared.SafeHtml;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLElement;

public class GavkaAppView extends AbstractAppView<GavkaAppController> {

	@SuppressWarnings("boxing")
	private static final String LEFT_PANEL_HEIGHT = Unit.percent.of(100);

	private final class NavClickHandler implements EventListener {

		private final String moduleId;
		private final String submoduleId;

		private NavClickHandler(final String moduleId) {
			this(moduleId, null);
		}

		private NavClickHandler(final String moduleId, final String submoduleId) {
			this.moduleId = moduleId;
			this.submoduleId = submoduleId;
		}

		@Override
		public void handleEvent(final Event evt) {
			final ModuleEvent event = new ModuleEvent(moduleId);
			if (Strings.isValuable(submoduleId)) {
				event.setData(submoduleId);
			}
			bus.fire(event);
		}
	}

	@Nonnull
	private final Layout layout;
	private final Tree moduleTree;
	private final Map<String, TreeItem> moduleNodes = new HashMap<>();
	private final Multimap<String, String> submodules = HashMultimap.create();

	@Inject
	public GavkaAppView(@Nonnull final INotificator notificator, @Nonnull final GavkaAppController controller, @Nonnull final Bus bus) {
		super(notificator, controller, bus);
		layout = Layout.create("Gavka");
		layout.getLeftPanel().appendChild(moduleTree = Tree.create("Modules"));
		moduleTree.setHeight(LEFT_PANEL_HEIGHT);
	}

	@Override
	@Nonnull
	public HTMLElement getContent() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean updateChildContent(final String viewId, final HTMLElement content) {
		layout.setContent(content);
		return true;
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
		modules.forEach(this::addModule);
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

	public void addModule(final IAppModule module) {
		final TreeItem moduleNode = TreeItem.create(module.getName());
		moduleNodes.put(module.getModuleId(), moduleNode);
		moduleTree.appendChild(moduleNode.addClickListener(new NavClickHandler(module.getModuleId())));
	}

	public void addSubmodule(final String moduleId, final String submoduleId, final String title) {
		// wrong parameters or already registered
		if (moduleId == null || submoduleId == null || submodules.containsEntry(moduleId, submoduleId)) {
			return;
		}
		submodules.put(moduleId, submoduleId);
		final TreeItem moduleNode = moduleNodes.get(moduleId);
		moduleNode.appendChild(TreeItem.create(title).addClickListener(new NavClickHandler(moduleId, submoduleId)));
	}

}
