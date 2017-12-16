package org.jresearch.gavka.gwt.core.client.app;

import java.util.List;

import javax.annotation.Nonnull;

import org.jresearch.commons.gwt.client.app.AbstractAppView;
import org.jresearch.commons.gwt.client.app.IAppModule;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.gavka.gwt.core.client.app.widget.AppPage;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HasWidgets.ForIsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class LogbackUiView extends AbstractAppView<LogbackUiController> {

	@Nonnull
	private final AppPage page;

	@Inject
	public LogbackUiView(@Nonnull final LogbackUiController controller, @Nonnull final AppPage page, @Nonnull final Bus bus) {
		super(controller, bus);
		this.page = page;
	}

	@Override
	@Nonnull
	public Widget getContent() {
		return page;
	}

	@Override
	public ForIsWidget getChildContainer(final String viewId) {
		return page.getChildContainer(viewId);
	}

	@Override
	public void showAbout() {
		// do nothing
	}

	@Override
	public boolean switchToModule(final String moduleId) {
		return page.switchToModule(moduleId);
	}

	@Override
	public void initModules(final List<IAppModule> modules) {
		for (final IAppModule module : modules) {
			page.addModule(module);
		}
	}

	/*
	 * (non-Javadoc)
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
		// do nothing
	}

}
