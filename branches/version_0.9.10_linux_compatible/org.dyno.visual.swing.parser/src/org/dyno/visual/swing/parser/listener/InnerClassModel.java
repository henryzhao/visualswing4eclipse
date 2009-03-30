
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

import java.beans.MethodDescriptor;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

public class InnerClassModel extends BaseClassModel {
	@Override
	public void editMethod(IEditorPart editor, MethodDescriptor methodDesc) {
		Class<?>[] pd = methodDesc.getMethod().getParameterTypes();
		if (pd.length > 0) {
			String pname = pd[0].getName();
			int dot = pname.lastIndexOf('.');
			if (dot != -1)
				pname = pname.substring(dot + 1);
			
			String eventTypeSig = Signature.createTypeSignature(pname, false);
			
			IFileEditorInput file = (IFileEditorInput) editor.getEditorInput();
			ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file.getFile());
			String name = file.getName();
			dot = name.lastIndexOf('.');
			if (dot != -1)
				name = name.substring(0, dot);
			IType type = unit.getType(name);
			IType meType = type.getType(className);			
			IMember member = meType.getMethod(methodDesc.getName(),new String[] {eventTypeSig });
			JavaUI.revealInEditor(editor, (IJavaElement) member);
		}		
	}
}

