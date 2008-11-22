/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor.spinnermodels.types;


public class ShortType extends NumberType {

	protected ShortType() {
		super("Short");
	}

	@Override
	public int getMaximum() {
		return Short.MAX_VALUE;
	}

	@Override
	public int getMininum() {
		return Short.MIN_VALUE;
	}

	@Override
	public Number valueOf(int value) {
		return Short.valueOf((short)value);
	}

}
