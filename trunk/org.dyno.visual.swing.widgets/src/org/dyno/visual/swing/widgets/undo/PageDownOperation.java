package org.dyno.visual.swing.widgets.undo;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class PageDownOperation extends MoveOperation {
	public PageDownOperation(WidgetAdapter adapter) {
		super(adapter);
	}

	@Override
	protected int getStepSize() {
		return 20;
	}

	@Override
	protected boolean isVertical() {
		return true;
	}

	@Override
	protected String getName() {
		return "Page Down";
	}

}
