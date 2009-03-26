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

package org.dyno.visual.swing.widgets.undo;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.ICellEditorValidator;

public class BeanNameValidator implements ICellEditorValidator {
	private WidgetAdapter adapter;
	public BeanNameValidator(WidgetAdapter adapter){
		this.adapter = adapter;
	}
	@Override
	public String isValid(Object value) {
		String name = (String) value;
		try {
			validateName(name);
			return null;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	private void validateName(String newName) throws Exception {
		if (newName == null || newName.trim().length() == 0)
			throw new Exception(Messages.BeanNameValidator_Non_Empty_Name);
		char ch = newName.charAt(0);
		if (!Character.isJavaIdentifierStart(ch)) {
			throw new Exception(Messages.BeanNameValidator_Illegal_Var_Name);
		}
		int index = 1;
		while (index < newName.length()) {
			ch = newName.charAt(index++);
			if (!Character.isJavaIdentifierPart(ch))
				throw new Exception(Messages.BeanNameValidator_Illegal_Var_Name);
		}
		if (!newName.equals(adapter.getName()) && adapter.getNamespace().hasDeclaredName(newName))
			throw new Exception(Messages.BeanNameValidator_Used_Var_Name);
	}
}

