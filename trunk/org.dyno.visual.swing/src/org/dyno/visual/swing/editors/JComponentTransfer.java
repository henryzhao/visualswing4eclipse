package org.dyno.visual.swing.editors;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

public class JComponentTransfer extends ByteArrayTransfer {
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
