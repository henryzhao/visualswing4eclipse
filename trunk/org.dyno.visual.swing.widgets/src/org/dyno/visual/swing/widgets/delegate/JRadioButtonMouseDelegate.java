package org.dyno.visual.swing.widgets.delegate;

import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JRadioButton;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JRadioButtonMouseDelegate extends WidgetMouseInputAdapter {
	@Override
	public void mousePressed(MouseEvent e) {
		assert e!=null;
		super.mousePressed(e);
		if(e.isConsumed())
			return;
		if (e.getButton() == MouseEvent.BUTTON1) {
			JRadioButton radio = (JRadioButton) adaptable.getWidget();
			radio.setSelected(true);
			adaptable.setDirty(true);
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
			adaptable.repaintDesigner();
		}
		e.setSource(null);
	}
}
