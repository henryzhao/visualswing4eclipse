
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

package org.dyno.visual.swing.types.editor.spinnermodels;

import java.util.List;

import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.TypeAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.swt.widgets.Composite;

public class SpinnerListModelType extends SpinnerModelType {

	protected SpinnerListModelType() {
		super("list");
	}

	@Override
	public AccessibleUI createEditPane(Composite parent) {
		return new ListAccessible(parent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return "null";
		SpinnerListModel slm = (SpinnerListModel) value;
		String className = imports.addImport("javax.swing.SpinnerListModel");
		List list = slm.getList();
		StringBuilder builder = new StringBuilder();
		builder.append("new " + className + "(new Object[]{");
		for (Object item : list) {
			if (item == null)
				builder.append("null, ");
			else {
				if (item instanceof String) {
					builder.append("\"" + item + "\", ");
				} else {
					TypeAdapter type = ExtensionRegistry.getTypeAdapter(item.getClass());
					if (type != null && type.getCodegen() != null) {
						builder.append(type.getCodegen().getJavaCode(item, imports) + ", ");
					} else
						builder.append("" + item + ", ");
				}
			}
		}
		builder.append("})");
		return builder.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(SpinnerModel o1, SpinnerModel o2) {
		SpinnerListModel slm1 = (SpinnerListModel) o1;
		SpinnerListModel slm2 = (SpinnerListModel) o2;
		List list1 = slm1.getList();
		List list2 = slm2.getList();
		if (list1 == null) {
			if (list2 == null)
				return 0;
			else
				return 1;
		} else {
			if (list2 == null)
				return 1;
		}
		int size1 = list1.size();
		int size2 = list2.size();
		if (size1 != size2)
			return 1;
		for (int i = 0; i < size1; i++) {
			Object e1 = list1.get(i);
			Object e2 = list2.get(i);
			if (e1 == null) {
				if (e2 != null)
					return 1;
			} else {
				if (e2 == null)
					return 1;
				else {
					if (!e1.equals(e2))
						return 1;
				}
			}
		}
		return 0;
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	@Override
	public Object clone(Object object) {
		SpinnerListModel slm=(SpinnerListModel)object;		
		return new SpinnerListModel(slm.getList());
	}
}

