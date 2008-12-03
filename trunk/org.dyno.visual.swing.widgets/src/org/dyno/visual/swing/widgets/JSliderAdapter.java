
/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JSlider;
import javax.swing.SwingConstants;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JSliderAdapter extends WidgetAdapter {
	private static int VAR_INDEX = 0;

	public JSliderAdapter() {
		super("jSlider" + (VAR_INDEX++));
	}

	@Override
	protected Component createWidget() {
		JSlider bar = new JSlider();
		Dimension size = bar.getPreferredSize();
		bar.setSize(size);
		bar.doLayout();
		bar.validate();
		return bar;
	}

	private IEditor iEditor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		if (iEditor == null) {
			iEditor = new IntegerTextEditor();
		}
		return iEditor;
	}

	@Override
	public Object getWidgetValue() {
		return ((JSlider) getWidget()).getValue();
	}

	@Override
	public void setWidgetValue(Object value) {
		((JSlider) getWidget()).setValue(((Number) value).intValue());
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		int oritention = ((JSlider) getWidget()).getOrientation();
		if (oritention == SwingConstants.HORIZONTAL) {
			return new Rectangle((w - 40) / 2, 0, 40, 23);
		} else {
			return new Rectangle(0, (h - 23) / 2, w, 23);
		}
	}

	@Override
	protected Component newWidget() {
		return new JSlider();
	}
	@Override
	public Class getWidgetClass() {
		return JSlider.class;
	}

}

