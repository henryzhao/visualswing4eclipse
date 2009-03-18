package org.dyno.visual.swing.widgets.delegate;

import java.awt.Container;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

public class JCheckBoxMenuItemMouseDelegate extends WidgetMouseInputAdapter{
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		if(e.isConsumed())
			return;
		if (e == null || e.getButton() == MouseEvent.BUTTON1) {
			JCheckBoxMenuItem jmenu = (JCheckBoxMenuItem) adaptable.getWidget();
			Container parent = jmenu.getParent();
			if (parent instanceof JPopupMenu) {
				boolean selected = jmenu.isSelected();
				jmenu.setSelected(!selected);
				adaptable.setDirty(true);
				adaptable.repaintDesigner();				
			}
		}
		if (e != null) {
			e.setSource(null);
		}
	}
}
