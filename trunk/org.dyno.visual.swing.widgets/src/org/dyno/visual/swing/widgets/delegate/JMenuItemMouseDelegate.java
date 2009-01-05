package org.dyno.visual.swing.widgets.delegate;

import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.widgets.JMenuItemAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class JMenuItemMouseDelegate extends MouseAdapter implements IAdaptableContext{
	private JMenuItemAdapter adapter;
	@Override
	public void mousePressed(MouseEvent e) {
		JMenuItem jmenu = (JMenuItem) adapter.getWidget();
		Container parent = jmenu.getParent();
		if (parent instanceof JPopupMenu) {
			boolean selected=jmenu.isSelected();
			jmenu.setSelected(!selected);
		}
	}
	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adapter=(JMenuItemAdapter) adaptable;
	}
}
