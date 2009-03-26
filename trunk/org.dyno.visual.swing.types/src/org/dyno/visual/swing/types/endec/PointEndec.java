package org.dyno.visual.swing.types.endec;

import java.awt.Point;
import java.util.StringTokenizer;

import org.dyno.visual.swing.plugin.spi.IEndec;

public class PointEndec implements IEndec {

	@Override
	public Object decode(String string) {
		return string==null?null:decodePoint(string);
	}
	private Point decodePoint(String string){
		StringTokenizer st=new StringTokenizer(string, ",");
		if(!st.hasMoreTokens())
			return null;
		int x = 0;
		try{
			x=Integer.parseInt(st.nextToken().trim());
		}catch(NumberFormatException nfe){}
		if(!st.hasMoreTokens())
			return null;
		int y = 0;
		try{
			y=Integer.parseInt(st.nextToken().trim());
		}catch(NumberFormatException nfe){}
		return new Point(x, y);
		
	}
	@Override
	public String encode(Object value) {
		return value==null?"null":encodePoint((Point)value);
	}
	private String encodePoint(Point value) {
		return ""+value.x+","+value.y;
	}

}
