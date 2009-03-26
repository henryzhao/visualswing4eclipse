package org.dyno.visual.swing.widgets.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JMenuBar;

import org.dyno.visual.swing.widgets.design.JMenuBarDesignOperation;

public class JMenuBarPainter extends CompositePainter {
	@Override
	public void paintHovered(Graphics clipg) {
		if (((JMenuBarDesignOperation) getOperation()).getDropStatus() == JMenuBarDesignOperation.DROPPING_FORBIDDEN) {
			JMenuBar jmenubar = (JMenuBar) adaptable.getWidget();
			Graphics2D g2d = (Graphics2D) clipg;
			g2d.setStroke(STROKE);
			g2d.setColor(RED_COLOR);
			g2d.drawRect(0, 0, jmenubar.getWidth(), jmenubar.getHeight());
		} else if (((JMenuBarDesignOperation) getOperation()).getDropStatus() == JMenuBarDesignOperation.DROPPING_PERMITTED) {
			JMenuBar jmenubar = (JMenuBar) adaptable.getWidget();
			Graphics2D g2d = (Graphics2D) clipg;
			g2d.setStroke(STROKE);
			g2d.setColor(GREEN_COLOR);
			g2d.drawRect(0, 0, jmenubar.getWidth(), jmenubar.getHeight());
			g2d.drawLine(((JMenuBarDesignOperation) getOperation())
					.getInsert_x(), 0,
					((JMenuBarDesignOperation) getOperation()).getInsert_x(),
					jmenubar.getHeight());
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
