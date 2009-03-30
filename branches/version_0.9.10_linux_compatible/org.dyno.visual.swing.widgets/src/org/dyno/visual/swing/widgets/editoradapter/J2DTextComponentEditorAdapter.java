package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;

import javax.swing.text.JTextComponent;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.JScrollPaneAdapter;
import org.dyno.visual.swing.widgets.editors.TextAreaEditor;

public class J2DTextComponentEditorAdapter extends ComplexWidgetEditorAdapter {

	private IEditor iEditor;

	@Override
	public IEditor getEditorAt() {
		if (iEditor == null) {
			iEditor = new TextAreaEditor();
		}
		return iEditor;
	}

	@Override
	public Object getWidgetValue() {
		return ((JTextComponent) adaptable.getWidget()).getText();
	}

	@Override
	public void setWidgetValue(Object value) {
		((JTextComponent) adaptable.getWidget()).setText(value == null ? "" : (String) value);
		((JTextComponent) adaptable.getWidget()).setCaretPosition(0);
	}

	@Override
	public Rectangle getEditorBounds() {
		WidgetAdapter parent = adaptable.getParentAdapter();
		int w, h;
		if (parent != null && parent instanceof JScrollPaneAdapter) {
			w = parent.getWidget().getWidth();
			h = parent.getWidget().getHeight();
		} else {
			w = adaptable.getWidget().getWidth();
			h = adaptable.getWidget().getHeight();
		}
		return new Rectangle(0, 0, w, h);
	}
}
