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

package org.dyno.visual.swing.parser;

import java.awt.Component;
import java.beans.EventSetDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.dyno.visual.swing.base.AwtEnvironment;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.ISyncUITask;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.base.WidgetProperty;
import org.dyno.visual.swing.parser.spi.IFieldParser;
import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IConstants;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;
import org.dyno.visual.swing.plugin.spi.ISourceParser;
import org.dyno.visual.swing.plugin.spi.IValueParser;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.ui.CodeStyleConfiguration;
import org.eclipse.jdt.ui.actions.OrganizeImportsAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * DefaultSourceParser
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
class DefaultSourceParser implements ISourceParser, IConstants {
	private static final String FIELD_PARSER_EXTENSION_ID = "org.dyno.visual.swing.parser.fieldParser"; //$NON-NLS-1$
	private DefaultParserFactory factory;
	private List<IFieldParser> fieldParsers;

	DefaultSourceParser(DefaultParserFactory factory) {
		this.factory = factory;
		fieldParsers = new ArrayList<IFieldParser>();
		parseFieldParsers();
	}

	private void parseFieldParsers() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
				.getExtensionPoint(FIELD_PARSER_EXTENSION_ID);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseFieldParser(extensions[i]);
				}
			}
		}
	}

	private void parseFieldParser(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("parser")) { //$NON-NLS-1$
					try {
						fieldParsers.add((IFieldParser) configs[i]
								.createExecutableExtension("class")); //$NON-NLS-1$
					} catch (CoreException e) {
						ParserPlugin.getLogger().error(e);
					}
				}
			}
		}
	}

	@Override
	public WidgetAdapter parse(ICompilationUnit unit, IProgressMonitor monitor) {
		try {
			IType[] types = unit.getPrimary().getAllTypes();
			for (IType type : types) {
				if (type.isClass() && Flags.isPublic(type.getFlags())) {
					WidgetAdapter result = processType(unit.getPrimary(), type);
					if (result != null)
						return result;
				}
			}
		} catch (Exception e) {
			ParserPlugin.getLogger().error(e);
		}
		return null;
	}
	private WidgetAdapter processType(ICompilationUnit unit, IType type)
			throws JavaModelException {
		try {
			IJavaProject java_project = type.getJavaProject();
			String className = type.getFullyQualifiedName();
            java_project.getProject().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);			String[] classPath = JavaRuntime.computeDefaultRuntimeClassPath(java_project);
			URL[] urls = new URL[classPath.length];
			for(int i=0;i<classPath.length;i++){
				File cp = new File(classPath[i]);
				if(cp.exists())
					urls[i]=cp.toURI().toURL();
			}
			final Class<?> beanClass = new URLClassLoader(urls, getClass().getClassLoader()).loadClass(className);
			if (Component.class.isAssignableFrom(beanClass)) {
				String lnf = getBeanClassLnf(beanClass);
				ILookAndFeelAdapter lnfAdapter = ExtensionRegistry
						.getLnfAdapter(lnf);
				if (lnfAdapter != null) {
					LookAndFeel newlnf = lnfAdapter.getLookAndFeelInstance();
					Component bean = (Component) AwtEnvironment.runWithLnf(
							newlnf, new ISyncUITask() {
								@Override
								public Object doTask() throws Throwable {
									return beanClass.newInstance();
								}
							});
					WidgetAdapter beanAdapter = ExtensionRegistry
							.createWidgetAdapter(bean);
					ASTParser parser = ASTParser.newParser(AST.JLS3);
					parser.setSource(unit);
					CompilationUnit cunit = (CompilationUnit) parser
							.createAST(null);
					parseEventListener(cunit, beanAdapter);
					initDesignedWidget(cunit, bean);
					parsePropertyValue(lnf, cunit, beanAdapter);
					beanAdapter.clearDirty();
					return beanAdapter;
				}
			}
		} catch (Throwable e) {
			ParserPlugin.getLogger().error(e);
		}
		return null;
	}
	private void parsePropertyValue(String lnfClassname, CompilationUnit cunit,
			WidgetAdapter adapter) {
		parseWidgetProperty(lnfClassname, cunit, adapter);
		if (adapter instanceof CompositeAdapter) {
			CompositeAdapter compositeAdapter = (CompositeAdapter) adapter;
			int count = compositeAdapter.getChildCount();
			for (int i = 0; i < count; i++) {
				Component child = compositeAdapter.getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter
						.getWidgetAdapter(child);
				parsePropertyValue(lnfClassname, cunit, childAdapter);
			}
		}
	}

	private MethodDeclaration getMethodDeclaration(TypeDeclaration type,
			String name) {
		MethodDeclaration[] methods = type.getMethods();
		for (MethodDeclaration method : methods) {
			if (method.getName().getFullyQualifiedName().equals(name))
				return method;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void parseWidgetProperty(String lnfClassname,
			CompilationUnit cunit, WidgetAdapter adapter) {
		TypeDeclaration type = (TypeDeclaration) cunit.types().get(0);
		ArrayList<IWidgetPropertyDescriptor> properties = adapter
				.getPropertyDescriptors();
		Object bean = adapter.getWidget();
		IStructuredSelection sel = new StructuredSelection(bean);
		for (IWidgetPropertyDescriptor property : properties) {
			if (property.isPropertySet(lnfClassname, sel)
					&& property instanceof WidgetProperty) {
				List statements = getBeanPropertyInitStatements(adapter, type);
				for (Object stmt : statements) {
					Statement statement = (Statement) stmt;
					checkSatement(bean, property, statement);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void checkSatement(Object bean, IWidgetPropertyDescriptor property,
			Statement statement) {
		if (statement instanceof ExpressionStatement) {
			ExpressionStatement expressionStatement = (ExpressionStatement) statement;
			Expression expression = expressionStatement.getExpression();
			if (expression instanceof MethodInvocation) {
				MethodInvocation mi = (MethodInvocation) expression;
				String setMethodName = mi.getName().getFullyQualifiedName();
				WidgetProperty wp = (WidgetProperty) property;
				String writeMethodName = wp.getPropertyDescriptor()
						.getWriteMethod().getName();
				if (setMethodName.equals(writeMethodName)) {
					List args = mi.arguments();
					IValueParser vp = property.getValueParser();
					if (vp != null) {
						Object oldValue = property.getFieldValue(bean);
						Object newValue = vp.parseValue(oldValue, args);
						property.setFieldValue(bean, newValue);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List getBeanPropertyInitStatements(WidgetAdapter adapter, TypeDeclaration type) {
		List statements;
		if (adapter.isRoot()) {
			MethodDeclaration initMethod = getMethodDeclaration(type,INIT_METHOD_NAME);
			Block body = initMethod.getBody();
			statements = body.statements();
		} else {
			String getMethodName = NamespaceUtil.getGetMethodName(adapter.getID());
			MethodDeclaration getMethod = getMethodDeclaration(type, getMethodName);
			if (getMethod != null) {
				Block body = getMethod.getBody();
				statements = body.statements();
				IfStatement ifs = (IfStatement) statements.get(0);
				Statement thenstmt = ifs.getThenStatement();
				if (thenstmt instanceof Block) {
					Block block = (Block) thenstmt;
					statements = block.statements();
				}
			}else{				
				MethodDeclaration initMethod = getMethodDeclaration(type,INIT_METHOD_NAME);
				Block body = initMethod.getBody();
				statements = body.statements();
			}
		}
		return statements;
	}

	@SuppressWarnings("unchecked")
	private void initDesignedWidget(CompilationUnit cunit, Component bean) {
		Class clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				Object fieldValue = field.get(bean);
				if (fieldValue != null) {
					if (JComponent.class.isAssignableFrom(field.getType())) {
						parseField(cunit, bean, field);
					} else {
						for (IFieldParser parser : fieldParsers) {
							parser.parseField(cunit, bean, field);
						}
					}
				}
			} catch (Exception e) {
				ParserPlugin.getLogger().error(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void parseField(CompilationUnit cunit, Component bean, Field field) {
		Class clazz = bean.getClass();
		String fieldName = field.getName();
		field.setAccessible(true);
		Object fieldValue = null;
		try {
			fieldValue = field.get(bean);
		}catch(Exception e){
			ParserPlugin.getLogger().error(e);
			return;
		}
		JComponent fieldComponent = (JComponent) fieldValue;
		WidgetAdapter adapter = ExtensionRegistry.createWidgetAdapter(fieldComponent);
		try{
			String widgetName = getNameFromFieldName(fieldName);
			adapter.setName(widgetName);
			adapter.setLastName(widgetName);
			int flags = field.getModifiers();
			if (Modifier.isPrivate(flags)) {
				adapter.setFieldAccess(WidgetAdapter.ACCESS_PRIVATE);
			} else if (Modifier.isProtected(flags)) {
				adapter.setFieldAccess(WidgetAdapter.ACCESS_PROTECTED);
			} else if (Modifier.isPublic(flags)) {
				adapter.setFieldAccess(WidgetAdapter.ACCESS_PUBLIC);
			} else {
				adapter.setFieldAccess(WidgetAdapter.ACCESS_DEFAULT);
			}
		}catch(Exception e){
			ParserPlugin.getLogger().warning(e);
		}
		try{
			String getName = getGetMethodName(fieldName);
			Method getMethod = clazz.getDeclaredMethod(getName);
			int flags = getMethod.getModifiers();
			if (Modifier.isPrivate(flags)) {
				adapter.setGetAccess(WidgetAdapter.ACCESS_PRIVATE);
			} else if (Modifier.isProtected(flags)) {
				adapter.setGetAccess(WidgetAdapter.ACCESS_PROTECTED);
			} else if (Modifier.isPublic(flags)) {
				adapter.setGetAccess(WidgetAdapter.ACCESS_PUBLIC);
			} else {
				adapter.setGetAccess(WidgetAdapter.ACCESS_DEFAULT);
			}
		} catch (Exception e) {
			ParserPlugin.getLogger().warning(e);
		}
		parseEventListener(cunit, adapter);
	}

	@SuppressWarnings("unchecked")
	private static String getBeanClassLnf(Class beanClass) {
		try {
			Field field = beanClass.getDeclaredField("PREFERRED_LOOK_AND_FEEL"); //$NON-NLS-1$
			if (field.getType() == String.class) {
				field.setAccessible(true);
				String lnf = (String) field.get(null);
				String className = UIManager
						.getCrossPlatformLookAndFeelClassName();
				if (lnf == null) {
					lnf = className;
				}
				return lnf;
			}
		} catch (Exception e) {
			ParserPlugin.getLogger().warning(e);			
		}
		return UIManager.getCrossPlatformLookAndFeelClassName();
	}
	private void parseEventListener(CompilationUnit cunit, WidgetAdapter adapter) {
		EventSetDescriptor[] esds = adapter.getBeanInfo()
				.getEventSetDescriptors();
		TypeDeclaration type = (TypeDeclaration) cunit.types().get(0);
		if (esds != null && esds.length > 0) {
			for (EventSetDescriptor esd : esds) {
				factory.parseEventListener(adapter, type, esd);
			}
		}
	}

	private String getNameFromFieldName(String fieldName) {
		return NamespaceUtil.getNameFromFieldName(fieldName);
	}

	private boolean isDesigningField(IType type, IField field) {
		try {
			String sig = field.getTypeSignature();
			if (isRegisteredWidget(sig)) {
				String fieldName = field.getElementName();
				String getMethodName = getGetMethodName(fieldName);
				IMethod method = type.getMethod(getMethodName, new String[0]);
				if (method != null && method.exists()) {
					return true;
				}
			}
			for (IFieldParser parser : fieldParsers) {
				if (parser.isDesigningField(type, field))
					return true;
			}
		} catch (JavaModelException e) {
			ParserPlugin.getLogger().error(e);
		}
		return false;
	}

	private boolean isRegisteredWidget(String sig) {
		if (sig.startsWith("L") || sig.startsWith("Q")) { //$NON-NLS-1$ //$NON-NLS-2$
			String className = sig.substring(1, sig.length() - 1);
			Map<String, IConfigurationElement> widgets = ExtensionRegistry
					.getRegisteredWidgets();
			int dot = className.lastIndexOf('.');
			if (dot != -1) {
				if (widgets.get(className) != null)
					return true;
			} else {
				String cName = "javax.swing." + className; //$NON-NLS-1$
				if (widgets.get(cName) != null)
					return true;
			}
		}
		return false;
	}
	private IType getUnitMainType(ICompilationUnit unit){
		String unit_name = unit.getElementName();
		int dot = unit_name.lastIndexOf('.');
		if (dot != -1)
			unit_name = unit_name.substring(0, dot);
		IType type = unit.getType(unit_name);
		return type;
	}
	public static ImportRewrite createImportRewrite(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(unit);
		parser.setResolveBindings(false);
		parser.setFocalPosition(0);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		return CodeStyleConfiguration.createImportRewrite(cu, true);
	}
	@Override
	public ICompilationUnit generate(WidgetAdapter root, IProgressMonitor monitor) {
		try {
			IParser parser = (IParser) root.getAdapter(IParser.class);
			if (parser == null)
				return null;
			ICompilationUnit unit = root.getCompilationUnit();
			ICompilationUnit copy = unit.getWorkingCopy(monitor);
			IType type = getUnitMainType(copy);
			if (type != null) {
				ImportRewrite imports = createImportRewrite(copy);
				ArrayList<String> fieldAdapters = listBeanName(root);
				boolean success = parser.generateCode(type, imports, monitor);
				if(!success)
					return null;
				IField[] fields = type.getFields();
				List<String> declared = new ArrayList<String>();
				if (fields != null && fields.length > 0) {
					for (IField field : fields) {
						if (isDesigningField(type, field)) {
							String fieldName = field.getElementName();
							declared.add(fieldName);
						}
					}
				}
				for (String iadapter : fieldAdapters) {
					declared.remove(iadapter);
				}
				for (String declare : declared) {
					removeField(type, declare, monitor);
				}
				IMethod[] methods = type.getMethods();
				for (IMethod method : methods) {
					if (isUnusedPrivateMethod(type, method)) {
						method.delete(false, monitor);
					}
				}
				IField lnfField = type.getField("PREFERRED_LOOK_AND_FEEL"); //$NON-NLS-1$
				if (lnfField.exists()) {
					lnfField.delete(false, monitor);
				}
				String className = (String) root
						.getProperty("preferred.lookandfeel"); //$NON-NLS-1$
				String newfield = "private static final " //$NON-NLS-1$
						+ imports.addImport("java.lang.String") //$NON-NLS-1$
						+ " PREFERRED_LOOK_AND_FEEL = " //$NON-NLS-1$
						+ (className == null ? "null" : "\"" + className + "\"") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						+ ";\n"; //$NON-NLS-1$
				type.createField(newfield, null, false, monitor);
				if (success) {
					TextEdit edit = imports.rewriteImports(monitor);
					JavaUtil.applyEdit(copy, edit, true, monitor);
				}
				if(copy.isWorkingCopy()){
					copy.commitWorkingCopy(true, monitor);
					copy.discardWorkingCopy();
				}				
				IWorkbenchPartSite site = getEditorSite();
				if (site != null) {
					OrganizeImportsAction action = new OrganizeImportsAction(site);
					action.run(unit);
				}
				type = getUnitMainType(unit);
				rename(type, root);
				if(unit.isWorkingCopy()){
					unit.commitWorkingCopy(true, monitor);
				}
				return unit;
			} else
				return null;
		} catch (Exception e) {
			ParserPlugin.getLogger().error(e);
			return null;
		}
	}
	private void rename(IType type, WidgetAdapter root) {
		if(root.getLastName()!=null&&root.getName()!=null&&!root.getLastName().equals(root.getName())){
			IParser parser = (IParser) root.getAdapter(IParser.class);
			if (parser != null) {
				parser.renameField(type, null);
			}
		}
		if(root.isRoot()){
			for(InvisibleAdapter invisible:root.getInvisibles()){
				renameInvisible(type, invisible);
			}
		}
		if(root instanceof CompositeAdapter){
			CompositeAdapter container = (CompositeAdapter) root;
			int count = container.getChildCount();
			for(int i=0;i<count;i++){
				Component child = container.getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
				rename(type, childAdapter);
			}
		}
	}

	private void renameInvisible(IType type, InvisibleAdapter root) {
		if(root.getLastName()!=null&&root.getName()!=null&&!root.getLastName().equals(root.getName())){
			IParser parser = (IParser) root.getAdapter(IParser.class);
			if (parser != null) {
				parser.renameField(type, null);
			}
		}
	}

	private IWorkbenchPartSite getEditorSite(){
		IWorkbench workbench = PlatformUI.getWorkbench();
		if(workbench!=null){
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			if(window!=null){
				IWorkbenchPage page = window.getActivePage();
				if(page!=null){
					IEditorPart editor = page.getActiveEditor();
					if(editor!=null)
						return editor.getSite();
				}
			}
		}
		return null;
	}


	private boolean isUnusedPrivateMethod(IType type, IMethod method) {
		try {
			int flags = method.getFlags();
			if (Flags.isPrivate(flags)) {
				String sig = method.getReturnType();
				if (isRegisteredWidget(sig)) {
					if (isGetMethod(method)) {
						String fieldName = getFieldNameFromGetMethod(method);
						IField field = type.getField(fieldName);
						if (!field.exists()) {
							return true;
						}
					}
				}
			}
		} catch (JavaModelException e) {
			ParserPlugin.getLogger().error(e);
		}
		return false;
	}

	private String getFieldNameFromGetMethod(IMethod method) {
		return NamespaceUtil.getFieldNameFromGetMethodName(
				method.getElementName());
	}

	private boolean isGetMethod(IMethod method) {
		return NamespaceUtil.isGetMethodName(
				method.getElementName());
	}

	private void removeField(IType type, String fieldName,
			IProgressMonitor monitor) {
		IField field = type.getField(getFieldName(fieldName));
		if (field != null && field.exists()) {
			try {
				field.delete(true, monitor);
			} catch (JavaModelException e) {
				ParserPlugin.getLogger().error(e);
				return;
			}
		}
		IMethod method = type.getMethod(getGetMethodName(fieldName),
				new String[0]);
		if (method != null && method.exists()) {
			try {
				method.delete(true, monitor);
				return;
			} catch (JavaModelException e) {
				ParserPlugin.getLogger().error(e);
			}
		} else {
			for (IFieldParser parser : fieldParsers) {
				if (parser.removeField(type, fieldName, monitor))
					return;
			}
		}
	}

	private String getFieldName(String fieldName) {
		return NamespaceUtil.getFieldName(fieldName);
	}

	private String getGetMethodName(String fieldName) {
		return NamespaceUtil.getGetMethodName(fieldName);
	}

	private ArrayList<String> listBeanName(WidgetAdapter root) {
		ArrayList<String> list = new ArrayList<String>();
		_listNames(root, list);
		return list;
	}

	private void _listNames(WidgetAdapter root, ArrayList<String> list) {
		if (!root.isRoot()) {
			list.add(root.getID());
		} else {
			for (InvisibleAdapter adapter : root.getInvisibles()) {
				list.add(adapter.getID());
			}
		}
		if (root instanceof CompositeAdapter) {
			CompositeAdapter containerAdapter = (CompositeAdapter) root;
			int count = containerAdapter.getChildCount();
			for (int i = 0; i < count; i++) {
				Component child = containerAdapter.getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter
						.getWidgetAdapter(child);
				_listNames(childAdapter, list);
			}
		}
	}
}
