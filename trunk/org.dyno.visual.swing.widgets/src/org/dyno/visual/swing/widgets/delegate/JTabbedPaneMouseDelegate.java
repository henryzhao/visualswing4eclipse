package org.dyno.visual.swing.widgets.delegate;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.MouseInputAdapter;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.JTabbedPaneAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class JTabbedPaneMouseDelegate extends MouseInputAdapter implements
		IAdaptableContext {
	private JTabbedPaneAdapter adapter;

	@Override
	public void mousePressed(MouseEvent e) {
		JTabbedPane tabbedPane = (JTabbedPane) adapter.getWidget();
		int count = tabbedPane.getTabCount();
		for (int i = 0; i < count; i++) {
			Rectangle tabBounds = tabbedPane.getBoundsAt(i);
			if ((tabBounds != null) && tabBounds.contains(e.getX(), e.getY())) {
				tabbedPane.setSelectedIndex(i);
				JComponent widget = (JComponent) tabbedPane.getComponent(i);
				adapter.clearAllSelected();
				WidgetAdapter selAdapter = WidgetAdapter.getWidgetAdapter(widget);
				selAdapter.setSelected(true);
				adapter.changeNotify();
				e.consume();
				break;
			}
		}
	}

	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adapter = (JTabbedPaneAdapter) adaptable;
	}
}
