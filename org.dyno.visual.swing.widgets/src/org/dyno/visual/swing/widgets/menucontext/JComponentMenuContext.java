package org.dyno.visual.swing.widgets.menucontext;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import org.dyno.visual.swing.base.CompositeMenuContext;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.IWidgetListener;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetEvent;
import org.dyno.visual.swing.widgets.JPopupMenuAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;

public class JComponentMenuContext extends CompositeMenuContext {
	public void fillContextAction(MenuManager menu) {
		Component widget = adaptable.getWidget();
		if (widget instanceof JComponent) {
			JComponent jcomp = (JComponent) widget;
			JPopupMenu jpm = JavaUtil.getComponentPopupMenu(jcomp);
			if (jpm != null && WidgetAdapter.getWidgetAdapter(jpm) != null) {
				menu.add(new HideShowPopupAction(widget));
				menu.add(new DeletePopupAction(widget));
			}
		}
		
		super.fillContextAction(menu);
	}
	class DeletePopupAction extends Action {
		private Component widget;
		private JPopupMenu popup;
		private JPopupMenuAdapter popupAdapter;
		public DeletePopupAction(Component widget) {
			this.widget = widget;
			JComponent jcomp = (JComponent) widget;
			popup = JavaUtil.getComponentPopupMenu(jcomp);
			popupAdapter = (JPopupMenuAdapter) WidgetAdapter.getWidgetAdapter(popup);
			setText("Delete Popup Menu " + popupAdapter.getID());
		}
		@Override
		public void run() {
			JComponent jcomp = (JComponent) widget;			
			JavaUtil.hideMenu();
			jcomp.setComponentPopupMenu(null);
			WidgetAdapter widgetAdapter = WidgetAdapter.getWidgetAdapter(widget);
			widgetAdapter.setDirty(true);
			widgetAdapter.addNotify();
			popupAdapter.deleteNotify();
			WidgetEvent we = new WidgetEvent(widgetAdapter, popupAdapter);
			for(IWidgetListener listener:ExtensionRegistry.getWidgetListeners()){
				listener.widgetRemoved(we);
			}
			widgetAdapter.repaintDesigner();
		}
	}

}
