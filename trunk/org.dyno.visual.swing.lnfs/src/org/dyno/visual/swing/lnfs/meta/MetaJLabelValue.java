package org.dyno.visual.swing.lnfs.meta;

import javax.swing.SwingConstants;

public class MetaJLabelValue extends MetaJComponentValue {
	private static final long serialVersionUID = 1L;
	public MetaJLabelValue() {
		put("alignmentY", 0.5f);
		put("iconTextGap", 4);
		put("inheritsPopupMenu", true);
		put("doubleBuffered", false);
		put("horizontalAlignment", SwingConstants.LEADING);
	}
}
