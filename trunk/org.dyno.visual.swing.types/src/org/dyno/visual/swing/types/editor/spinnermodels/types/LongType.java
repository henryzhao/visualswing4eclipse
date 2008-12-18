
/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.types.editor.spinnermodels.types;

public class LongType extends NumberType {

	protected LongType() {
		super("Long");
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
		return Long.valueOf(value);
	}

}

