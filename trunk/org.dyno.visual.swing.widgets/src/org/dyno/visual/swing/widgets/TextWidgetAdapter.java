
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
import java.awt.Point;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import javax.swing.JComponent;

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
		requestGlobalNewName();
		setText(jc, getName());
		Dimension size = jc.getPreferredSize();		
		jc.setSize(size);
		jc.doLayout();
		jc.validate();
		return jc;
	}
	private PropertyDescriptor getTextProperty() {
		try {
			return new PropertyDescriptor("text", getWidgetClass()); //$NON-NLS-1$
		} catch (IntrospectionException e) {
			WidgetPlugin.getLogger().error(e);
			return null;
		}
	}

	private void setText(Component jc, String text) {
		PropertyDescriptor textProperty = getTextProperty();
		try {
			textProperty.getWriteMethod().invoke(jc, text);
		} catch (Exception e) {
			WidgetPlugin.getLogger().error(e);
		}
	}	
	@Override
	public void requestNewName() {
		if (getName() == null) {
			super.requestNewName();
			setText(getWidget(), getName());
		}
	}


	@Override
	public void fillContextAction(MenuManager menu) {
		super.fillContextAction(menu);
		menu.add(new TextEditingAction());
	}

	class TextEditingAction extends Action {
		public TextEditingAction() {
			setText(Messages.TextWidgetAdapter_Edit_Text);
			setId("EditingTextId"); //$NON-NLS-1$
		}

		public void run() {
			editValue();
		}
	}
}

