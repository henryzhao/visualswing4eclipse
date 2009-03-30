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
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

@SuppressWarnings("unchecked")
abstract class BaseClassModel extends AbstractClassModel {
	protected Map<MethodDescriptor, MethodDescriptor> methods;
	protected String className;
	protected String parameters;	
	public BaseClassModel(){
		methods = new HashMap<MethodDescriptor, MethodDescriptor>();
	}

	@Override
	public void init(WidgetAdapter adapter, EventSetDescriptor eventSet) {
		super.init(adapter, eventSet);
		this.className = (adapter.isRoot() ? "This" : NamespaceUtil.getCapitalName(adapter.getID())) + NamespaceUtil.getCapitalName(eventSet.getName()) + "Listener";
	}

	@Override
	public void addMethod(MethodDescriptor methodDesc) {
		methods.put(methodDesc, methodDesc);
	}
	protected IType getMeType(IType type){
		return type.getCompilationUnit().getType(className);
	}
	@Override
	public boolean createEventMethod(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		IType meType = getMeType(type);
		Class listClass = eventSet.getListenerType();		
		Class adapterClass = DelegationModel.getListenerAdapter(listClass);
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

	private boolean createEventMethod(IType meType, String code, IProgressMonitor monitor) {
		try {
			meType.createMethod(code, null, false, monitor);
			return true;
		} catch (JavaModelException e) {
			ParserPlugin.getLogger().error(e);
			return false;
		}
	}
	@Override
	public void removeMethod(MethodDescriptor methodDesc) {
		methods.remove(methodDesc);
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
						return processAddMethod(type, mListener);
					} else
						return false;
				}
			} else
				return false;
		}
		return false;
	}

	protected boolean processAddMethod(TypeDeclaration type, MethodDescriptor mListener) {
		TypeDeclaration[] innerTypes = getInnerTypes(type);
		if (innerTypes != null) {
			for (TypeDeclaration innerDec : innerTypes) {
				if (innerDec.getName().getFullyQualifiedName().equals(className)) {
					String mListenerName = mListener.getName();
					MethodDeclaration[] mds = innerDec.getMethods();
					for (MethodDeclaration md : mds) {
						if (md.getName().getFullyQualifiedName().equals(mListenerName)) {
							addMethod(mListener);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	protected TypeDeclaration[] getInnerTypes(TypeDeclaration type){
		return type.getTypes();
	}
}
