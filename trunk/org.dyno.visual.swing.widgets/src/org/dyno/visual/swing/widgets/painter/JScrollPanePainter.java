package org.dyno.visual.swing.widgets.painter;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import org.dyno.visual.swing.widgets.design.JScrollPaneDesignOperation;

public class JScrollPanePainter extends CompositePainter {
	private static Color RED_COLOR = new Color(255, 164, 0);
	private static Color GREEN_COLOR = new Color(164, 255, 0);
	private static Stroke STROKE;
	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 2 }, 0);
	}
	@Override
	public void paintHovered(Graphics g) {
		if (((JScrollPaneDesignOperation)getOperation()).isHovered()) {
			Graphics2D g2d = (Graphics2D) g;
			Stroke olds = g2d.getStroke();
			Composite oldc = g2d.getComposite();
			g2d.setColor(((JScrollPaneDesignOperation)getOperation()).isPermitted() ? GREEN_COLOR : RED_COLOR);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			int w = adaptable.getWidget().getWidth();
			int h = adaptable.getWidget().getHeight();
			g2d.fillRect(0, 0, w, h);
			g2d.setColor(((JScrollPaneDesignOperation)getOperation()).isPermitted() ? RED_COLOR : GREEN_COLOR);
			g2d.setStroke(STROKE);
			g2d.drawRect(0, 0, w - 1, h - 1);
			g2d.setComposite(oldc);
			g2d.setStroke(olds);
		}
	}

}
