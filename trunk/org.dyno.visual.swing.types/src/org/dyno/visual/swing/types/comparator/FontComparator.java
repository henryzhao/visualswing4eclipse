package org.dyno.visual.swing.types.comparator;

import java.awt.Font;
import java.util.Comparator;

public class FontComparator implements Comparator<Font> {
	@Override
	public int compare(Font o1, Font o2) {
		String f1 = o1.getFamily();
		String f2 = o2.getFamily();
		int style1 = o1.getStyle();
		int style2 = o2.getStyle();
		int size1 = o1.getSize();
		int size2 = o2.getSize();
		return f1.equals(f2)&&style1==style2&&size1==size2?0:1;
	}
}
