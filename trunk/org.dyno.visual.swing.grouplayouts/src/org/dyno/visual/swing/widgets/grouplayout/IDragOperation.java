package org.dyno.visual.swing.widgets.grouplayout;

import java.awt.Point;

interface IDragOperation {
	int THRESHOLD_DISTANCE = 4;

	int NORTH = 0;
	int NORTH_EAST = 1;
	int EAST = 2;
	int SOUTH_EAST = 3;
	int SOUTH = 4;
	int SOUTH_WEST = 5;
	int WEST = 6;
	int NORTH_WEST = 7;
	int ANCHOR_EXT = 10;

	boolean dragExit(Point p);

	boolean dragEnter(Point p);

	boolean dragOver(Point p);

	boolean drop(Point p);
}
