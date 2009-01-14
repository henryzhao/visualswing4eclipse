package org.dyno.visual.swing.widgets.delegate;

import java.awt.Container;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.MouseInputAdapter;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.widgets.JCheckBoxMenuItemAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class JCheckBoxMenuItemMouseDelegate extends MouseInputAdapter implements IAdaptableContext{
	private JCheckBoxMenuItemAdapter adapter;
	@Override
	public void mousePressed(MouseEvent e) {
		if (e == null || e.getButton() == MouseEvent.BUTTON1) {
			JCheckBoxMenuItem jmenu = (JCheckBoxMenuItem) adapter.getWidget();
			Container parent = jmenu.getParent();
			if (parent instanceof JPopupMenu) {
				boolean selected = jmenu.isSelected();
				jmenu.setSelected(!selected);
				adapter.setDirty(true);
				adapter.repaintDesigner();				
			}
		}
		if (e != null) {
			e.setSource(null);
		}
	}
	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adapter=(JCheckBoxMenuItemAdapter) adaptable;
	}
}
