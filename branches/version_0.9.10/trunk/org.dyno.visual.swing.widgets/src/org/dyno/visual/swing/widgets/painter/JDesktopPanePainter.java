package org.dyno.visual.swing.widgets.painter;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class JDesktopPanePainter extends CompositePainter {
	protected static Color RED_COLOR = new Color(255, 164, 0);
	protected static Color GREEN_COLOR = new Color(164, 255, 0);
	protected static Stroke STROKE;
	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 2 }, 0);
	}
	private boolean forbid;
	public void setForbid(boolean f){
		forbid=f;
	}
	public boolean isForbid(){
		return forbid;
	}
	@Override
	public void paintHovered(Graphics clipg) {
		if (forbid) {
			int w = adaptable.getWidget().getWidth();
			int h = adaptable.getWidget().getHeight();
			Graphics2D g2d = (Graphics2D) clipg;
			g2d.setColor(RED_COLOR);
			Composite oldComposite = g2d.getComposite();
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
			g2d.setComposite(composite);
			g2d.fillRect(0, 0, w, h);
			Stroke oldStroke = g2d.getStroke();
			g2d.setColor(GREEN_COLOR);
			g2d.setStroke(STROKE);
			g2d.drawRect(0, 0, w, h);
			g2d.setStroke(oldStroke);
			g2d.setComposite(oldComposite);
		}
	}

}
