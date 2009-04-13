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
	
	public void addChangeListener(ChangeListener l) {
	}

	
	public Component getComponent() {
		return scrollBar;
	}

	
	public Object getValue() {
		return scrollBar.getValue();
	}

	
	public void removeChangeListener(ChangeListener l) {
	}

	
	public void setFocus() {
		scrollBar.requestFocus();
		scrollBar.setToolTipText(Messages.TransparentScrollBarEditor_Drag_ScrollBar_Adjust_Value);
	}

	
	public void setFont(Font f) {
		scrollBar.setFont(f);
	}
	private Object old_value;
	
	public void setValue(Object v) {
		int value = v==null?0:((Number)v).intValue();
		scrollBar.setValue(value);
		this.old_value = v;
	}

	
	public void validateValue() throws Exception {
	}
	
	public Object getOldValue() {
		return old_value;
	}
}
