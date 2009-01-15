package org.dyno.visual.swing.widgets.editors;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JScrollBar;
import javax.swing.event.ChangeListener;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.Messages;

public class TransparentScrollBarEditor implements IEditor {
	private JScrollBar scrollBar;
	public TransparentScrollBarEditor(JScrollBar js){
		scrollBar = js;
	}
	@Override
	public void addChangeListener(ChangeListener l) {
	}

	@Override
	public Component getComponent() {
		return scrollBar;
	}

	@Override
	public Object getValue() {
		return scrollBar.getValue();
	}

	@Override
	public void removeChangeListener(ChangeListener l) {
	}

	@Override
	public void setFocus() {
		scrollBar.requestFocus();
		scrollBar.setToolTipText(Messages.TransparentScrollBarEditor_Drag_ScrollBar_Adjust_Value);
	}

	@Override
	public void setFont(Font f) {
		scrollBar.setFont(f);
	}
	private Object old_value;
	@Override
	public void setValue(Object v) {
		int value = v==null?0:((Number)v).intValue();
		scrollBar.setValue(value);
		this.old_value = v;
	}

	@Override
	public void validateValue() throws Exception {
	}
	@Override
	public Object getOldValue() {
		return old_value;
	}
}
