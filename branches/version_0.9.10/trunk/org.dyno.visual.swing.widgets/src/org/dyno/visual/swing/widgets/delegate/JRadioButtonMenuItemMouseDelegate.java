package org.dyno.visual.swing.widgets.delegate;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.MouseInputAdapter;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.JRadioButtonMenuItemAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class JRadioButtonMenuItemMouseDelegate extends MouseInputAdapter implements IAdaptableContext{
	private JRadioButtonMenuItemAdapter adapter;
	@Override
	public void mousePressed(MouseEvent e) {
		if (e == null || e.getButton() == MouseEvent.BUTTON1) {
			JRadioButtonMenuItem jmenu = (JRadioButtonMenuItem) adapter
					.getWidget();
			Container parent = jmenu.getParent();
			if (parent instanceof JPopupMenu) {
				jmenu.setSelected(true);
				adapter.setDirty(true);
				DefaultButtonModel bm=(DefaultButtonModel) jmenu.getModel();
				ButtonGroup bg=bm.getGroup();
				if(bg!=null){
					int count =bg.getButtonCount();
					if(count>0){
						Enumeration<AbstractButton> elements = bg.getElements();
						while(elements.hasMoreElements()){
							AbstractButton ab=elements.nextElement();
							WidgetAdapter btnAdapter = WidgetAdapter.getWidgetAdapter(ab);
							btnAdapter.setDirty(true);
						}
					}
				}
				adapter.repaintDesigner();
			}
		}
		if(e!=null){
			e.setSource(null);
		}
	}
	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adapter=(JRadioButtonMenuItemAdapter) adaptable;
	}
}
