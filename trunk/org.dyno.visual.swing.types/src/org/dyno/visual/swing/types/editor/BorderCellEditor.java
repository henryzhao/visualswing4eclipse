/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor;

import javax.swing.JComponent;
import javax.swing.border.Border;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class BorderCellEditor extends DialogCellEditor {

	public BorderCellEditor(Object bean, Composite parent) {
		super(parent);
		if (bean == null) {
			setValue(bean);
		} else if (bean instanceof JComponent) {
			setValue(((JComponent) bean).getBorder());
		} else if (bean instanceof Border) {
			setValue(bean);
		}
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		BorderDialog bDialog = new BorderDialog(cellEditorWindow.getShell());
		bDialog.setBorder((Border)getValue());
		bDialog.open();
		if (bDialog.getReturnCode() == Window.OK) {
			Object value = bDialog.getBorder();
			if(value==null){
				markDirty();
				setValue(value);
                fireApplyEditorValue();
                return null;
			}else 
				return value;				
		} else
			return null;
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
