package org.dyno.visual.swing.types.endec;

import java.awt.Rectangle;
import java.util.StringTokenizer;

import org.dyno.visual.swing.plugin.spi.IEndec;

public class RectangleEndec implements IEndec {

	@Override
	public Object decode(String string) {
		return string==null?null:decodeRectangle(string);
	}
	private Rectangle decodeRectangle(String string){
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
		if(!st.hasMoreTokens())
			return null;
		int width = 0;
		try{
			width=Integer.parseInt(st.nextToken().trim());
		}catch(NumberFormatException nfe){}
		if(!st.hasMoreTokens())
			return null;
		int height = 0;
		try{
			height=Integer.parseInt(st.nextToken().trim());
		}catch(NumberFormatException nfe){}		
		return new Rectangle(x,y,width,height);
	}
	@Override
	public String encode(Object value) {
		return value==null?"null":encodeRectangle((Rectangle)value);
	}
	private String encodeRectangle(Rectangle value) {
		return ""+value.x+","+value.y+","+value.width+","+value.height;
	}

}
