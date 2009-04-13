package org.dyno.visual.swing.widgets.editors;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.Messages;

public class TransparentSliderEditor implements IEditor {
	private JSlider slider;

	public TransparentSliderEditor(JSlider js) {
		slider = js;
	}

	
	public void addChangeListener(ChangeListener l) {
	}

	
	public Component getComponent() {
		return slider;
	}

	
	public Object getValue() {
		return slider.getValue();
	}

	
	public void removeChangeListener(ChangeListener l) {
	}

	
	public void setFocus() {
		slider.requestFocus();
		slider
				.setToolTipText(Messages.TransparentSliderEditor_Drag_Slider_Adjust_Value);
	}

	
	public void setFont(Font f) {
		slider.setFont(f);
	}

	private Object old;

	
	public void setValue(Object v) {
		this.old = v;
		int value = v == null ? 0 : ((Number) v).intValue();
		slider.setValue(value);
	}

	
	public void validateValue() throws Exception {
	}

	
	public Object getOldValue() {
		return old;
	}
}
