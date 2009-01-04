
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
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import javax.swing.JComponent;

import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;

public abstract class TextWidgetAdapter extends WidgetAdapter {
	public TextWidgetAdapter() {
		this.widget = createWidget();
		this.hotspotPoint = new Point(widget.getWidth() / 2, widget.getHeight() / 2);
		attach();
	}

	@Override
	protected Component newWidget() {
		try {
			return (JComponent)getWidgetClass().newInstance();
		} catch (Exception e) {
			WidgetPlugin.getLogger().error(e);
			return null;
		}
	}


	private PropertyDescriptor getTextProperty() {
		try {
			return new PropertyDescriptor("text", getWidgetClass());
		} catch (IntrospectionException e) {
			WidgetPlugin.getLogger().error(e);
			return null;
		}
	}

	private JComponent createWidgetByClass() {
		try {
			return (JComponent) getWidgetClass().newInstance();
		} catch (Exception e) {
			WidgetPlugin.getLogger().error(e);
			return null;
		}
	}

	protected Component createWidget() {
		JComponent jc = createWidgetByClass();
		setText(jc, getBasename());
		Dimension size = jc.getPreferredSize();		
		jc.setSize(size);
		jc.doLayout();
		jc.validate();
		return jc;
	}
	private String getText(Component jc) {
		PropertyDescriptor textProperty = getTextProperty();
		try {
			return (String) textProperty.getReadMethod().invoke(jc);
		} catch (Exception e) {
			WidgetPlugin.getLogger().error(e);
			return null;
		}
	}

	@Override
	public void requestNewName() {
		super.requestNewName();
		setText(getWidget(), getName());
	}

	private void setText(Component jc, String text) {
		PropertyDescriptor textProperty = getTextProperty();
		try {
			textProperty.getWriteMethod().invoke(jc, text);
		} catch (Exception e) {
			WidgetPlugin.getLogger().error(e);
		}
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
		return getText(getWidget());
	}

	@Override
	public void setWidgetValue(Object value) {
		setText(getWidget(), value == null ? "" : value.toString());
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		Component widget = getWidget();
		FontMetrics fm = widget.getFontMetrics(widget.getFont());
		int fh = fm.getHeight() + VER_TEXT_PAD;
		int fw = fm.stringWidth(getText(getWidget())) + HOR_TEXT_PAD;
		int fx = (w - fw) / 2;
		int fy = (h - fh) / 2;
		return new Rectangle(fx, fy, fw, fh);
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
		return 2 * (descent + fm.getAscent())- fm.getHeight();
	}

	@Override
	public void fillContextAction(MenuManager menu) {
		super.fillContextAction(menu);
		menu.add(new TextEditingAction());
	}

	class TextEditingAction extends Action {
		public TextEditingAction() {
			setText("Edit text ...");
			setId("EditingTextId");
		}

		public void run() {
			editValue();
		}
	}
	private static final int HOR_TEXT_PAD = 20;
	private static final int VER_TEXT_PAD = 4;
}

