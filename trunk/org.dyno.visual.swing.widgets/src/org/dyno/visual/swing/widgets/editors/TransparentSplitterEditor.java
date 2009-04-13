package org.dyno.visual.swing.widgets.editors;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JSplitPane;
import javax.swing.event.ChangeListener;

import org.dyno.visual.swing.plugin.spi.IEditor;

public class TransparentSplitterEditor implements IEditor {
	private TransparentSplitter splitter;
	public TransparentSplitterEditor(JSplitPane jsp){
		splitter = new TransparentSplitter(jsp);
	}
	
	public void addChangeListener(ChangeListener l) {
		splitter.addChangeListener(l);
	}

	
	public Component getComponent() {
		return splitter;
	}

	
	public Object getValue() {
		return splitter.getDividerLocation();
	}

	
	public void removeChangeListener(ChangeListener l) {
		splitter.removeChangeListener(l);
	}

	
	public void setFocus() {
		splitter.requestFocus();
	}

	
	public void setFont(Font f) {
		splitter.setFont(f);
	}
	private Object old;
	
	public void setValue(Object v) {
		old=v;
	}

	
	public void validateValue() throws Exception {
	}
	public Object getOldValue() {
		return old;
	}
}
