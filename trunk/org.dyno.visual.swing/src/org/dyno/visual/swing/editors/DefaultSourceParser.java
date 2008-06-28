package org.dyno.visual.swing.editors;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.ISourceParser;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.resources.IncrementalProjectBuilder;
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
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

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
			if (JPanel.class.isAssignableFrom(beanClass)) {
				try {
					JComponent bean = (JComponent) beanClass.newInstance();
					WidgetAdapter beanAdapter = ExtensionRegistry
							.createWidgetAdapter(bean);
					genWidgetEvent(beanAdapter, beanClass, true);
					initDesignedWidget(bean);
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
	private void initDesignedWidget(JComponent bean) {
		Class clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Class type = field.getType();
			String fieldName = field.getName();
			if (JComponent.class.isAssignableFrom(type)
					&& isDesigningField(fieldName)) {
				field.setAccessible(true);
				try {
					JComponent fieldComponent = (JComponent) field.get(bean);
					if (fieldComponent != null) {
						WidgetAdapter adapter = ExtensionRegistry
								.createWidgetAdapter(fieldComponent);
						String widgetName = getNameFromFieldName(fieldName);
						adapter.setName(widgetName);
						adapter.setLastName(widgetName);
						genWidgetEvent(adapter, clazz, false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void genWidgetEvent(WidgetAdapter adapter, Class rootClass, boolean root) {
		EventSetDescriptor[] esds = adapter.getBeanInfo()
				.getEventSetDescriptors();
		if (esds != null && esds.length > 0) {
			for (EventSetDescriptor esd : esds) {
				MethodDescriptor[] mListeners = esd
						.getListenerMethodDescriptors();
				for (MethodDescriptor mListener : mListeners) {
					try {
						String mName = (root ? "" : adapter.getName() + "_")
								+ esd.getName() + "_" + mListener.getName();
						Class[] params = mListener.getMethod()
								.getParameterTypes();
						Method listMethod = rootClass.getDeclaredMethod(mName,
								params);
						if (listMethod != null) {
							Map<EventSetDescriptor, List<MethodDescriptor>> map = adapter
									.getEventDescriptor();
							List<MethodDescriptor> methods = map.get(esd);
							if (methods == null) {
								methods = new ArrayList<MethodDescriptor>();
								map.put(esd, methods);
							}
							methods.add(mListener);
						}
					} catch (NoSuchMethodException nsme) {
					}
				}
			}
		}
	}

	private String getNameFromFieldName(String fieldName) {
		return NamespaceManager.getInstance().getNameFromFieldName(fieldName);
	}

	private boolean isDesigningField(String name) {
		return NamespaceManager.getInstance().isDesigningField(name);
	}

	@Override
	public void setSource(ICompilationUnit source) {
		this.unit = source;
	}

	private WidgetAdapter result;

	@Override
	public boolean genCode(IProgressMonitor monitor) {
		try {
			IType[] types = unit.getAllTypes();
			if (types != null && types.length > 0) {
				IType type = types[0];
				ArrayList<String> fieldNames = listBeanName(root);
				boolean success = root.genCode(type, imports, monitor);
				IField[] fields = type.getFields();
				ArrayList<String> declared = new ArrayList<String>();
				for (IField field : fields) {
					String fieldName = field.getElementName();
					if (isDesigningField(fieldName)) {
						declared.add(getNameFromFieldName(fieldName));
					}
				}
				for (String fieldName : fieldNames) {
					declared.remove(fieldName);
				}
				for (String declare : declared) {
					removeComponent(type, declare, monitor);
				}
				return success;
			} else
				return false;
		} catch (JavaModelException e) {
			e.printStackTrace();
			return false;
		}
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
}
