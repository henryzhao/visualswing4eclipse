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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListModel;

import org.dyno.visual.swing.plugin.spi.Editor;

public class JListAdapter extends ComplexWidgetAdapter {
	private static int VAR_INDEX = 0;

	public JListAdapter() {
		super("jList" + (VAR_INDEX++));
	}

	protected JComponent createWidget() {
		JList jc = new JList();
		DefaultListModel dlm = new DefaultListModel();
		dlm.addElement("item0");
		dlm.addElement("item1");
		dlm.addElement("item2");
		dlm.addElement("item3");
		jc.setModel(dlm);
		Dimension size = new Dimension(100, 135);
		jc.setSize(size);
		jc.doLayout();
		jc.validate();
		return jc;
	}

	private Editor editor;

	@Override
	public Editor getEditorAt(int x, int y) {
		if (editor == null) {
			editor = new TextAreaEditor();
		}
		return editor;
	}

	@Override
	public Object getWidgetValue() {
		ListModel model = ((JList) getWidget()).getModel();
		int size = model.getSize();
		String items = "";
		for (int i = 0; i < size; i++) {
			if (i == 0)
				items += model.getElementAt(i);
			else
				items += "\n " + model.getElementAt(i);
		}
		return items;
	}

	@Override
	public void setWidgetValue(Object value) {
		if (value == null)
			((JList) getWidget()).setModel(new DefaultListModel());
		else {
			String items = (String) value;
			if (items.trim().length() == 0)
				((JList) getWidget()).setModel(new DefaultListModel());
			else {
				BufferedReader br = new BufferedReader(new StringReader(items));
				DefaultListModel model = new DefaultListModel();
				String token;
				try {
					while ((token = br.readLine()) != null) {
						model.addElement(token.trim());
					}
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				((JList) getWidget()).setModel(model);
			}
		}
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		return new Rectangle(0, 0, w, h);
	}

}
