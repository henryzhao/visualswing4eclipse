package org.dyno.visual.swing.types.endec;

import java.awt.Insets;
import java.util.StringTokenizer;

import org.dyno.visual.swing.plugin.spi.IEndec;

public class InsetsEndec implements IEndec {

	@Override
	public Object decode(String string) {
		return string==null?null:decodeInsets(string);
	}
	private Insets decodeInsets(String string){
		StringTokenizer st=new StringTokenizer(string, ",");
		if(!st.hasMoreTokens())
			return null;
		int left = 0;
		try{
			left=Integer.parseInt(st.nextToken().trim());
		}catch(NumberFormatException nfe){}
		if(!st.hasMoreTokens())
			return null;
		int top = 0;
		try{
			top=Integer.parseInt(st.nextToken().trim());
		}catch(NumberFormatException nfe){}
		if(!st.hasMoreTokens())
			return null;
		int right = 0;
		try{
			right=Integer.parseInt(st.nextToken().trim());
		}catch(NumberFormatException nfe){}
		if(!st.hasMoreTokens())
			return null;
		int bottom = 0;
		try{
			bottom=Integer.parseInt(st.nextToken().trim());
		}catch(NumberFormatException nfe){}		
		return new Insets(left,top,right,bottom);
	}
	@Override
	public String encode(Object value) {
		return value==null?"null":encodeInsets((Insets)value);
	}
	private String encodeInsets(Insets value) {
		return ""+value.left+","+value.top+","+value.right+","+value.bottom;
	}

}
