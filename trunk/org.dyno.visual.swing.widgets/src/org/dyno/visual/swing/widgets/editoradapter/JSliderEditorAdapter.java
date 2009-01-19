package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.editors.TransparentSliderEditor;

public class JSliderEditorAdapter extends WidgetEditorAdapter {
	private IEditor editor;
	@Override
	public IEditor getEditorAt() {
		Component cloneWidget = adaptable.cloneWidget();		
		editor = new TransparentSliderEditor((JSlider) cloneWidget);
		return editor;
	}

	@Override
	public Object getWidgetValue() {
		return ((JSlider) adaptable.getWidget()).getValue();
	}

	@Override
	public void setWidgetValue(Object value) {
		((JSlider) adaptable.getWidget()).setValue(((Number) value).intValue());
	}

	@Override
	public Rectangle getEditorBounds() {
		return SwingUtilities.getLocalBounds(adaptable.getWidget());
	}
}
