package org.dyno.visual.swing.types.endec;

import java.awt.Color;
import java.util.StringTokenizer;

import org.dyno.visual.swing.plugin.spi.IEndec;

public class ColorEndec implements IEndec {

	@Override
	public Object decode(String string) {
		return string==null?null:decodeColor(string);
	}
	private Color decodeColor(String string){
		StringTokenizer st=new StringTokenizer(string, ",");
		if(!st.hasMoreTokens())
			return null;
		int red=0;
		try{
			red=Integer.parseInt(st.nextToken().trim());
		}catch(NumberFormatException nfe){}
		if(!st.hasMoreTokens())
			return null;
		int green=0;
		try{
			green=Integer.parseInt(st.nextToken().trim());
		}catch(NumberFormatException nfe){}
		if(!st.hasMoreTokens())
			return null;
		int blue=0;
		try{
			blue=Integer.parseInt(st.nextToken().trim());
		}catch(NumberFormatException nfe){}
		return new Color(red, green, blue);
	}
	@Override
	public String encode(Object value) {
		return value==null?"null":encodeColor((Color)value);
	}
	
	private String encodeColor(Color value) {
		return ""+value.getRed()+","+value.getGreen()+","+value.getBlue();
	}

}
