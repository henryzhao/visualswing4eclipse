package org.dyno.visual.swing.types.endec;

import java.awt.Font;
import java.util.StringTokenizer;

import org.dyno.visual.swing.plugin.spi.IEndec;

public class FontEndec implements IEndec {

	
	public Object decode(String string) {
		return string == null ? null : decodeFont(string);
	}

	private Font decodeFont(String string) {
		StringTokenizer st = new StringTokenizer(string, ",");
		if (!st.hasMoreTokens())
			return null;
		String family = st.nextToken().trim();
		if (!st.hasMoreTokens())
			return null;
		int style = 0;
		try {
			style = Integer.parseInt(st.nextToken().trim());
		} catch (NumberFormatException nfe) {
		}
		if (!st.hasMoreTokens())
			return null;
		int size = 11;
		try{
			size = Integer.parseInt(st.nextToken().trim());
		}catch(NumberFormatException nfe){}
		return new Font(family, style, size);
	}

	
	public String encode(Object value) {
		return value==null?"null":encodeFont((Font)value);
	}

	private String encodeFont(Font font) {
		return font.getFamily()+","+font.getStyle()+","+font.getSize();
	}

}
