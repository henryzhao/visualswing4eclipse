package org.dyno.visual.swing.widgets.delegate;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.widgets.JMenuAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class JMenuMouseDelegate extends MouseAdapter implements IAdaptableContext{
	private JMenuAdapter adapter;
	@Override
	public void mousePressed(MouseEvent e) {
		JMenu jmenu = (JMenu) adapter.getWidget();
		boolean v = jmenu.isPopupMenuVisible();
		if (v) {
			adapter.hidePopup();
		} else {
			adapter.showPopup();
		}
	}
	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adapter=(JMenuAdapter) adaptable;
	}
}
