package org.dyno.visual.swing.widgets.design;

import java.awt.Point;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.IDesignOperation;
import org.dyno.visual.swing.plugin.spi.IPainter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class WidgetDesignOperation implements IDesignOperation, IAdaptableContext {
	protected WidgetAdapter adaptable;
	public boolean dragExit(Point p) {
		return false;
	}
	protected IPainter getPainter(){
		return (IPainter) adaptable.getAdapter(IPainter.class);
	}
	public boolean dragOver(Point p) {
		return false;
	}

	public boolean drop(Point p) {
		return false;
	}

	public boolean dragEnter(Point p) {
		return false;
	}

	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adaptable = (WidgetAdapter) adaptable;
	}
}
