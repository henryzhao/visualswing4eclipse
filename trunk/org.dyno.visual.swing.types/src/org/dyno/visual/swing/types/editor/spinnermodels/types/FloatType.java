/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor.spinnermodels.types;

public class FloatType extends NumberType {

	protected FloatType() {
		super("Float");
	}

	@Override
	public int getMaximum() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getMininum() {
		return Integer.MIN_VALUE;
	}

	@Override
	public Number valueOf(int value) {
		return Float.valueOf(value);
	}

}
