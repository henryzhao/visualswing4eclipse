package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;

import javax.swing.JSpinner;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.editors.IntegerTextEditor;

public class JSpinnerEditorAdapter extends WidgetEditorAdapter {

	private IEditor iEditor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		if (iEditor == null) {
			iEditor = new IntegerTextEditor();
		}
		return iEditor;
	}

	@Override
	public Object getWidgetValue(int x, int y) {
		return ((JSpinner) adaptable.getWidget()).getValue();
	}

	@Override
	public void setWidgetValue(Object value) {
		((JSpinner) adaptable.getWidget()).setValue(((Number) value).intValue());
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = adaptable.getWidget().getWidth();
		int h = adaptable.getWidget().getHeight();
		return new Rectangle(0, 0, w, h);
	}
}
