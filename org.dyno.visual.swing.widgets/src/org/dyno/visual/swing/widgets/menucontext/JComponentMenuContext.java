package org.dyno.visual.swing.widgets.menucontext;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import org.dyno.visual.swing.base.CompositeMenuContext;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.JPopupMenuAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;

public class JComponentMenuContext extends CompositeMenuContext {
	public void fillContextAction(MenuManager menu) {
		Component widget = adaptable.getWidget();
		if (widget instanceof JComponent) {
			JComponent jcomp = (JComponent) widget;
			final JPopupMenu jpm = jcomp.getComponentPopupMenu();
			if (jpm != null && WidgetAdapter.getWidgetAdapter(jpm) != null) {
				final WidgetAdapter wa = WidgetAdapter.getWidgetAdapter(jpm);
				Action a = new Action((jpm.isVisible() ? "Hide" : "Show") + " Popup Menu " + wa.getID()) {
					@Override
					public void run() {
						if (jpm.isVisible())
							((JPopupMenuAdapter) wa).hidePopup();
						else
							((JPopupMenuAdapter) wa).showPopup();
					}
				};
				menu.add(a);
			}
		}
		super.fillContextAction(menu);
	}
}
