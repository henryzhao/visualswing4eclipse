package org.dyno.visual.swing.types.editor.spinnermodels.types;

public class IntegerType extends NumberType {

	protected IntegerType() {
		super("Integer");
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
		return Integer.valueOf(value);
	}

}
