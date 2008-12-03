
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
import org.dyno.visual.swing.types.endec.StringWrapper;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class StringEditor extends StringWrapper implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		return new TextCellEditor(parent);
	}

	@Override
	public Object decodeValue(Object value) {
		if (value == null || value != null && value.toString().trim().length() == 0)
			return null;
		else
			return value;
	}

	@Override
	public Object encodeValue(Object value) {
		if (value == null)
			return "";
		else
			return value;
	}
}

