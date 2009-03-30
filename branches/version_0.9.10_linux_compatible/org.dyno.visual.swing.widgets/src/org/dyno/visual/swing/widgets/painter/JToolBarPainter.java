package org.dyno.visual.swing.widgets.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;

import javax.swing.JToolBar;

import org.dyno.visual.swing.widgets.design.JToolBarDesignOperation;

public class JToolBarPainter extends CompositePainter {
	protected static Color RED_COLOR = new Color(255, 164, 0);
	protected static Stroke STROKE;
	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 2 }, 0);
	}
	@Override
	public void paintHovered(Graphics g) {
		if (((JToolBarDesignOperation)getOperation()).isHovered()&&adaptable.getDropWidget().size()>0) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(STROKE);
			g2d.setColor(RED_COLOR);
			JToolBar parent = (JToolBar) adaptable.getWidget();
			int size = parent.getComponentCount();
			Insets insets = parent.getInsets();
			int x = insets.left;
			int y = insets.top;
			Component drop = adaptable.getDropWidget().get(0).getWidget();
			int w = drop.getWidth();
			int h = drop.getHeight();
			if (parent.getOrientation() == JToolBar.HORIZONTAL) {
				if (size > 0) {
					Component last = parent.getComponent(size - 1);
					x = last.getX() + last.getWidth();
					y = last.getY() + (last.getHeight() - drop.getHeight()) / 2;
				} else {
					x = insets.left;
					y = (parent.getHeight() - h - insets.top - insets.bottom) / 2 + insets.top;
				}
			} else {
				if (size > 0) {
					Component last = parent.getComponent(size - 1);
					y = last.getY() + last.getHeight();
					x = last.getX() + (last.getWidth() - drop.getWidth()) / 2;
				} else {
					y = insets.top;
					x = (parent.getWidth() - w - insets.left - insets.right) / 2 + insets.left;
				}
			}
			g2d.drawRect(x, y, w, h);
		}
	}	
}
