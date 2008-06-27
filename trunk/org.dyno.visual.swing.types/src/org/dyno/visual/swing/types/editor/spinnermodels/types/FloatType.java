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
