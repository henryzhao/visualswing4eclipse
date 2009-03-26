package org.dyno.visual.swing.widgets.delegate;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.MouseInputAdapter;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.menucontext.HideShowPopupAction;
import org.eclipse.core.runtime.IAdaptable;

public class WidgetMouseInputAdapter extends MouseInputAdapter implements IAdaptableContext {
	protected WidgetAdapter adaptable;

	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adaptable = (WidgetAdapter) adaptable;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Component widget = adaptable.getWidget();
		if (widget instanceof JComponent) {
			JComponent jcomp = (JComponent) widget;
			JPopupMenu componentPopupMenu = jcomp.getComponentPopupMenu();
			if (adaptable.isSelected() && componentPopupMenu != null && WidgetAdapter.getWidgetAdapter(componentPopupMenu) != null) {
				int w = widget.getWidth();
				int h = widget.getHeight();
				if (e.getX() > w / 2 - 16 && e.getX() < w / 2 && e.getY() > h / 2 - 16 && e.getY() < h / 2) {
					adaptable.setProperty("popup.state", "up");
					adaptable.showTooltipText("Click to " + (componentPopupMenu.isVisible() ? "hide" : "show") + " popup menu!");
				} else {
					adaptable.setProperty("popup.state", "normal");
					adaptable.showTooltipText(null);
				}
				adaptable.repaintDesigner();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			Component widget = adaptable.getWidget();
			if (widget instanceof JComponent) {
				JComponent jcomp = (JComponent) widget;
				JPopupMenu componentPopupMenu = jcomp.getComponentPopupMenu();
				if (adaptable.isSelected() && componentPopupMenu != null && WidgetAdapter.getWidgetAdapter(componentPopupMenu) != null) {
					int w = widget.getWidth();
					int h = widget.getHeight();
					if (e.getX() > w / 2 - 16 && e.getX() < w / 2 && e.getY() > h / 2 - 16 && e.getY() < h / 2) {
						adaptable.setProperty("popup.state", "down");
						adaptable.repaintDesigner();
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			Component widget = adaptable.getWidget();
			if (widget instanceof JComponent) {
				JComponent jcomp = (JComponent) widget;
				JPopupMenu componentPopupMenu = jcomp.getComponentPopupMenu();
				if (adaptable.isSelected() && componentPopupMenu != null && WidgetAdapter.getWidgetAdapter(componentPopupMenu) != null) {
					int w = widget.getWidth();
					int h = widget.getHeight();
					if (e.getX() > w / 2 - 16 && e.getX() < w / 2 && e.getY() > h / 2 - 16 && e.getY() < h / 2) {
						adaptable.setProperty("popup.state", "up");
						adaptable.repaintDesigner();
						new HideShowPopupAction(widget).run();
						e.consume();
					}
				}
			}
		}
	}

}
