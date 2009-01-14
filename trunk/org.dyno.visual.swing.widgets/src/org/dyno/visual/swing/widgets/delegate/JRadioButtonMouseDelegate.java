package org.dyno.visual.swing.widgets.delegate;

import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JRadioButton;
import javax.swing.event.MouseInputAdapter;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.JRadioButtonAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class JRadioButtonMouseDelegate extends MouseInputAdapter implements
		IAdaptableContext {
	private JRadioButtonAdapter adapter;

	@Override
	public void mousePressed(MouseEvent e) {
		assert e!=null;
		if (e.getButton() == MouseEvent.BUTTON1) {
			JRadioButton radio = (JRadioButton) adapter.getWidget();
			radio.setSelected(true);
			adapter.setDirty(true);
			DefaultButtonModel bm = (DefaultButtonModel) radio.getModel();
			ButtonGroup bg = bm.getGroup();
			if (bg != null) {
				int count = bg.getButtonCount();
				if (count > 0) {
					Enumeration<AbstractButton> elements = bg.getElements();
					while (elements.hasMoreElements()) {
						AbstractButton ab = elements.nextElement();
						WidgetAdapter btnAdapter = WidgetAdapter
								.getWidgetAdapter(ab);
						btnAdapter.setDirty(true);
					}
				}
			}
			adapter.repaintDesigner();
		}
		e.setSource(null);
	}

	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adapter = (JRadioButtonAdapter) adaptable;
	}
}
