package org.dyno.visual.swing.plugin.spi;

import java.awt.Graphics;

public interface IPainter {
	void paintMascot(Graphics g);
	void paintGrid(Graphics clipg);
	void paintHovered(Graphics clipg);
	void paintHint(Graphics g);
	void paintAnchor(Graphics g);
}
