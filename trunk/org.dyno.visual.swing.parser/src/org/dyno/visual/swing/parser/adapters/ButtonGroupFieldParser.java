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

package org.dyno.visual.swing.parser.adapters;

import java.awt.Component;
import java.lang.reflect.Field;

import javax.swing.ButtonGroup;

import org.dyno.visual.swing.parser.NamespaceUtil;
import org.dyno.visual.swing.parser.ParserPlugin;
import org.dyno.visual.swing.parser.spi.IFieldParser;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class ButtonGroupFieldParser implements IFieldParser {

	@Override
	public void parseField(CompilationUnit cunit, Component bean, Field field) {
		if (ButtonGroup.class.isAssignableFrom(field.getType())) {
			try {
				field.setAccessible(true);
				Object fieldValue = field.get(bean);
				String fieldName = field.getName();
				ButtonGroup group = (ButtonGroup) fieldValue;
				WidgetAdapter rootAdapter = WidgetAdapter
						.getWidgetAdapter(bean);
				rootAdapter.addInvisible(fieldName, group);
			} catch (Exception e) {
				ParserPlugin.getLogger().error(e);
			}
		}
	}

	private boolean acceptTypeSignature(String sig) {
		return sig.indexOf("ButtonGroup") != -1;
	}

	@Override
	public boolean isDesigningField(IType type, IField field) {
		try {
			String sig = field.getTypeSignature();
			if (acceptTypeSignature(sig)) {
				String fieldName = field.getElementName();
				String getMethodName = NamespaceUtil.getInitMethodName(fieldName);
				IMethod method = type.getMethod(getMethodName, new String[0]);
				if (method != null && method.exists()) {
					return true;
				}
			}
		} catch (JavaModelException e) {
			ParserPlugin.getLogger().error(e);
		}
		return false;
	}

	@Override
	public boolean removeField(IType type, String fieldName,
			IProgressMonitor monitor) {
		IMethod method = type.getMethod(NamespaceUtil.getInitMethodName(fieldName), new String[0]);
		if (method != null && method.exists()) {
			try {
				method.delete(true, monitor);
				return true;
			} catch (JavaModelException e) {
				ParserPlugin.getLogger().error(e);
			}
		}
		return false;
	}
}
