/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.editors;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.plugin.spi.CodeSnippet;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.EventDelegation;
import org.dyno.visual.swing.plugin.spi.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.IEventMethod;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;
import org.dyno.visual.swing.plugin.spi.ISourceParser;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * DefaultSourceParser
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
class DefaultSourceParser implements ISourceParser {
	private ICompilationUnit unit;
	private ImportRewrite imports;

	@Override
	public WidgetAdapter getResult() {
		return result;
	}

	@Override
	public boolean parse() {
		try {
			IType[] types = unit.getPrimary().getAllTypes();
			for (IType type : types) {
				if (type.isClass() && Flags.isPublic(type.getFlags())) {
					if (processType(unit.getPrimary(), type))
						return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean processType(ICompilationUnit unit, IType type)
			throws JavaModelException {
		try {
			unit.getJavaProject().getProject().build(
					IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
			IJavaProject java_project = type.getJavaProject();
			String className = type.getFullyQualifiedName();
			IClasspathEntry[] classpaths = java_project
					.getResolvedClasspath(true);
			ArrayList<URL> paths = new ArrayList<URL>();
			for (IClasspathEntry path : classpaths) {
				paths.add(path.getPath().toFile().toURI().toURL());
			}
			IPath wsPath = java_project.getProject().getWorkspace().getRoot()
					.getRawLocation();
			paths.add(wsPath.append(java_project.getOutputLocation()).toFile()
					.toURI().toURL());
			URL[] urls = paths.toArray(new URL[paths.size()]);
			Class<?> beanClass = new URLClassLoader(urls, getClass()
					.getClassLoader()).loadClass(className);
			if (JComponent.class.isAssignableFrom(beanClass)) {
				try {
					setUpLookAndFeel(beanClass);
				} catch (Exception e) {
					Shell parent = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell();
					MessageDialog.openError(parent, "Error!", e.getMessage());
					return false;
				}
				try {
					JComponent bean = (JComponent) beanClass.newInstance();
					WidgetAdapter beanAdapter = ExtensionRegistry
							.createWidgetAdapter(bean);
					ASTParser parser = ASTParser.newParser(AST.JLS3);
					parser.setSource(this.unit);
					CompilationUnit cunit = (CompilationUnit) parser
							.createAST(null);
					createWidgetEvent(type, cunit, beanAdapter, beanClass, true);
					initDesignedWidget(cunit, bean, type);
					result = beanAdapter;
				} catch (Error re) {
					return false;
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void setUpLookAndFeel(Class beanClass) throws Exception {
		String lnf = getBeanClassLnf(beanClass);
		ILookAndFeelAdapter adapter = ExtensionRegistry.getLnfAdapter(lnf);
		if (adapter != null) {
			LookAndFeel instance = adapter.getLookAndFeelInstance();
			if (instance != null) {
				try {
					UIManager.setLookAndFeel(instance);
				} catch (Exception e) {
				}
			} else {
				throw new Exception(
						lnf
								+ " specified in this class is not a supported LAF on this java platform!");
			}
		} else
			throw new Exception(
					lnf
							+ " specified in this class is not a supported LAF on this java platform!");
	}

	@SuppressWarnings("unchecked")
	private void initDesignedWidget(CompilationUnit cunit, JComponent bean,
			IType itype) {
		Class clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Class type = field.getType();
			String fieldName = field.getName();
			if (JComponent.class.isAssignableFrom(type)) {
				field.setAccessible(true);
				try {
					JComponent fieldComponent = (JComponent) field.get(bean);
					if (fieldComponent != null) {
						WidgetAdapter adapter = ExtensionRegistry
								.createWidgetAdapter(fieldComponent);
						String widgetName = getNameFromFieldName(fieldName);
						adapter.setName(widgetName);
						adapter.setLastName(widgetName);
						createWidgetEvent(itype, cunit, adapter, clazz, false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createWidgetEvent(IType itype, CompilationUnit cunit,
			WidgetAdapter adapter, Class rootClass, boolean root) {
		EventSetDescriptor[] esds = adapter.getBeanInfo()
				.getEventSetDescriptors();
		if (esds != null && esds.length > 0) {
			for (EventSetDescriptor esd : esds) {
				MethodDescriptor[] mListeners = esd
						.getListenerMethodDescriptors();
				for (MethodDescriptor mListener : mListeners) {
					TypeDeclaration type = (TypeDeclaration) cunit.types().get(
							0);
					createEventMethod(adapter, esd, mListener, type, root);
				}
			}
		}
	}

	private void createEventMethod(WidgetAdapter adapter,
			EventSetDescriptor esd, MethodDescriptor mListener,
			TypeDeclaration type, boolean root) {
		MethodDeclaration[] mds = type.getMethods();
		for (MethodDeclaration md : mds) {
			String mdName = md.getName().getFullyQualifiedName();
			if (root) {
				if (mdName.equals("initComponent")) {
					createEventMethodForWidget(adapter, esd, mListener, md,
							root);
					break;
				}
			} else {
				String getName = getGetMethodName(adapter.getName());
				if (mdName.equals(getName)) {
					createEventMethodForWidget(adapter, esd, mListener, md,
							root);
					break;
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createEventMethodForWidget(WidgetAdapter adapter,
			EventSetDescriptor esd, MethodDescriptor mListener,
			MethodDeclaration md, boolean root) {
		Block body = md.getBody();
		List statements = body.statements();
		if (!root) {
			IfStatement ifstatement = (IfStatement) statements.get(0);
			Statement thenstatement = ifstatement.getThenStatement();
			if (thenstatement instanceof Block) {
				statements = ((Block) thenstatement).statements();
			}
		}
		for (Object stmt : statements) {
			Statement statement = (Statement) stmt;
			if (statement instanceof ExpressionStatement) {
				processWidgetCreationStatement(adapter, esd, mListener,
						statement, root);
			}
		}
	}

	private void processWidgetCreationStatement(WidgetAdapter adapter,
			EventSetDescriptor esd, MethodDescriptor mListener,
			Statement statement, boolean root) {
		ExpressionStatement expressionStatement = (ExpressionStatement) statement;
		Expression expression = expressionStatement.getExpression();
		if (expression instanceof MethodInvocation) {
			MethodInvocation mi = (MethodInvocation) expression;
			if (root) {
				createAddMethod(adapter, esd, mListener, mi);
			} else {
				Expression optionalExpression = mi.getExpression();
				if (optionalExpression instanceof SimpleName) {
					SimpleName simplename = (SimpleName) optionalExpression;
					String idName = simplename.getFullyQualifiedName();
					if (idName.equals(adapter.getName()))
						createAddMethod(adapter, esd, mListener, mi);
				}
			}
		}
	}

	private void createAddMethod(WidgetAdapter adapter, EventSetDescriptor esd,
			MethodDescriptor mListener, MethodInvocation mi) {
		Method addm = esd.getAddListenerMethod();
		String addmName = addm.getName();
		String mName = mi.getName().getFullyQualifiedName();
		if (mName.equals(addmName)) {
			processAddListenerStatement(adapter, esd, mListener, mi);
		}
	}

	@SuppressWarnings("unchecked")
	private void processAddListenerStatement(WidgetAdapter adapter,
			EventSetDescriptor esd, MethodDescriptor mListener,
			MethodInvocation mi) {
		List arguments = mi.arguments();
		for (Object arg : arguments) {
			Expression argExpression = (Expression) arg;
			if (argExpression instanceof ClassInstanceCreation) {
				ClassInstanceCreation cic = (ClassInstanceCreation) argExpression;
				AnonymousClassDeclaration acd = cic
						.getAnonymousClassDeclaration();
				if (acd != null) {
					List bodys = acd.bodyDeclarations();
					for (Object element : bodys) {
						if (element instanceof MethodDeclaration) {
							processListenerMethod(adapter, esd, mListener,
									element);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private IEventMethod getDelegatingContent(WidgetAdapter adapter,
			EventSetDescriptor eventSet, MethodDescriptor methodDesc, Block body) {
		List statements = body.statements();
		if (statements.size() == 1) {
			Object stmt = statements.get(0);
			if (stmt instanceof ExpressionStatement) {
				ExpressionStatement es = (ExpressionStatement) stmt;
				Expression expression = es.getExpression();
				if (expression instanceof MethodInvocation) {
					MethodInvocation mi = (MethodInvocation) expression;
					Expression optional = mi.getExpression();
					if (optional == null) {
						return new EventDelegation(adapter, methodDesc, mi
								.getName().getFullyQualifiedName());
					} else if (optional instanceof ThisExpression) {
						ThisExpression thisExpression = (ThisExpression) optional;
						Name qName = thisExpression.getQualifier();
						if (qName != null) {
							return new EventDelegation(adapter, methodDesc, mi
									.getName().getFullyQualifiedName());
						} else {
							return new CodeSnippet(adapter, eventSet,
									methodDesc, es.toString());
						}
					} else {
						return new CodeSnippet(adapter, eventSet, methodDesc,
								es.toString());
					}
				} else
					return new CodeSnippet(adapter, eventSet, methodDesc, es
							.toString());
			} else
				return new CodeSnippet(adapter, eventSet, methodDesc, stmt
						.toString());
		} else {
			StringBuilder builder = new StringBuilder();
			for (Object stmt : statements) {
				builder.append(stmt.toString());
			}
			return new CodeSnippet(adapter, eventSet, methodDesc, builder
					.toString());
		}
	}

	private void processListenerMethod(WidgetAdapter adapter,
			EventSetDescriptor esd, MethodDescriptor mListener, Object element) {
		MethodDeclaration methoddec = (MethodDeclaration) element;
		if (methoddec.getName().getFullyQualifiedName().equals(
				mListener.getName())) {
			Map<EventSetDescriptor, Map<MethodDescriptor, IEventMethod>> map = adapter
					.getEventDescriptor();
			Map<MethodDescriptor, IEventMethod> methods = map.get(esd);
			if (methods == null) {
				methods = new HashMap<MethodDescriptor, IEventMethod>();
				map.put(esd, methods);
			}
			Block mbody = methoddec.getBody();
			IEventMethod content = getDelegatingContent(adapter, esd,
					mListener, mbody);
			methods.put(mListener, content);
		}
	}

	private String getNameFromFieldName(String fieldName) {
		return NamespaceManager.getInstance().getNameFromFieldName(fieldName);
	}

	@Override
	public void setSource(ICompilationUnit source) {
		this.unit = source;
	}

	private WidgetAdapter result;

	private boolean isDesigningField(IType type, IField field) {
		try {
			String sig = field.getTypeSignature();
			if (isRegisteredWidget(sig)) {
				String fieldName = field.getElementName();
				String get = getGetMethodName(fieldName);
				IMethod method = type.getMethod(get, new String[0]);
				if (method.exists()) {
					return true;
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean isRegisteredWidget(String sig) {
		if (sig.startsWith("L") || sig.startsWith("Q")) {
			String className = sig.substring(1, sig.length() - 1);
			HashMap<String, IConfigurationElement> widgets = ExtensionRegistry
					.getRegisteredWidgets();
			int dot = className.lastIndexOf('.');
			if (dot != -1) {
				return widgets.get(className) != null;
			} else {
				className = "javax.swing." + className;
				return widgets.get(className) != null;
			}
		} else
			return false;
	}

	@Override
	public boolean genCode(IProgressMonitor monitor) {
		try {
			String unit_name = unit.getElementName();
			int dot = unit_name.lastIndexOf('.');
			if (dot != -1)
				unit_name = unit_name.substring(0, dot);
			IType type = unit.getType(unit_name);
			if (type != null) {
				ArrayList<String> fieldNames = listBeanName(root);
				boolean success = root.genCode(type, imports, monitor);
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
				for (String fieldName : fieldNames) {
					declared.remove(fieldName);
				}
				for (String declare : declared) {
					removeComponent(type, declare, monitor);
				}
				// Remove unused private method
				IMethod[] methods = type.getMethods();
				for (IMethod method : methods) {
					if (isUnusedPrivateMethod(type, method)) {
						method.delete(false, monitor);
					}
				}
				if (lnfChanged) {
					IField lnfField = type.getField("PREFERRED_LOOK_AND_FEEL");
					if (lnfField.exists()) {
						lnfField.delete(false, monitor);
					}
					LookAndFeel lnf = UIManager.getLookAndFeel();
					String className = null;
					if (lnf != null) {
						className = lnf.getClass().getName();
					}
					String newfield = "private static final "
							+ imports.addImport("java.lang.String")
							+ " PREFERRED_LOOK_AND_FEEL = "
							+ (className == null ? "null" : "\"" + className
									+ "\"") + ";\n";
					type.createField(newfield, null, false, monitor);
				}
				return success;
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
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

	private void removeComponent(IType type, String fieldName,
			IProgressMonitor monitor) {
		IField field = type.getField(getFieldName(fieldName));
		IMethod method = type.getMethod(getGetMethodName(fieldName),
				new String[0]);
		if (field != null && field.exists()) {
			try {
				field.delete(true, monitor);
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		if (method != null && method.exists()) {
			try {
				method.delete(true, monitor);
			} catch (JavaModelException e) {
				e.printStackTrace();
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
		}
		if (root instanceof CompositeAdapter) {
			CompositeAdapter containerAdapter = (CompositeAdapter) root;
			int count = containerAdapter.getChildCount();
			for (int i = 0; i < count; i++) {
				JComponent child = containerAdapter.getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter
						.getWidgetAdapter(child);
				_listNames(childAdapter, list);
			}
		}
	}

	@Override
	public void setRootAdapter(WidgetAdapter root) {
		this.root = root;
	}

	private WidgetAdapter root;

	@Override
	public void setImportWrite(ImportRewrite imports) {
		this.imports = imports;
	}

	@Override
	public void setLnfChanged(boolean b) {
		lnfChanged = b;
	}

	private boolean lnfChanged;
}
