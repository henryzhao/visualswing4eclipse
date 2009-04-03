package org.dyno.visual.swing.widgets.menucontext;

import java.awt.Component;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.JPopupMenuAdapter;
import org.eclipse.jface.action.Action;

public class HideShowPopupAction extends Action {
	private Component widget;
	private JPopupMenu popup;
	private JPopupMenuAdapter popupAdapter;

	public HideShowPopupAction(Component widget) {
		this.widget = widget;
		JComponent jcomp = (JComponent) widget;
		popup = JavaUtil.getComponentPopupMenu(jcomp);
		popupAdapter = (JPopupMenuAdapter) WidgetAdapter.getWidgetAdapter(popup);
		setText((popup.isVisible() ? "Hide" : "Show") + " Popup Menu " + popupAdapter.getID());
	}

	@Override
	public void run() {
		popup.setInvoker(widget);
		if (popup.isVisible())
			popupAdapter.hidePopup();
		else {
			Point loc = new Point(widget.getWidth() / 2, widget.getHeight() / 2);
			SwingUtilities.convertPointToScreen(loc, widget);
			popup.setLocation(loc);
			popupAdapter.showPopup();
		}
		WidgetAdapter invokerAdapter = WidgetAdapter.getWidgetAdapter(widget);
		if(invokerAdapter!=null)
			invokerAdapter.repaintDesigner();
	}
}
