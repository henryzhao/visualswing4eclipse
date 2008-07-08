package org.dyno.visual.swing.parser.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.ui.IEditorPart;
import org.osgi.framework.Bundle;

public class AnonymousInnerClassModel extends AbstractClassModel {
	@SuppressWarnings("unchecked")
	private static HashMap<Class, Class> listenerAdapters;
	public static final String LISTENER_ADAPTER_EXTENSION = "org.dyno.visual.swing.listenerAdapter";
	static {
		initListenerAdapters();
	}

	@SuppressWarnings("unchecked")
	private static void initListenerAdapters() {
		listenerAdapters = new HashMap<Class, Class>();
		parseListenerAdapterExtensions();
		listenerAdapters.put(MouseListener.class, MouseAdapter.class);
	}

	@SuppressWarnings("unchecked")
	public static Class getListenerAdapter(Class list) {
		return listenerAdapters.get(list);
	}

	private static void parseListenerAdapterExtensions() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(LISTENER_ADAPTER_EXTENSION);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseListenerAdapterExtension(extensions[i]);
				}
			}
		}
	}

	private static void parseListenerAdapterExtension(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("listener")) {
					addListenerAdapter(configs[i]);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void addListenerAdapter(IConfigurationElement config) {
		String interf = config.getAttribute("interface");
		String adapter = config.getAttribute("adapter");
		IContributor contrib = config.getContributor();
		String pluginID = contrib.getName();
		Bundle bundle = Platform.getBundle(pluginID);
		try {
			Class interClass = bundle.loadClass(interf);
			Class adapterClass = bundle.loadClass(adapter);
			listenerAdapters.put(interClass, adapterClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map<MethodDescriptor, IEventMethod> methods;

	public AnonymousInnerClassModel() {
		methods = new HashMap<MethodDescriptor, IEventMethod>();
	}

	@Override
	public String getDisplayName(MethodDescriptor method) {
		IEventMethod content = methods.get(method);
		return content.getDisplayName();
	}

	@Override
	public Iterable<MethodDescriptor> methods() {
		return methods.keySet();
	}

	@Override
	public boolean hasMethod(MethodDescriptor methodDesc) {
		return methods.containsKey(methodDesc);
	}

	@Override
	public void removeMethod(MethodDescriptor methodDesc) {
		methods.remove(methodDesc);
	}

	@Override
	public boolean isEmpty() {
		return methods.isEmpty();
	}

	@Override
	public void addMethod(MethodDescriptor methodDesc) {
		String methodName;
		if (adapter.isRoot())
			methodName = eventSet.getName() + getCapitalName(methodDesc.getName());
		else
			methodName = adapter.getName() + getCapitalName(eventSet.getName()) + getCapitalName(methodDesc.getName());
		IEventMethod content = new EventDelegation(methodDesc, methodName);
		methods.put(methodDesc, content);
	}

	private String getCapitalName(String name) {
		return NamespaceManager.getInstance().getCapitalName(name);
	}

	@Override
	public void editMethod(IEditorPart editor, MethodDescriptor methodDesc) {
		IEventMethod content = methods.get(methodDesc);
		content.editCode(editor);
	}

	@Override
	public boolean createEventMethod(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		for (MethodDescriptor mdesc : methods()) {
			if (adapter.getLastName() == null || adapter.isRoot() || adapter.getName().equals(adapter.getLastName())) {
				IEventMethod content = methods.get(mdesc);
				String code = content.createEventMethod(type, imports);
				if (code != null) {
					try {
						type.createMethod(WidgetAdapter.formatCode(code), null, false, monitor);
					} catch (JavaModelException e) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String createListenerInstance(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append("new ");
		Class lClass = eventSet.getListenerType();
		boolean hasAdapter = listenerAdapters.get(lClass) != null;
		if (hasAdapter)
			lClass = listenerAdapters.get(lClass);
		String lName = lClass.getName();
		String cName = imports.addImport(lName);
		builder.append(cName + "(){\n");
		for (MethodDescriptor mdesc : methods()) {
			Method mEvent = mdesc.getMethod();
			boolean genOverride = !hasAdapter || hasMethod(mdesc);
			if (genOverride) {
				builder.append("@Override\n");
				builder.append("public void " + mEvent.getName() + "(");
				Class[] pTypes = mEvent.getParameterTypes();
				if (pTypes != null && pTypes.length > 0) {
					String pcName = pTypes[0].getName();
					pcName = imports.addImport(pcName);
					builder.append(pcName);
					builder.append(" event");
				}
				builder.append("){\n");
			}
			if (hasMethod(mdesc)) {
				IEventMethod content = methods.get(mdesc);
				String code = content.createAddListenerCode();
				builder.append(code);
			}
			if (genOverride)
				builder.append("}\n");
		}
		builder.append("}\n");
		return builder.toString();
	}

	@SuppressWarnings("unchecked")
	private void parse(WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener, ClassInstanceCreation instanceExpression) {
		AnonymousClassDeclaration acd = instanceExpression.getAnonymousClassDeclaration();
		List bodys = acd.bodyDeclarations();
		for (Object element : bodys) {
			if (element instanceof MethodDeclaration) {
				processListenerMethod(adapter, esd, mListener, (MethodDeclaration) element);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private IEventMethod getDelegatingContent(WidgetAdapter adapter, EventSetDescriptor eventSet, MethodDescriptor methodDesc, Block body) {
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
						return new EventDelegation(methodDesc, mi.getName().getFullyQualifiedName());
					} else if (optional instanceof ThisExpression) {
						ThisExpression thisExpression = (ThisExpression) optional;
						Name qName = thisExpression.getQualifier();
						if (qName != null) {
							return new EventDelegation(methodDesc, mi.getName().getFullyQualifiedName());
						} else {
							return new CodeSnippet(adapter, eventSet, methodDesc, es.toString());
						}
					} else {
						return new CodeSnippet(adapter, eventSet, methodDesc, es.toString());
					}
				} else
					return new CodeSnippet(adapter, eventSet, methodDesc, es.toString());
			} else
				return new CodeSnippet(adapter, eventSet, methodDesc, stmt.toString());
		} else {
			StringBuilder builder = new StringBuilder();
			for (Object stmt : statements) {
				builder.append(stmt.toString());
			}
			return new CodeSnippet(adapter, eventSet, methodDesc, builder.toString());
		}
	}

	private void processListenerMethod(WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener, MethodDeclaration methoddec) {
		if (methoddec.getName().getFullyQualifiedName().equals(mListener.getName())) {
			Block mbody = methoddec.getBody();
			IEventMethod content = getDelegatingContent(adapter, esd, mListener, mbody);
			methods.put(mListener, content);
		}
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
					parse(adapter, esd, mListener, cic);
					return true;
				} else
					return false;
			} else
				return false;
		}
		return false;
	}
}
