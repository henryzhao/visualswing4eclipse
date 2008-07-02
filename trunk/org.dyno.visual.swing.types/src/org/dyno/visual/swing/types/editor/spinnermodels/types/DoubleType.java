package org.dyno.visual.swing.types.editor.spinnermodels.types;

public class DoubleType extends NumberType {

	protected DoubleType() {
		super("Double");
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
		return Double.valueOf(value);
	}

}
