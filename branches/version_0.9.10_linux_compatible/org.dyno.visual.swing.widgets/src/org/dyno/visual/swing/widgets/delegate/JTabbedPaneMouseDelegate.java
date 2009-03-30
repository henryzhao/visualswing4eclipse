package org.dyno.visual.swing.widgets.delegate;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JTabbedPaneMouseDelegate extends WidgetMouseInputAdapter{
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		if(e.isConsumed())
			return;
		JTabbedPane tabbedPane = (JTabbedPane) adaptable.getWidget();
		int count = tabbedPane.getTabCount();
		for (int i = 0; i < count; i++) {
			Rectangle tabBounds = tabbedPane.getBoundsAt(i);
			if ((tabBounds != null) && tabBounds.contains(e.getX(), e.getY())) {
				tabbedPane.setSelectedIndex(i);
				JComponent widget = (JComponent) tabbedPane.getComponent(i);
				adaptable.clearAllSelected();
				WidgetAdapter selAdapter = WidgetAdapter.getWidgetAdapter(widget);
				selAdapter.setSelected(true);
				adaptable.addNotify();
				e.consume();
				break;
			}
		}
	}
}
