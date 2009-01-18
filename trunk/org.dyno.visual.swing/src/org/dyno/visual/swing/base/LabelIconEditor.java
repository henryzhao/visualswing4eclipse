package org.dyno.visual.swing.base;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;

import org.dyno.visual.swing.plugin.spi.IEditor;

public class LabelIconEditor extends JComponent implements IEditor {
	private static final long serialVersionUID = -4403435758517308113L;
	private ArrayList<ChangeListener> listeners;
	private JTextField field;
	private JButton button;
	public LabelIconEditor(){
		listeners = new ArrayList<ChangeListener>();
		field = new JTextField();
		button = new JButton();
		setLayout(new BorderLayout());
		add(field, BorderLayout.CENTER);
		add(button, BorderLayout.WEST);
	}
	@Override
	public void addChangeListener(ChangeListener l) {
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public Object getOldValue() {
		return old;
	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public void removeChangeListener(ChangeListener l) {
	}

	@Override
	public void setFocus() {
		field.requestFocus();
	}
	private Object old;
	@Override
	public void setValue(Object v) {
		old = v;
	}

	@Override
	public void validateValue() throws Exception {
	}

}
