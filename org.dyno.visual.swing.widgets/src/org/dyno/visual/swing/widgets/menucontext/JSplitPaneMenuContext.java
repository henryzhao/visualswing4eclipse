package org.dyno.visual.swing.widgets.menucontext;

import java.awt.Component;

import javax.swing.JSplitPane;

import org.dyno.visual.swing.widgets.Messages;
import org.dyno.visual.swing.widgets.actions.JSplitPanePlacementAction;
import org.eclipse.jface.action.MenuManager;

public class JSplitPaneMenuContext extends JComponentMenuContext {

	@Override
	public void fillConstraintsAction(MenuManager menu, Component child) {
		MenuManager plcMenu = new MenuManager(Messages.JSplitPaneAdapter_Component_Placement,
				"#BORDERLAYOUT_CONSTRAINTS"); //$NON-NLS-1$
		JSplitPane jsp = (JSplitPane) adaptable.getWidget();
		if (jsp.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
			plcMenu.add(new JSplitPanePlacementAction(jsp, "left", child)); //$NON-NLS-1$
			plcMenu.add(new JSplitPanePlacementAction(jsp, "right", child)); //$NON-NLS-1$
		} else {
			plcMenu.add(new JSplitPanePlacementAction(jsp, "top", child)); //$NON-NLS-1$
			plcMenu.add(new JSplitPanePlacementAction(jsp, "bottom", child)); //$NON-NLS-1$
		}
		menu.add(plcMenu);
	}
}
