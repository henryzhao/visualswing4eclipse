
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

import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JScrollBarAdapter extends WidgetAdapter {
	public JScrollBarAdapter() {
		super(null);
	}

	@Override
	protected Component createWidget() {
		JScrollBar bar = new JScrollBar();
		Dimension size = bar.getPreferredSize();
		bar.setSize(size);
		bar.doLayout();
		bar.validate();
		return bar;
	}

	private IEditor iEditor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		JScrollBar bar = (JScrollBar) cloneWidget();
		iEditor = new TransparentScrollBarEditor(bar);
		return iEditor;
	}

	@Override
	public Object getWidgetValue(int x, int y) {
		return ((JScrollBar) getWidget()).getValue();
	}

	@Override
	public void setWidgetValue(Object value) {
		((JScrollBar) getWidget()).setValue(((Number) value).intValue());
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		return SwingUtilities.getLocalBounds(getWidget());
	}

	@Override
	protected Component newWidget() {
		return new JScrollBar();
	}
	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JScrollBar.class;
	}
}

