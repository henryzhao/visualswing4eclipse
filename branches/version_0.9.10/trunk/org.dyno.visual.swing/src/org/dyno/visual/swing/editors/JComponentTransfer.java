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

package org.dyno.visual.swing.editors;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

class JComponentTransfer extends ByteArrayTransfer {
	private static int JCOMPONENT_TRANSFER_TYPE_ID;
	static{
		JCOMPONENT_TRANSFER_TYPE_ID=registerType("JComponent_Transfer");
	}
	private Object data;
	public JComponentTransfer(){
	}
	@Override
	protected int[] getTypeIds() {
		return new int[]{JCOMPONENT_TRANSFER_TYPE_ID};
	}

	@Override
	protected String[] getTypeNames() {
		return new String[]{"Component Transfer"};
	}

	@Override
	protected void javaToNative(Object object, TransferData transferData) {
		data = object;
	}

	@Override
	protected Object nativeToJava(TransferData transferData) {
		super.nativeToJava(transferData);
		return data;
	}
}

