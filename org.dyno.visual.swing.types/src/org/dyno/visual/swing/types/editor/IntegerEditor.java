
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

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class IntegerEditor implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		CellEditor editor = new TextCellEditor(parent);
		editor.setValidator(new IntegerCellEditorValidator());
		return editor;
	}

	
	public Object decodeValue(Object value) {
		if (value == null)
			return 0;
		else {
			String sValue = value.toString().trim();
			return Integer.valueOf(sValue);
		}
	}

	
	public Object encodeValue(Object value) {
		if (value == null)
			return "0";
		else
			return value.toString();
	}

	
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	
	public String getJavaCode(Object value, ImportRewrite imports) {
		return value==null?"0":value.toString();
	}
}

