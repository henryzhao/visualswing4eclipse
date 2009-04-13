
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

import org.dyno.visual.swing.parser.ParserPlugin;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

@SuppressWarnings("unchecked")
public class ThisClassModel extends AbstractClassModel {
	private Map<MethodDescriptor, MethodDescriptor> methods;

	public ThisClassModel() {
		methods = new HashMap<MethodDescriptor, MethodDescriptor>();
	}

	
	public void addMethod(MethodDescriptor methodDesc) {
		methods.put(methodDesc, methodDesc);
	}

	
	public boolean createEventMethod(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		String[] inters;
		try {
			inters = type.getSuperInterfaceNames();
		} catch (JavaModelException e) {
			ParserPlugin.getLogger().error(e);
			return false;
		}
		Class listClass = eventSet.getListenerType();
		String listClassname = listClass.getName();
		String cName = imports.addImport(listClassname);
		boolean hasInter = false;
		for (String inter : inters) {
			if (inter.equals(cName)) {
				hasInter = true;
			}
		}
		if (!hasInter) {
			addImplInterface(type, cName);
			Method[] listMethods = eventSet.getListenerMethods();
			for (Method mthd : listMethods) {
				addInterfaceMethod(type, imports, monitor, mthd);
			}
		}
		return true;
	}

	private void addInterfaceMethod(IType type, ImportRewrite imports,
			IProgressMonitor monitor, Method mthd) {
		String mName = mthd.getName();
		Class<?>[] pTypes = mthd.getParameterTypes();
		String eventName = pTypes[0].getName();
		eventName = imports.addImport(eventName);
		String eventSig = Signature.createTypeSignature(eventName, false);
		IMethod imthd = type.getMethod(mName, new String[] { eventSig });
		if (!imthd.exists()) {
			StringBuilder builder = new StringBuilder();
			builder.append("public void " + mName + "(");
			builder.append(eventName
					+ " event){\n//TODO Add event code here.\n}\n");
			String content = builder.toString();
			try {
				type.createMethod(content, null, false, monitor);
			} catch (Exception e) {
				ParserPlugin.getLogger().error(e);
			}
		}
	}

	private void addImplInterface(IType type, String cName) {
		try {
			ICompilationUnit icunit = type.getCompilationUnit();
			String source = icunit.getBuffer().getContents();
			Document document = new Document(source);
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setSource(icunit);
			CompilationUnit cunit = (CompilationUnit) parser.createAST(null);
			cunit.recordModifications();
			AST ast = cunit.getAST();
			TypeDeclaration typeDec = (TypeDeclaration) cunit.types().get(0);
			List list = typeDec.superInterfaceTypes();
			Name name = ast.newName(cName);
			Type interfaceType = ast.newSimpleType(name);
			list.add(interfaceType);
			TextEdit edits = cunit.rewrite(document, icunit.getJavaProject()
					.getOptions(true));
			edits.apply(document);
			String newSource = document.get();
			icunit.getBuffer().setContents(newSource);
		} catch (Exception e) {
			ParserPlugin.getLogger().error(e);
		}
	}

	
	public String createListenerInstance(ImportRewrite imports) {
		return "this";
	}

	
	public void editMethod(IEditorPart editor, MethodDescriptor methodDesc) {
		Class<?>[] pd = methodDesc.getMethod().getParameterTypes();
		if (pd.length > 0) {
			String pname = pd[0].getName();
			int dot = pname.lastIndexOf('.');
			if (dot != -1)
				pname = pname.substring(dot + 1);

			String eventTypeSig = Signature.createTypeSignature(pname, false);

			IFileEditorInput file = (IFileEditorInput) editor.getEditorInput();
			ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file
					.getFile());
			String name = file.getName();
			dot = name.lastIndexOf('.');
			if (dot != -1)
				name = name.substring(0, dot);
			IType type = unit.getType(name);
			IMember member = type.getMethod(methodDesc.getName(),
					new String[] { eventTypeSig });
			JavaUI.revealInEditor(editor, (IJavaElement) member);
		}
	}

	
	public String getDisplayName(MethodDescriptor methodDesc) {
		return methodDesc.getDisplayName();
	}

	
	public boolean hasMethod(MethodDescriptor methodDesc) {
		return methods.containsKey(methodDesc);
	}

	
	public boolean isEmpty() {
		return methods.isEmpty();
	}

	
	public Iterable<MethodDescriptor> methods() {
		return methods.keySet();
	}
	
	
	protected boolean processAddListenerStatement(TypeDeclaration type, WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener, MethodInvocation mi) {
		List arguments = mi.arguments();
		for (Object arg : arguments) {
			Expression argExpression = (Expression) arg;
			if (argExpression instanceof ThisExpression) {
				addMethod(mListener);
				return true;
			} else
				return false;
		}
		return false;
	}	
	
	public void removeMethod(MethodDescriptor methodDesc) {
		methods.remove(methodDesc);
	}

}

