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

	@Override
	public void addChangeListener(ChangeListener l) {
	}

	@Override
	public Component getComponent() {
		return slider;
	}

	@Override
	public Object getValue() {
		return slider.getValue();
	}

	@Override
	public void removeChangeListener(ChangeListener l) {
	}

	@Override
	public void setFocus() {
		slider.requestFocus();
		slider
				.setToolTipText(Messages.TransparentSliderEditor_Drag_Slider_Adjust_Value);
	}

	@Override
	public void setFont(Font f) {
		slider.setFont(f);
	}

	private Object old;

	@Override
	public void setValue(Object v) {
		this.old = v;
		int value = v == null ? 0 : ((Number) v).intValue();
		slider.setValue(value);
	}

	@Override
	public void validateValue() throws Exception {
	}

	@Override
	public Object getOldValue() {
		return old;
	}
}
