package org.dyno.visual.swing.parser.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.parser.ParserPlugin;
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
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.ui.IEditorPart;
import org.osgi.framework.Bundle;


@SuppressWarnings("unchecked")
abstract class AbstractInnerModel extends AbstractClassModel {
	protected static HashMap<Class, Class> listenerAdapters;
	public static final String LISTENER_ADAPTER_EXTENSION = "org.dyno.visual.swing.listenerAdapter";
	static {
		initListenerAdapters();
	}

	private static void initListenerAdapters() {
		listenerAdapters = new HashMap<Class, Class>();
		parseListenerAdapterExtensions();
		listenerAdapters.put(MouseListener.class, MouseAdapter.class);
	}

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
			ParserPlugin.getLogger().error(e);
		}
	}
	protected Map<MethodDescriptor, IEventMethod> methods;
	public AbstractInnerModel() {
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
						type.createMethod(JavaUtil.formatCode(code), null, false, monitor);
					} catch (JavaModelException e) {
						ParserPlugin.getLogger().error(e);
						return false;
					}
				}
			}
		}
		return true;
	}
	
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
		builder.append(cName + "()");
		createNewListenerContent(imports, builder, hasAdapter);
		return builder.toString();
	}
	
	protected void createNewListenerContent(ImportRewrite imports, StringBuilder builder, boolean hasAdapter){
		builder.append("{\n");
		createNewListener(imports, builder, hasAdapter);
	}
	
	protected void createNewListener(ImportRewrite imports, StringBuilder builder, boolean hasAdapter) {
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
	}	

	protected boolean processListenerMethod(WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener, MethodDeclaration methoddec) {
		if (methoddec.getName().getFullyQualifiedName().equals(mListener.getName())) {
			Block mbody = methoddec.getBody();
			SingleVariableDeclaration var = (SingleVariableDeclaration) methoddec.parameters().get(0);
			IEventMethod content = getDelegatingContent(adapter, esd, mListener, mbody, var);
			methods.put(mListener, content);
			return true;
		} else {
			return false;
		}
	}
	
	protected abstract IEventMethod getDelegatingContent(WidgetAdapter adapter, EventSetDescriptor eventSet, MethodDescriptor methodDesc, Block body, SingleVariableDeclaration var);

	@Override
	protected boolean processAddListenerStatement(TypeDeclaration type, WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener, MethodInvocation mi) {
		List arguments = mi.arguments();
		for (Object arg : arguments) {
			Expression argExpression = (Expression) arg;
			if (argExpression instanceof ClassInstanceCreation) {
				ClassInstanceCreation cic = (ClassInstanceCreation) argExpression;
				AnonymousClassDeclaration acd = cic.getAnonymousClassDeclaration();
				if (acd != null) {
					if (parse(adapter, esd, mListener, cic))
						return true;
				} else
					return false;
			} else
				return false;
		}
		return false;
	}

	protected abstract boolean parse(WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor listener, ClassInstanceCreation cic);
}
