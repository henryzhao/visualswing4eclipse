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
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import org.dyno.visual.swing.plugin.spi.Editor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JProgressBarAdapter extends WidgetAdapter {
	private static int VAR_INDEX = 0;

	public JProgressBarAdapter() {
		super("jProgressBar" + (VAR_INDEX++));
	}

	@Override
	protected JComponent createWidget() {
		JProgressBar bar = new JProgressBar();
		Dimension size = bar.getPreferredSize();
		bar.setSize(size);
		bar.doLayout();
		bar.validate();
		return bar;
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
		return ((JProgressBar) getWidget()).getValue();
	}

	@Override
	public void setWidgetValue(Object value) {
		((JProgressBar) getWidget()).setValue(((Number) value).intValue());
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		int oritention = ((JProgressBar) getWidget()).getOrientation();
		if (oritention == SwingConstants.HORIZONTAL) {
			return new Rectangle((w - 40) / 2, 0, 40, 23);
		} else {
			return new Rectangle(0, (h - 23) / 2, w, 23);
		}
	}

	@Override
	protected JComponent newWidget() {
		return new JProgressBar();
	}

}
