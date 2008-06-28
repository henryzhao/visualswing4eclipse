package org.dyno.visual.swing.types.comparator;

import java.awt.Color;
import java.util.Comparator;

public class ColorComparator implements Comparator<Color> {
	@Override
	public int compare(Color c1, Color c2) {
		int r1 = c1.getRed();
		int g1 = c1.getGreen();
		int b1 = c1.getBlue();
		int r2 = c2.getRed();
		int g2 = c2.getGreen();
		int b2 = c2.getBlue();
		return r1 == r2 && g1 == g2 && b1 == b2 ? 0 : 1;
	}
}
