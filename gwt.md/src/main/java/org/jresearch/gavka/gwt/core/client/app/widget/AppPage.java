package org.jresearch.gavka.gwt.core.client.app.widget;

import java.util.HashMap;
import java.util.Map;

import org.jresearch.commons.gwt.client.app.IAppModule;
import org.jresearch.commons.gwt.client.mvc.event.Bus;
import org.jresearch.commons.gwt.client.mvc.event.module.ModuleEvent;
import org.jresearch.commons.gwt.client.widget.Uis;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AppPage extends Composite {

	private final class NavClickHandler implements ClickHandler {

		private final IAppModule module;

		private NavClickHandler(final IAppModule module) {
			this.module = module;
		}

		@Override
		public void onClick(final ClickEvent event) {
			bus.fire(new ModuleEvent(module.getModuleId()));
		}
	}

	interface AppPageUiBinder extends UiBinder<Widget, AppPage> {
		/* nothing */}

	private final Map<String, IAppModule> modules = new HashMap<>();
	private final Map<String, HTMLPanel> eventMap = new HashMap<>();

//	@UiField
//	MaterialContainer moduleContainer;

	private final Bus bus;

	@Inject
	public AppPage(final AppPageUiBinder uiBinder, final Bus bus) {
		this.bus = bus;
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void addModule(final IAppModule module) {
		createModulePanel(module);
	}

	public void addSingleModule(final IAppModule module) {
		createModulePanel(module);
	}

	private void createModulePanel(final IAppModule module) {
		final HTMLPanel tab = new HTMLPanel(Uis.NOTHING);
		tab.setStyleName("moduleTab" + module.getModuleId());
//		moduleContainer.add(tab);
		eventMap.put(module.getModuleId(), tab);
		modules.put(module.getModuleId(), module);
	}

	public boolean switchToModule(final String moduleId) {
		if (modules.containsKey(moduleId)) {
			// navBar.setText(modules.get(moduleId).getName());
			// final int widgetIndex =
			// moduleContainer.getWidgetIndex(eventMap.get(moduleId));
			// moduleContainer.showWidget(widgetIndex);
			return true;
		}
		return false;
	}

	public HasWidgets.ForIsWidget getChildContainer(final String viewId) {
		return null;
//		return eventMap.get(viewId);
	}

}
