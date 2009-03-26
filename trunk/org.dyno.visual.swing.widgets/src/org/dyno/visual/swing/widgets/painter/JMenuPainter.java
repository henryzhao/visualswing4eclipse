package org.dyno.visual.swing.widgets.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IPainter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.design.JMenuDesignOperation;

public class JMenuPainter extends CompositePainter {
	@Override
	public void paintHovered(Graphics clipg) {
		if (((JMenuDesignOperation)getOperation()).isInside_popup()) {
			JMenu jmenu = (JMenu) adaptable.getWidget();
			JPopupMenu popup = jmenu.getPopupMenu();
			CompositeAdapter adapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(popup);
			if (adapter == null)
				adapter = (CompositeAdapter) ExtensionRegistry.createWidgetAdapter(popup);
			IPainter painter = (IPainter) adapter.getAdapter(IPainter.class);
			painter.paintHovered(clipg);
		} else {
			if (((JMenuDesignOperation)getOperation()).getDropStatus()== JMenuDesignOperation.DROPPING_FORBIDDEN) {
				JMenu jmenu = (JMenu) adaptable.getWidget();
				Graphics2D g2d = (Graphics2D) clipg;
				g2d.setStroke(STROKE);
				g2d.setColor(RED_COLOR);
				g2d.drawRect(0, 0, jmenu.getWidth(), jmenu.getHeight());
			} else if (((JMenuDesignOperation)getOperation()).getDropStatus()== JMenuDesignOperation.DROPPING_PERMITTED) {
				JMenu jmenu = (JMenu) adaptable.getWidget();
				Graphics2D g2d = (Graphics2D) clipg;
				g2d.setStroke(STROKE);
				g2d.setColor(GREEN_COLOR);
				g2d.drawRect(0, 0, jmenu.getWidth(), jmenu.getHeight());
			}
		}
	}

	protected static Color RED_COLOR = new Color(255, 164, 0);
	protected static Color GREEN_COLOR = new Color(164, 255, 0);
	protected static Stroke STROKE;

	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 0, new float[] { 4 }, 0);
	}
}
