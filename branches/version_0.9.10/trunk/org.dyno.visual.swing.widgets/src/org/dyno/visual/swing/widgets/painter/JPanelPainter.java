package org.dyno.visual.swing.widgets.painter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;

public class JPanelPainter extends CompositePainter {

	@Override
	public void paintHovered(Graphics clipg) {
		JPanel jpanel = (JPanel) adaptable.getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null) {
			LayoutAdapter layoutAdapter = ((CompositeAdapter)adaptable).getLayoutAdapter();
			layoutAdapter.paintHovered(clipg);
		}
		clipg.setColor(Color.lightGray);
		clipg.drawRect(0, 0, jpanel.getWidth() -1 , jpanel.getHeight() - 1);
	}
	@Override
	public void paintGrid(Graphics clipg) {
		JPanel jpanel = (JPanel) adaptable.getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null) {
			LayoutAdapter layoutAdapter = ((CompositeAdapter)adaptable).getLayoutAdapter();
			layoutAdapter.paintGrid(clipg);
		}
	}
	@Override
	public void paintHint(Graphics g) {
		JPanel jpanel = (JPanel) adaptable.getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null) {
			LayoutAdapter layoutAdapter = ((CompositeAdapter)adaptable).getLayoutAdapter();
			layoutAdapter.paintHint(g);
		}
	}
	@Override
	public void paintAnchor(Graphics g) {
		JPanel jpanel = (JPanel) adaptable.getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null) {
			LayoutAdapter layoutAdapter = ((CompositeAdapter)adaptable).getLayoutAdapter();
			layoutAdapter.paintAnchor(g);
		}
	}
}
