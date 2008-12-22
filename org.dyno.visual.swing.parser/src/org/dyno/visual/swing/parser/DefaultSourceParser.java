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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.base.WidgetProperty;
import org.dyno.visual.swing.parser.spi.IFieldParser;
import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;
import org.dyno.visual.swing.plugin.spi.ISourceParser;
import org.dyno.visual.swing.plugin.spi.IValueParser;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * DefaultSourceParser
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
class DefaultSourceParser implements ISourceParser {
	private static final String FIELD_PARSER_EXTENSION_ID = "org.dyno.visual.swing.parser.fieldParser";
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
				if (name.equals("parser")) {
					try {
						fieldParsers.add((IFieldParser) configs[i]
								.createExecutableExtension("class"));
					} catch (CoreException e) {
						ParserPlugin.getLogger().error(e);
					}
				}
			}
		}
	}

	@Override
	public WidgetAdapter parse(ICompilationUnit unit) {
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
			ArrayList<URL> paths = new ArrayList<URL>();
			addClasspaths(java_project, paths);
			URL[] urls = paths.toArray(new URL[paths.size()]);
			Class<?> beanClass = new URLClassLoader(urls, getClass()
					.getClassLoader()).loadClass(className);
			if (Component.class.isAssignableFrom(beanClass)) {
				String lnf = getBeanClassLnf(beanClass);
				try {
					setUpLookAndFeel(lnf);
				} catch (Exception e) {
					Shell shell = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell();
					MessageDialog.openError(shell, "Error!", e.getMessage());
					return null;
				}
				try {
					Component bean = (Component) beanClass.newInstance();
					WidgetAdapter beanAdapter = ExtensionRegistry
							.createWidgetAdapter(bean);
					ASTParser parser = ASTParser.newParser(AST.JLS3);
					parser.setSource(unit);
					CompilationUnit cunit = (CompilationUnit) parser
							.createAST(null);
					parseEventListener(cunit, beanAdapter);
					initDesignedWidget(cunit, bean);
					parsePropertyValue(lnf, cunit, beanAdapter);
					return beanAdapter;
				} catch (Error re) {
					ParserPlugin.getLogger().error(re);
				}
			}
		} catch (Exception e) {
			ParserPlugin.getLogger().error(e);
		}
		return null;
	}

	private void addClasspaths(IJavaProject java_project, ArrayList<URL> paths)
			throws MalformedURLException, CoreException {
		java_project.getProject().build(
				IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
		IClasspathEntry[] classpaths = java_project.getResolvedClasspath(true);
		for (IClasspathEntry path : classpaths) {
			URL url = null;
			switch (path.getEntryKind()) {
			case IClasspathEntry.CPE_SOURCE:
				url = path.getPath().toFile().toURI().toURL();
				break;
			case IClasspathEntry.CPE_CONTAINER:
				url = path.getPath().toFile().toURI().toURL();
				break;
			case IClasspathEntry.CPE_LIBRARY:
				switch (path.getContentKind()) {
				case IPackageFragmentRoot.K_BINARY:
					IPath ip = ResourcesPlugin.getWorkspace().getRoot()
							.getLocation().append(path.getPath());
					url = ip.toFile().toURI().toURL();
					break;
				}
				break;
			case IClasspathEntry.CPE_PROJECT:
				IPath ip = path.getPath();
				IProject prj = ResourcesPlugin.getWorkspace().getRoot()
						.getProject(ip.segment(0));
				IJavaProject jp = JavaCore.create(prj);
				addClasspaths(jp, paths);
				break;
			case IClasspathEntry.CPE_VARIABLE:
				url = path.getPath().toFile().toURI().toURL();
				break;
			}
			if (url != null && !paths.contains(url)) {
				paths.add(url);
			}
		}
		IPath wsPath = java_project.getProject().getWorkspace().getRoot()
				.getRawLocation();
		paths.add(wsPath.append(java_project.getOutputLocation()).toFile()
				.toURI().toURL());
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
						Object oldValue = property.getRawValue(bean);
						Object newValue = vp.parseValue(oldValue, args);
						property.setRawValue(bean, newValue);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List getBeanPropertyInitStatements(WidgetAdapter adapter,
			TypeDeclaration type) {
		List statements;
		if (adapter.isRoot()) {
			MethodDeclaration initMethod = getMethodDeclaration(type,
					"initComponent");
			Block body = initMethod.getBody();
			statements = body.statements();
		} else {
			String getMethodName = NamespaceManager.getInstance()
					.getGetMethodName(adapter.getName());
			MethodDeclaration getMethod = getMethodDeclaration(type,
					getMethodName);
			Block body = getMethod.getBody();
			statements = body.statements();
			IfStatement ifs = (IfStatement) statements.get(0);
			Statement thenstmt = ifs.getThenStatement();
			if (thenstmt instanceof Block) {
				Block block = (Block) thenstmt;
				statements = block.statements();
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
		try {
			Class clazz = bean.getClass();
			field.setAccessible(true);
			Object fieldValue = field.get(bean);
			String fieldName = field.getName();
			JComponent fieldComponent = (JComponent) fieldValue;
			WidgetAdapter adapter = ExtensionRegistry
					.createWidgetAdapter(fieldComponent);
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
			String getName = getGetMethodName(fieldName);
			Method getMethod = clazz.getDeclaredMethod(getName);
			flags = getMethod.getModifiers();
			if (Modifier.isPrivate(flags)) {
				adapter.setGetAccess(WidgetAdapter.ACCESS_PRIVATE);
			} else if (Modifier.isProtected(flags)) {
				adapter.setGetAccess(WidgetAdapter.ACCESS_PROTECTED);
			} else if (Modifier.isPublic(flags)) {
				adapter.setGetAccess(WidgetAdapter.ACCESS_PUBLIC);
			} else {
				adapter.setGetAccess(WidgetAdapter.ACCESS_DEFAULT);
			}
			parseEventListener(cunit, adapter);
		} catch (Exception e) {
			ParserPlugin.getLogger().error(e);
		}
	}

	@SuppressWarnings("unchecked")
	private static String getBeanClassLnf(Class beanClass) {
		try {
			Field field = beanClass.getDeclaredField("PREFERRED_LOOK_AND_FEEL");
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
			ParserPlugin.getLogger().error(e);
		}
		return UIManager.getCrossPlatformLookAndFeelClassName();
	}

	private void setUpLookAndFeel(String lnf) throws Exception {
		ILookAndFeelAdapter adapter = ExtensionRegistry.getLnfAdapter(lnf);
		if (adapter != null) {
			LookAndFeel instance = adapter.getLookAndFeelInstance();
			if (instance != null) {
				try {
					UIManager.setLookAndFeel(instance);
				} catch (Exception e) {
					ParserPlugin.getLogger().error(e);
				}
			} else {
				throw new Exception(
						lnf
								+ " specified in this class is not a supported LAF on this java platform!");
			}
		}
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
		return NamespaceManager.getInstance().getNameFromFieldName(fieldName);
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
		if (sig.startsWith("L") || sig.startsWith("Q")) {
			String className = sig.substring(1, sig.length() - 1);
			Map<String, IConfigurationElement> widgets = ExtensionRegistry
					.getRegisteredWidgets();
			int dot = className.lastIndexOf('.');
			if (dot != -1) {
				if (widgets.get(className) != null)
					return true;
			} else {
				String cName = "javax.swing." + className;
				if (widgets.get(cName) != null)
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean generate(ICompilationUnit unit, WidgetAdapter root,
			IProgressMonitor monitor) {
		try {
			ImportRewrite imports = JavaUtil.createImportRewrite(unit);
			String unit_name = unit.getElementName();
			int dot = unit_name.lastIndexOf('.');
			if (dot != -1)
				unit_name = unit_name.substring(0, dot);
			IType type = unit.getType(unit_name);
			if (type != null) {
				ArrayList<String> fieldAdapters = listBeanName(root);
				IParser parser = (IParser) root.getAdapter(IParser.class);
				if (parser == null)
					return false;
				boolean success = parser.generateCode(type, imports, monitor);
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
				IField lnfField = type.getField("PREFERRED_LOOK_AND_FEEL");
				if (lnfField.exists()) {
					lnfField.delete(false, monitor);
				}
				String className = (String) root
						.getProperty("preferred.lookandfeel");
				String newfield = "private static final "
						+ imports.addImport("java.lang.String")
						+ " PREFERRED_LOOK_AND_FEEL = "
						+ (className == null ? "null" : "\"" + className + "\"")
						+ ";\n";
				type.createField(newfield, null, false, monitor);
				if (success) {
					TextEdit edit = imports.rewriteImports(monitor);
					JavaUtil.applyEdit(unit, edit, true, monitor);
				}
				return success;
			} else
				return false;
		} catch (Exception e) {
			ParserPlugin.getLogger().error(e);
			return false;
		}
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
		return NamespaceManager.getInstance().getFieldNameFromGetMethodName(
				method.getElementName());
	}

	private boolean isGetMethod(IMethod method) {
		return NamespaceManager.getInstance().isGetMethodName(
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
		return NamespaceManager.getInstance().getFieldName(fieldName);
	}

	private String getGetMethodName(String fieldName) {
		return NamespaceManager.getInstance().getGetMethodName(fieldName);
	}

	private ArrayList<String> listBeanName(WidgetAdapter root) {
		ArrayList<String> list = new ArrayList<String>();
		_listNames(root, list);
		return list;
	}

	private void _listNames(WidgetAdapter root, ArrayList<String> list) {
		if (!root.isRoot()) {
			list.add(root.getName());
		} else {
			for (InvisibleAdapter adapter : root.getInvisibles()) {
				list.add(adapter.getName());
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
