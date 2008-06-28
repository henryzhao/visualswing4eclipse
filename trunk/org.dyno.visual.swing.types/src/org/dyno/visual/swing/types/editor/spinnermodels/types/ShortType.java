package org.dyno.visual.swing.types.editor.spinnermodels.types;


public class ShortType extends NumberType {

	protected ShortType() {
		super("Short");
	}

	@Override
	public int getMaximum() {
		return Short.MAX_VALUE;
	}

	@Override
	public int getMininum() {
		return Short.MIN_VALUE;
	}

	@Override
	public Number valueOf(int value) {
		return Short.valueOf((short)value);
	}

}
