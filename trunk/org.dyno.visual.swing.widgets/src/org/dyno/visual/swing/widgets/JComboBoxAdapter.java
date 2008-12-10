
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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.util.StringTokenizer;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JComboBoxAdapter extends WidgetAdapter {
	private static int VAR_INDEX = 0;

	public JComboBoxAdapter() {
		super("jComboBox" + (VAR_INDEX++));
	}

	protected Component createWidget() {
		JComboBox jc = new JComboBox();
		jc.setModel(new DefaultComboBoxModel(new Object[] { "item0", "item1", "item2", "item3" }));
		Dimension size = jc.getPreferredSize();
		jc.setSize(size);
		jc.doLayout();
		jc.validate();
		return jc;
	}

	private LabelEditor editor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		if (editor == null) {
			editor = new LabelEditor();
		}
		return editor;
	}

	@Override
	public Object getWidgetValue() {
		ComboBoxModel model = ((JComboBox) getWidget()).getModel();
		int size = model.getSize();
		String items = "";
		for (int i = 0; i < size; i++) {
			if (i == 0)
				items += model.getElementAt(i);
			else
				items += ", " + model.getElementAt(i);
		}
		return items;
	}

	@Override
	public void setWidgetValue(Object value) {
		if (value == null)
			((JComboBox) getWidget()).setModel(new DefaultComboBoxModel());
		else {
			String items = (String) value;
			if (items.trim().length() == 0)
				((JComboBox) getWidget()).setModel(new DefaultComboBoxModel());
			else {
				StringTokenizer tokenizer = new StringTokenizer(items, ",");
				DefaultComboBoxModel model = new DefaultComboBoxModel();
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					model.addElement(token.trim());
				}
				((JComboBox) getWidget()).setModel(model);
			}
		}
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		return new Rectangle(0, 0, w - 1, h - 1);
	}

	@Override
	public int getBaseline() {
		return getBaseline(getWidget().getHeight());
	}

	private Font getButtonFont() {
		Font f = getWidget().getFont();
		if (f == null)
			f = new Font("Dialog", 0, 12);
		return f;
	}

	@Override
	public int getHeightByBaseline(int baseline) {
		FontMetrics fm = getWidget().getFontMetrics(getButtonFont());
		return 2 * (baseline - fm.getAscent()) + fm.getHeight();
	}

	@Override
	public int getBaseline(int h) {
		FontMetrics fm = getWidget().getFontMetrics(getButtonFont());
		return (h - fm.getHeight()) / 2 + fm.getAscent();
	}

	@Override
	public int getHeightByDescent(int descent) {
		FontMetrics fm = getWidget().getFontMetrics(getButtonFont());
		return 2 * descent + fm.getHeight();
	}

	@Override
	protected Component newWidget() {
		return new JComboBox();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JComboBox.class;
	}

}

