
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

package org.dyno.visual.swing.types.editor;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.border.Border;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

@SuppressWarnings("unchecked")
public class BorderCellEditor extends DialogCellEditor {
	private JComponent component;
	public BorderCellEditor(Object bean, Composite parent) {
		super(parent);
		if (bean == null) {
			setValue(bean);
		} else if (bean instanceof JComponent) {
			setValue(((JComponent) bean).getBorder());
			this.component = (JComponent)bean;
		} else if (bean instanceof Border) {
			setValue(bean);
		} else if(bean instanceof List){
			List beans=(List)bean;
			if(!beans.isEmpty()){
				bean=beans.get(0);
				if(bean instanceof JComponent){
					setValue(((JComponent) bean).getBorder());
					this.component = (JComponent)bean;
				} else if (bean instanceof Border) {
					setValue(bean);
				}
			}
		}
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		BorderDialog bDialog = new BorderDialog(cellEditorWindow.getShell());
		bDialog.setBorder((Border)getValue());
		bDialog.open();
		if (bDialog.getReturnCode() == Window.OK) {
			Object value = bDialog.getBorder();
            if(component!=null){
            	changeComponentDirtyFlag();
            }
			if(value==null){
				markDirty();
				setValue(value);
                fireApplyEditorValue();
                return null;
			}else {
				return value;
			}
		} else
			return null;
	}

	private void changeComponentDirtyFlag() {
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(component);
		if(adapter!=null){
			adapter.setDirty(true);
			adapter.repaintDesigner();
		}			
	}

	protected Button createButton(Composite parent) {
		Button result = new Button(parent, SWT.DOWN);
		result.setText("."); //$NON-NLS-1$
		return result;
	}

	protected void updateContents(Object value) {
		Label lblText = getDefaultLabel();
		if (value == null)
			lblText.setText("null");
		else {
			String name = value.getClass().getName();
			int dot = name.lastIndexOf('.');
			if (dot != -1)
				name = name.substring(dot + 1);
			lblText.setText(name);
		}
	}
}

