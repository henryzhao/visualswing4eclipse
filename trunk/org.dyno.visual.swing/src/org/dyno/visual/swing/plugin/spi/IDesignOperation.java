package org.dyno.visual.swing.plugin.spi;

import java.awt.Point;

public interface IDesignOperation {
	boolean dragExit(Point p);
	boolean dragOver(Point p);
	boolean drop(Point p);
	boolean dragEnter(Point p);
}
