/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor.spinnermodels.types;


public abstract class NumberType {
	private static NumberType[] types={
		new ByteType(),
		new ShortType(),
		new IntegerType(), 
		new LongType(),
		new FloatType(),
		new DoubleType(),
	};
	public static NumberType[]getTypes(){
		return types;
	}
	public static int getInitialIndex(){
		return 2;
	}
	public static int getNumberTypeIndex(Number value){
		int index = 2;
		if(value!=null){
			if(value instanceof Byte)
				index = 0;
			else if(value instanceof Short)
				index = 1;
			else if(value instanceof Integer)
				index =2;
			else if(value instanceof Long)
				index = 3;
			else if(value instanceof Float)
				index = 4;
			else if(value instanceof Double)
				index = 5;
		}
		return index;
		
	}
	private String name;
	protected NumberType(String name){
		this.name = name;
	}
	public String toString(){
		return name;
	}
	public abstract int getMininum();
	public abstract int getMaximum();
	public abstract Number valueOf(int value);
}
