package org.dyno.visual.swing.lnfs.meta;

import javax.swing.SwingConstants;

public class MetaJRadioButtonValue extends MetaAbstractButtonValue {
	private static final long serialVersionUID = 1L;

	public MetaJRadioButtonValue() {
		put("opaque", true);
        put("defaultCapable", true);
		put("doubleBuffered", false);
		put("alignmentY", 0.5f);
		put("rolloverEnabled", false);
		put("borderPainted", false);
		put("horizontalAlignment", SwingConstants.LEADING);
		put("rolloverEnabled", true);
	}

}
