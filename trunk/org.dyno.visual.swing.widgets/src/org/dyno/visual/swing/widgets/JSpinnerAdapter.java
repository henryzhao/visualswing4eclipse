/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JSpinner;

import org.dyno.visual.swing.plugin.spi.Editor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JSpinnerAdapter extends WidgetAdapter {
	private static int VAR_INDEX = 0;

	public JSpinnerAdapter() {
		super("jSpinner" + (VAR_INDEX++));
	}

	@Override
	protected JComponent createWidget() {
		JSpinner spinner = new JSpinner();
		Dimension size = spinner.getPreferredSize();
		spinner.setSize(size);
		spinner.doLayout();
		spinner.validate();
		return spinner;
	}

	private Editor editor;

	@Override
	public Editor getEditorAt(int x, int y) {
		if (editor == null) {
			editor = new IntegerTextEditor();
		}
		return editor;
	}

	@Override
	public Object getWidgetValue() {
		return ((JSpinner) getWidget()).getValue();
	}

	@Override
	public void setWidgetValue(Object value) {
		((JSpinner) getWidget()).setValue(((Number) value).intValue());
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		return new Rectangle(0, 0, w, h);
	}

	@Override
	protected JComponent newWidget() {
		return new JSpinner();
	}

}
