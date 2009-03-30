package org.dyno.visual.swing.widgets.delegate;

import java.awt.event.MouseEvent;

import javax.swing.JInternalFrame;

public class JInternalFrameMouseDelegate extends WidgetMouseInputAdapter{
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		if(e.isConsumed())
			return;
		JInternalFrame jif = (JInternalFrame) adaptable.getWidget();
		jif.toFront();
	}
}
