package org.dyno.visual.swing.widgets;

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
	}

	@Override
	public void setFocus() {
		splitter.requestFocus();
	}

	@Override
	public void setFont(Font f) {
		splitter.setFont(f);
	}

	@Override
	public void setValue(Object v) {
	}

	@Override
	public void validateValue() throws Exception {
	}
}
