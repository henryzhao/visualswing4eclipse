
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

package org.dyno.visual.swing.parser.listener;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;

import org.dyno.visual.swing.parser.NamespaceUtil;
import org.dyno.visual.swing.parser.ParserPlugin;
import org.dyno.visual.swing.plugin.spi.IConstants;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

@SuppressWarnings("unchecked")
public class CodeSnippet implements IEventMethod, IConstants{
	private WidgetAdapter adapter;
	private EventSetDescriptor eventSet;
	private MethodDescriptor methodDesc;
	private String code;
	public CodeSnippet(WidgetAdapter adapter, EventSetDescriptor eventSet,
			MethodDescriptor methodDesc, String code) {
		this.adapter = adapter;
		this.eventSet = eventSet;
		this.methodDesc = methodDesc;
		this.code = code;
	}
	private String getGetMethodName(String name) {
		return NamespaceUtil.getGetMethodName(name);
	}

	@Override
	public void editCode(IEditorPart editor) {
		IFileEditorInput file = (IFileEditorInput) editor.getEditorInput();
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file
				.getFile());
		try {
			String name = file.getName();
			int dot = name.lastIndexOf('.');
			if (dot != -1)
				name = name.substring(0, dot);
			IType type = unit.getType(name);
			String mName = adapter.isRoot() ? INIT_METHOD_NAME
					: getGetMethodName(adapter.getID());
			IMethod method = type.getMethod(mName, new String[0]);
			IJavaElement[] children = method.getChildren();
			for (IJavaElement javaElement : children) {
				if (javaElement instanceof IType) {
					IType anonymous = (IType) javaElement;
					if (isTargetClass(adapter, eventSet, anonymous)) {
						mName = methodDesc.getName();
						Class<?>[] pts = methodDesc.getMethod()
								.getParameterTypes();
						String param = pts[0].getName();
						dot = param.lastIndexOf('.');
						if (dot != -1)
							param = param.substring(dot + 1);
						String sig = Signature
								.createTypeSignature(param, false);
						IMember member = anonymous.getMethod(mName,
								new String[] { sig });
						JavaUI.revealInEditor(editor, (IJavaElement) member);
					}
				}
			}
		} catch (Exception e) {
			ParserPlugin.getLogger().error(e);
		}
	}

	private boolean isTargetClass(final WidgetAdapter adapter,
			final EventSetDescriptor eventSet, IType anonymous) {
		try {
			Class clazz = eventSet.getListenerType();
			Class adapterClass = DelegationModel
					.getListenerAdapter(clazz);
			String superClassname = anonymous.getSuperclassName();
			boolean targetClass = false;
			if (superClassname == null) {
				String[] sis = anonymous.getSuperInterfaceTypeSignatures();
				if (sis.length > 0) {
					String interfaceName = sis[0];
					if (clazz.getName().indexOf(interfaceName) != -1) {
						targetClass = true;
					}
				}
			} else if (adapterClass != null) {
				String adapterClassname = adapterClass.getName();
				if (adapterClassname.indexOf(superClassname) != -1) {
					targetClass = true;
				}
			} else if (clazz.getName().indexOf(superClassname) != -1) {
				targetClass = true;
			}
			return targetClass;
		} catch (Exception e) {
			ParserPlugin.getLogger().error(e);
			return false;
		}
	}

	@Override
	public String getDisplayName() {
		return methodDesc.getDisplayName();
	}

	@Override
	public String createEventMethod(IType type, ImportRewrite imports) {
		return null;
	}

	@Override
	public String createAddListenerCode() {
		return code;
	}
}

