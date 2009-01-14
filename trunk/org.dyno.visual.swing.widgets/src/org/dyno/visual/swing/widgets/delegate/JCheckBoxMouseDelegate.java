package org.dyno.visual.swing.widgets.delegate;

import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.event.MouseInputAdapter;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.widgets.JCheckBoxAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class JCheckBoxMouseDelegate extends MouseInputAdapter implements IAdaptableContext{
	private JCheckBoxAdapter adapter;
	@Override
	public void mousePressed(MouseEvent e) {
		assert e!=null;
		if (e.getButton() == MouseEvent.BUTTON1) {
			JCheckBox checkBox = (JCheckBox) adapter.getWidget();
			boolean selected = checkBox.isSelected();
			checkBox.setSelected(!selected);
			adapter.setDirty(true);
			adapter.repaintDesigner();				
		}
		e.setSource(null);
	}
	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adapter=(JCheckBoxAdapter) adaptable;
	}
}
