
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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dyno.visual.swing.parser.NamespaceUtil;
import org.dyno.visual.swing.parser.ParserPlugin;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;

public class StandaloneClassModel extends AbstractClassModel {
	private Map<MethodDescriptor, MethodDescriptor> methods;
	private String className;
	private String parameters;
	private ICompilationUnit listenerUnit;

	public StandaloneClassModel() {
		methods = new HashMap<MethodDescriptor, MethodDescriptor>();
	}

	@Override
	public void init(WidgetAdapter adapter, EventSetDescriptor eventSet) {
		super.init(adapter, eventSet);
		this.className = (adapter.isRoot() ? "This" : getCapitalName(adapter.getID())) + getCapitalName(eventSet.getName()) + "Listener";
	}

	private String getCapitalName(String name) {
		return NamespaceUtil.getCapitalName(name);
	}

	@Override
	public void addMethod(MethodDescriptor methodDesc) {
		methods.put(methodDesc, methodDesc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean createEventMethod(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		IType meType = listenerUnit.getType(className);
		Class listClass = eventSet.getListenerType();		
		Class adapterClass = AnonymousInnerClassModel.getListenerAdapter(listClass);
		boolean override = adapterClass!=null;
		if (!meType.exists()) {
			StringBuilder builder = new StringBuilder();
			builder.append("class " + className);
			if (adapterClass == null) {
				String listClassname = listClass.getName();
				String cName = imports.addImport(listClassname);
				builder.append(" implements " + cName + " {\n");
			} else {
				String adapterClassname = adapterClass.getName();
				String cName = imports.addImport(adapterClassname);
				builder.append(" extends " + cName + " {\n");
			}
			builder.append("}\n");
			try {
				meType = type.getCompilationUnit().createType(builder.toString(), null, false, monitor);
			} catch (JavaModelException e) {
				ParserPlugin.getLogger().error(e);
				return false;
			}
		}
		MethodDescriptor[] all = eventSet.getListenerMethodDescriptors();
		for (MethodDescriptor mthd : all) {
			Method m = mthd.getMethod();
			Class[] ptypes = m.getParameterTypes();
			String pcName = ptypes[0].getName();
			pcName = imports.addImport(pcName);
			String pcSig = Signature.createTypeSignature(pcName, false);
			IMethod imthd = meType.getMethod(m.getName(), new String[] { pcSig });
			if (!imthd.exists()) {
				if (methods.get(mthd) != null) {
					StringBuilder builder = new StringBuilder();
					if (override)
						builder.append("@Override\n");
					builder.append(createEventMethodStub(monitor, meType, m, pcName));
					return createEventMethod(meType, builder.toString(), monitor);
				} else {
					if (!override) {
						return createEventMethod(meType, createEventMethodStub(monitor, meType, m, pcName), monitor);
					}
				}
			} else {
				if (methods.get(mthd) == null) {
					if (override) {
						try {
							imthd.delete(true, monitor);
						} catch (JavaModelException e) {
							ParserPlugin.getLogger().error(e);
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private boolean createEventMethod(IType meType, String code, IProgressMonitor monitor) {
		try {
			meType.createMethod(code, null, false, monitor);
			return true;
		} catch (JavaModelException e) {
			ParserPlugin.getLogger().error(e);
			return false;
		}
	}

	private String createEventMethodStub(IProgressMonitor monitor, IType meType, Method m, String pcName) {
		StringBuilder builder = new StringBuilder();
		builder.append("public void " + m.getName() + "(");
		builder.append(pcName + " event){\n");
		builder.append("}\n");
		return builder.toString();
	}

	@Override
	public String createListenerInstance(ImportRewrite imports) {
		return "new " + className + "(" + (parameters == null ? "" : parameters) + ")";
	}

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
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	@Override
	public String getDisplayName(MethodDescriptor methodDesc) {
		return className + "." + methodDesc.getDisplayName();
	}

	@Override
	public boolean hasMethod(MethodDescriptor methodDesc) {
		return methods.containsKey(methodDesc);
	}

	@Override
	public boolean isEmpty() {
		return methods.isEmpty();
	}

	@Override
	public Iterable<MethodDescriptor> methods() {
		return methods.keySet();
	}
	@Override
	@SuppressWarnings("unchecked")
	protected boolean processAddListenerStatement(TypeDeclaration type, WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener, MethodInvocation mi) {
		List arguments = mi.arguments();
		for (Object arg : arguments) {
			Expression argExpression = (Expression) arg;
			if (argExpression instanceof ClassInstanceCreation) {
				ClassInstanceCreation cic = (ClassInstanceCreation) argExpression;
				AnonymousClassDeclaration acd = cic.getAnonymousClassDeclaration();
				if (acd != null) {
					return false;
				} else {
					Type typeName = cic.getType();
					if (typeName instanceof SimpleType) {
						SimpleType simpleType = (SimpleType) typeName;
						className = simpleType.getName().getFullyQualifiedName();
						List args = cic.arguments();
						if (args != null && args.size() > 0) {
							StringBuilder builder = new StringBuilder();
							for (int i = 0; i < args.size(); i++) {
								Object para = args.get(i);
								if (i != 0)
									builder.append(",");
								builder.append(para);
							}
							parameters = builder.toString();
						} else
							parameters = null;
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
					} else
						return false;
				}
			} else
				return false;
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
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void removeMethod(MethodDescriptor methodDesc) {
		methods.remove(methodDesc);
	}
}

