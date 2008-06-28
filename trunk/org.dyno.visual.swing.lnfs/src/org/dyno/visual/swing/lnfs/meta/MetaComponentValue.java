package org.dyno.visual.swing.lnfs.meta;

import java.awt.Color;
import java.awt.Font;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class MetaComponentValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public MetaComponentValue() {
		put("foreground", new Color(51, 51, 51));
 		put("background", new Color(238, 238, 238));
		put("font", new Font("Dialog", Font.PLAIN, 11));        
		put("enabled", true);
		put("focusable", true);
		put("visible", true);        		
	}
}
