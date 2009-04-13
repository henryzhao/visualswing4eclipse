package org.dyno.visual.swing.types.endec;

import java.awt.Dimension;
import java.util.StringTokenizer;

import org.dyno.visual.swing.plugin.spi.IEndec;

public class DimensionEndec implements IEndec {

	
	public Object decode(String string) {
		return string==null?null:decodeDimension(string);
	}
	private Dimension decodeDimension(String string){
		StringTokenizer st=new StringTokenizer(string, ",");
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
		return new Dimension(width, height);
	}
	
	public String encode(Object value) {
		return value==null?"null":encodeDimension((Dimension)value);
	}
	private String encodeDimension(Dimension value) {
		return ""+value.width+","+value.height;
	}

}
