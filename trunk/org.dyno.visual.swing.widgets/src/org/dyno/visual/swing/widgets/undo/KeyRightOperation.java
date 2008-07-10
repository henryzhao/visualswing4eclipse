package org.dyno.visual.swing.widgets.undo;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class KeyRightOperation extends MoveOperation {

	public KeyRightOperation(WidgetAdapter adapter) {
		super(adapter);
	}

	@Override
	protected int getStepSize() {
		return 5;
	}

	@Override
	protected boolean isVertical() {
		return false;
	}

	@Override
	protected String getName() {
		return "Move Right";
	}

}
