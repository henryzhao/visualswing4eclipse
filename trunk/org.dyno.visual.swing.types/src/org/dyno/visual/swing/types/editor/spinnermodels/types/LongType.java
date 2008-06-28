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
