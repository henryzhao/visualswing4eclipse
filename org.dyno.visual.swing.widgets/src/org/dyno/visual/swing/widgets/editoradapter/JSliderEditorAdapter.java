package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.TransparentSliderEditor;

public class JSliderEditorAdapter extends WidgetEditorAdapter {
	private IEditor editor;
	@Override
	public IEditor getEditorAt(int x, int y) {
		Component cloneWidget = adaptable.cloneWidget();		
		editor = new TransparentSliderEditor((JSlider) cloneWidget);
		return editor;
	}

	@Override
	public Object getWidgetValue(int x, int y) {
		return ((JSlider) adaptable.getWidget()).getValue();
	}

	@Override
	public void setWidgetValue(Object value) {
		((JSlider) adaptable.getWidget()).setValue(((Number) value).intValue());
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		return SwingUtilities.getLocalBounds(adaptable.getWidget());
	}
}
