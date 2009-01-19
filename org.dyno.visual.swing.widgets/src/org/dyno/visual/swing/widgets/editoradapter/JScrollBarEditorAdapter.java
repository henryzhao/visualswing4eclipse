package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;

import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.editors.TransparentScrollBarEditor;

public class JScrollBarEditorAdapter extends WidgetEditorAdapter {

	private IEditor iEditor;

	@Override
	public IEditor getEditorAt() {
		JScrollBar bar = (JScrollBar) adaptable.cloneWidget();
		iEditor = new TransparentScrollBarEditor(bar);
		return iEditor;
	}

	@Override
	public Object getWidgetValue() {
		return ((JScrollBar) adaptable.getWidget()).getValue();
	}

	@Override
	public void setWidgetValue(Object value) {
		((JScrollBar) adaptable.getWidget()).setValue(((Number) value).intValue());
	}

	@Override
	public Rectangle getEditorBounds() {
		return SwingUtilities.getLocalBounds(adaptable.getWidget());
	}

}
