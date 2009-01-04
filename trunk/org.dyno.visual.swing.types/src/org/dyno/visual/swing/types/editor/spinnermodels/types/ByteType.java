
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

public class ByteType extends NumberType {
	protected ByteType() {
		super(Messages.ByteType_Byte);
	}

	@Override
	public int getMaximum() {
		return Byte.MAX_VALUE;
	}

	@Override
	public int getMininum() {
		return Byte.MIN_VALUE;
	}

	@Override
	public Number valueOf(int value) {
		return Byte.valueOf((byte)value);
	}
}

