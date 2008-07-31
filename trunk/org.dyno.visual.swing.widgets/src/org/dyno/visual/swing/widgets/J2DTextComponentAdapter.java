/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.text.JTextComponent;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public abstract class J2DTextComponentAdapter extends ComplexWidgetAdapter {
	private static int VAR_INDEX = 0;

	public J2DTextComponentAdapter(String varName) {
		super(varName + (VAR_INDEX++));
	}

	protected Component createWidget() {
		JTextComponent jtc = createTextComponent();
		jtc.setText(name);
		Dimension size = new Dimension(100, 100);
		jtc.setSize(size);
		jtc.doLayout();
		jtc.validate();
		return jtc;
	}

	protected abstract JTextComponent createTextComponent();

	private IEditor iEditor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		if (iEditor == null) {
			iEditor = new TextAreaEditor();
		}
		return iEditor;
	}

	@Override
	public Object getWidgetValue() {
		return ((JTextComponent) getWidget()).getText();
	}

	@Override
	public void setWidgetValue(Object value) {
		((JTextComponent) getWidget()).setText(value == null ? "" : (String) value);
		((JTextComponent) getWidget()).setCaretPosition(0);
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		WidgetAdapter parent = getParentAdapter();
		int w, h;
		if (parent != null && parent instanceof JScrollPaneAdapter) {
			w = parent.getWidget().getWidth();
			h = parent.getWidget().getHeight();
		} else {
			w = getWidget().getWidth();
			h = getWidget().getHeight();
		}
		return new Rectangle(0, 0, w, h);
	}
}
