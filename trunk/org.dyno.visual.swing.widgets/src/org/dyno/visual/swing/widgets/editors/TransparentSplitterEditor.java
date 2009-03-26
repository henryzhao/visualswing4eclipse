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
	@Override
	public void addChangeListener(ChangeListener l) {
		splitter.addChangeListener(l);
	}

	@Override
	public Component getComponent() {
		return splitter;
	}

	@Override
	public Object getValue() {
		return splitter.getDividerLocation();
	}

	@Override
	public void removeChangeListener(ChangeListener l) {
		splitter.removeChangeListener(l);
	}

	@Override
	public void setFocus() {
		splitter.requestFocus();
	}

	@Override
	public void setFont(Font f) {
		splitter.setFont(f);
	}
	private Object old;
	@Override
	public void setValue(Object v) {
		old=v;
	}

	@Override
	public void validateValue() throws Exception {
	}
	@Override
	public Object getOldValue() {
		return old;
	}
}
