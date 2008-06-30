package org.dyno.visual.swing.types.editor.spinnermodels.types;

public class ByteType extends NumberType {
	protected ByteType() {
		super("Byte");
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
