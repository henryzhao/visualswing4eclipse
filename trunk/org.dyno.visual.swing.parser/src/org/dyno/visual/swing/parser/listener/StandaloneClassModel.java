
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

import org.dyno.visual.swing.parser.ParserPlugin;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;

public class StandaloneClassModel extends BaseClassModel {
	@Override
	public void editMethod(IEditorPart editor, MethodDescriptor methodDesc) {
		Class<?>[] pd = methodDesc.getMethod().getParameterTypes();
		if (pd.length > 0&&listenerUnit!=null) {
			String pname = pd[0].getName();
			int dot = pname.lastIndexOf('.');
			if (dot != -1)
				pname = pname.substring(dot + 1);
			
			String eventTypeSig = Signature.createTypeSignature(pname, false);
			IType meType = listenerUnit.getType(className);
			IMember member = meType.getMethod(methodDesc.getName(),new String[] {eventTypeSig });
			try {
				JavaUI.openInEditor((IJavaElement) member);
			} catch (Exception e) {
			   ParserPlugin.getLogger().error(e);
			}
		}		
	}
	@Override
	protected boolean processAddMethod(TypeDeclaration type, MethodDescriptor mListener) {
		IType declaringType=findDeclaringType(adapter);
		if(declaringType==null)
			return false;
		listenerUnit = declaringType.getCompilationUnit();
		try {
			String mListenerName = mListener.getName();
			IMethod[] mds = declaringType.getMethods();
			for (IMethod md : mds) {
				if (md.getElementName().equals(mListenerName)) {
					addMethod(mListener);
					return true;
				}
			}
		} catch (Exception e) {
			ParserPlugin.getLogger().error(e);
		}
		return false;
	}
	
	private IType findDeclaringType(WidgetAdapter adapter){
		ICompilationUnit unit = adapter.getCompilationUnit();
		if(unit==null)
			return null;
		IJavaProject javaPrj= unit.getJavaProject();
		try{
			IJavaElement[] children = javaPrj.getChildren();
			for(IJavaElement child:children){
				if(child instanceof IPackageFragmentRoot){
					IPackageFragmentRoot root=(IPackageFragmentRoot) child;
					if(!root.isArchive()){
						IJavaElement[] children2 = root.getChildren();
						for(IJavaElement child2:children2){
							if(child2 instanceof IPackageFragment){
								IPackageFragment pkg=(IPackageFragment) child2;
								IJavaElement[] children3 = pkg.getChildren();
								for(IJavaElement child3:children3){
									if(child3 instanceof ICompilationUnit){
										ICompilationUnit icu=(ICompilationUnit)child3;
										IType type2 = icu.getType(className);
										if(icu.getElementName().equals((className+".java"))&&type2.exists()){
											return type2;
										}
									}
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			ParserPlugin.getLogger().error(e);
		}
		return null;
	}
	private ICompilationUnit listenerUnit;
	@Override
	protected IType getMeType(IType type) {
		return listenerUnit.getType(className);
	}
}

