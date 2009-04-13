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
	
	public void setAdaptable(IAdaptable adaptable) {
		this.adaptable = (WidgetAdapter) adaptable;
	}
	
	public void setWidgetValue(Object value) {
	}
	
	public IEditor getEditorAt() {
		return null;
	}
	
	public Rectangle getEditorBounds() {
		return null;
	}
	
	public Object getWidgetValue() {
		return null;
	}
	
	public void setHotspot(Point hotspot) {
		this.hotspot = hotspot;
	}
}
