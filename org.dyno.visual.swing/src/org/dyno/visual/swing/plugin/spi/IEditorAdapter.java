package org.dyno.visual.swing.plugin.spi;

import java.awt.Rectangle;

public interface IEditorAdapter {
	IEditor getEditorAt(int x, int y);
	void setWidgetValue(Object value);
	Object getWidgetValue(int x, int y);
	Rectangle getEditorBounds(int x, int y);
}
