package org.dyno.visual.swing.widgets.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.plugin.spi.IPainter;
import org.dyno.visual.swing.plugin.spi.RootPaneContainerAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JFramePainter extends RootPaneContainerPainter {

	private int dropStatus;
	public static final int NOOP = 0;
	public static final int DROPPING_PERMITTED = 1;
	public static final int DROPPING_FORBIDDEN = 2;
	public void setDropStatus(int dropStatus){
		this.dropStatus = dropStatus;
	}
	public IPainter getContentPainter(){
		WidgetAdapter contentAdapter=((RootPaneContainerAdapter)adaptable).getContentAdapter();
		return (IPainter) contentAdapter.getAdapter(IPainter.class);
	}
	@Override
	public void paintHovered(Graphics clipg) {
		if (dropStatus == NOOP) {
			JFrame jframe = (JFrame) adaptable.getWidget();
			JMenuBar jmb = jframe.getJMenuBar();
			if (jmb != null) {
				Rectangle bounds = adaptable.getContentPane().getBounds();
				bounds.x = bounds.y = 0;
				bounds = SwingUtilities.convertRectangle(adaptable.getContentPane(), bounds,
						adaptable.getRootPane());
				clipg = clipg.create(bounds.x, bounds.y, bounds.width,
						bounds.height);
			}
			getContentPainter().paintHovered(clipg);
			if (jmb != null) {
				clipg.dispose();
			}
		} else if (dropStatus == DROPPING_FORBIDDEN) {
			Rectangle bounds = adaptable.getContentPane().getBounds();
			Graphics2D g2d = (Graphics2D) clipg;
			g2d.setStroke(STROKE);
			g2d.setColor(RED_COLOR);
			g2d.drawRect(0, 0, bounds.width, 22);
		} else if (dropStatus == DROPPING_PERMITTED) {
			Graphics2D g2d = (Graphics2D) clipg;
			g2d.setStroke(STROKE);
			g2d.setColor(GREEN_COLOR);
			Rectangle bounds = adaptable.getContentPane().getBounds();
			g2d.drawRect(0, 0, bounds.width, 22);
		}
	}

	protected static Color RED_COLOR = new Color(255, 164, 0);
	protected static Color GREEN_COLOR = new Color(164, 255, 0);
	protected static Stroke STROKE;

	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 0, new float[] { 4 }, 0);
	}
	public void paintGrid(Graphics clipg) {
		JFrame jframe = (JFrame) adaptable.getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb != null) {
			Rectangle bounds = adaptable.getContentPane().getBounds();
			bounds.x = bounds.y = 0;
			bounds = SwingUtilities.convertRectangle(adaptable.getContentPane(), bounds,
					adaptable.getRootPane());
			clipg = clipg.create(bounds.x, bounds.y, bounds.width,
					bounds.height);
		}
		getContentPainter().paintGrid(clipg);
		if (jmb != null) {
			clipg.dispose();
		}
	}	
	public void paintAnchor(Graphics g) {
		JFrame jframe = (JFrame) adaptable.getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb != null) {
			Rectangle bounds = adaptable.getContentPane().getBounds();
			bounds.x = bounds.y = 0;
			bounds = SwingUtilities.convertRectangle(adaptable.getContentPane(), bounds,
					adaptable.getRootPane());
			g = g.create(bounds.x, bounds.y, bounds.width,
					bounds.height);
		}
		getContentPainter().paintAnchor(g);
		if (jmb != null) {
			g.dispose();
		}
	}	
	@Override
	public void paintHint(Graphics clipg) {
		JFrame jframe = (JFrame) adaptable.getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb != null) {
			Rectangle bounds = adaptable.getContentPane().getBounds();
			bounds.x = bounds.y = 0;
			bounds = SwingUtilities.convertRectangle(adaptable.getContentPane(), bounds,
					adaptable.getRootPane());
			clipg = clipg.create(bounds.x, bounds.y, bounds.width,
					bounds.height);
		}
		getContentPainter().paintHint(clipg);
		if (jmb != null) {
			clipg.dispose();
		}
	}

}
