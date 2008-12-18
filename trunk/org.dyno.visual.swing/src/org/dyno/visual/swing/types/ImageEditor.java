
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

package org.dyno.visual.swing.types;

import org.dyno.visual.swing.base.ResourceImage;
import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class ImageEditor extends ImageWrapper implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		CellEditor editor = new TextCellEditor(parent);
		editor.setValidator(new ImageIconValidator());
		return editor;
	}

	@Override
	public Object decodeValue(Object value) {
		if (value == null)
			return null;
		if (value.equals("null"))
			return null;
		String string = (String) value;
		return new ResourceImage(string);
	}

	@Override
	public Object encodeValue(Object value) {
		if (value == null)
			return "null";
		return value.toString();
	}
}

