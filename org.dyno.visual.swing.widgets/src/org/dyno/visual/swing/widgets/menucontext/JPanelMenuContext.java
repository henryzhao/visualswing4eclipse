package org.dyno.visual.swing.widgets.menucontext;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import org.dyno.visual.swing.base.CompositeMenuContext;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.widgets.JPanelAdapter;
import org.dyno.visual.swing.widgets.Messages;
import org.dyno.visual.swing.widgets.actions.NullLayoutAction;
import org.dyno.visual.swing.widgets.actions.SetLayoutAction;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;

public class JPanelMenuContext extends CompositeMenuContext {

	@Override
	public void fillContextAction(MenuManager menu) {
		super.fillContextAction(menu);
		fillSetLayoutAction(menu);
	}

	public void fillSetLayoutAction(MenuManager menu) {
		MenuManager layoutMenu = new MenuManager(Messages.JPanelAdapter_Set_Layout, "#SET_LAYOUT"); //$NON-NLS-2$
		fillLayoutAction(layoutMenu);
		menu.add(layoutMenu);
	}

	@Override
	public void fillConstraintsAction(MenuManager menu, Component child) {
		JPanel jpanel = (JPanel) adaptable.getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null)
			((CompositeAdapter)adaptable).getLayoutAdapter().fillConstraintsAction(menu, child);
	}

	private void fillLayoutAction(MenuManager layoutMenu) {
		Action nullLayoutAction = new NullLayoutAction((JPanelAdapter)adaptable);
		JPanel jpanel = (JPanel) adaptable.getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null)
			nullLayoutAction.setChecked(true);
		layoutMenu.add(nullLayoutAction);
		for (String layoutClass : LayoutAdapter.getLayoutClasses()) {
			IConfigurationElement config = LayoutAdapter
					.getLayoutConfig(layoutClass);
			SetLayoutAction action = new SetLayoutAction((JPanelAdapter)adaptable, config);
			if (layout != null) {
				String currLayoutClass = layout.getClass().getName();
				if (currLayoutClass.equals(layoutClass)) {
					action.setChecked(true);
				}
			}
			layoutMenu.add(action);
		}
	}
}
