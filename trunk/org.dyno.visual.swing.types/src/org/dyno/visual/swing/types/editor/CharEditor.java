
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

public class CharEditor implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		CellEditor editor = new TextCellEditor(parent);
		editor.setValidator(new CharCellEditorValidator());
		return editor;
	}

	
	public Object decodeValue(Object value) {
		if (value == null)
			return '\0';
		else if (value.equals("\\0")) {
			return '\0';
		} else {
			String sValue = value.toString().trim();
			return sValue.charAt(0);
		}
	}

	
	public Object encodeValue(Object value) {
		if (value == null) {
			return "\\0";
		} else if (value instanceof Character) {
			Character character = (Character) value;
			if (character.charValue() == '\0')
				return "\\0";
			else
				return character.toString();
		}
		return value.toString();
	}

	
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	
	public String getJavaCode(Object value, ImportRewrite imports) {
		if(value==null)
			return "\'\\0\'";
		return "\'"+value+"\'";
	}
}

