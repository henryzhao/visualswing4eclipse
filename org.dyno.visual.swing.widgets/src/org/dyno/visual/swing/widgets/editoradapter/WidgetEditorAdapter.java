package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.IEditorAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class WidgetEditorAdapter implements IEditorAdapter, IAdaptableContext {

	@Override
	public IEditor getEditorAt(int x, int y) {
		return null;
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		return null;
	}

	@Override
	public Object getWidgetValue(int x, int y) {
		return null;
	}

	@Override
	public void setWidgetValue(Object value) {
	}

	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adaptable = (WidgetAdapter) adaptable;
	}
	protected WidgetAdapter adaptable;
}
