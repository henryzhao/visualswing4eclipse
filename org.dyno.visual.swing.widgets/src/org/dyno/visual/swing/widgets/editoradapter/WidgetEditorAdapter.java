package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Point;
import java.awt.Rectangle;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.IEditorAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class WidgetEditorAdapter implements IEditorAdapter, IAdaptableContext {
	protected Point hotspot;
	protected WidgetAdapter adaptable;
	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adaptable = (WidgetAdapter) adaptable;
	}
	@Override
	public void setWidgetValue(Object value) {
	}
	@Override
	public IEditor getEditorAt() {
		return null;
	}
	@Override
	public Rectangle getEditorBounds() {
		return null;
	}
	@Override
	public Object getWidgetValue() {
		return null;
	}
	@Override
	public void setHotspot(Point hotspot) {
		this.hotspot = hotspot;
	}
}
