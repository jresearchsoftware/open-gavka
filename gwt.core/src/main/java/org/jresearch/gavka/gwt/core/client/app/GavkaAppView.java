package org.jresearch.gavka.gwt.core.client.app;

import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.dominokit.domino.ui.layout.Layout;
import org.dominokit.domino.ui.tree.Tree;
import org.dominokit.domino.ui.tree.TreeItem;
import org.jresearch.commons.gwt.client.app.AbstractAppView;
import org.jresearch.commons.gwt.client.app.IAppModule;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.commons.gwt.client.mvc.event.module.ModuleEvent;

import com.google.gwt.safehtml.shared.SafeHtml;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLElement;

public class GavkaAppView extends AbstractAppView<GavkaAppController> {

	private final class NavClickHandler implements EventListener {

		private final IAppModule module;

		private NavClickHandler(final IAppModule module) {
			this.module = module;
		}

		@Override
		public void handleEvent(final Event evt) {
			bus.fire(new ModuleEvent(module.getModuleId()));
		}
	}

	@Nonnull
	private final Layout layout;
	private final Tree moduleTree;

	@Inject
	public GavkaAppView(@Nonnull final GavkaAppController controller, @Nonnull final Bus bus) {
		super(controller, bus);
		layout = Layout.create("Gavka");
		layout.getLeftPanel().appendChild(moduleTree = Tree.create("Modules"));
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
		moduleTree.appendChild(TreeItem.create(module.getName()).addClickListener(new NavClickHandler(module)));
	}

}
