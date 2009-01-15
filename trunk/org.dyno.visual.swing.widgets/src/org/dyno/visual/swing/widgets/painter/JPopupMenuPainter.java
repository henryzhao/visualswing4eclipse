package org.dyno.visual.swing.widgets.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;

import javax.swing.JPopupMenu;

import org.dyno.visual.swing.plugin.spi.IDesignOperation;
import org.dyno.visual.swing.widgets.design.JPopupMenuDesignOperation;

public class JPopupMenuPainter extends CompositePainter {

	@Override
	public void paintHovered(Graphics g) {
		JPopupMenu jpm = (JPopupMenu) adaptable.getWidget();
		Rectangle bounds = jpm.getBounds();
		Point lt = adaptable.convertToGlobal(new Point(0, 0));
		bounds.x = lt.x;
		bounds.y = lt.y;
		Graphics clipg = g.create(bounds.x, bounds.y, bounds.width,
				bounds.height);
		Graphics2D g2d = (Graphics2D) clipg;
		g2d.setStroke(STROKE);
		g2d.setColor(GREEN_COLOR);
		JPopupMenuDesignOperation operation = (JPopupMenuDesignOperation) adaptable
				.getAdapter(IDesignOperation.class);
		g2d.drawLine(0, operation.getInsert_y(), jpm.getWidth(), operation
				.getInsert_y());
		clipg.dispose();
	}

	protected static Color GREEN_COLOR = new Color(164, 255, 0);
	protected static Stroke STROKE;

	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 0, new float[] { 4 }, 0);
	}
}
