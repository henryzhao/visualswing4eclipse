package org.dyno.visual.swing.parser.listener;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.plugin.spi.IEventListenerModel;
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
import org.eclipse.jdt.core.dom.Block;
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
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

public class ThisClassModel implements IEventListenerModel {
	private Map<MethodDescriptor, MethodDescriptor> methods;
	private EventSetDescriptor eventSet;
	private WidgetAdapter adapter;

	public ThisClassModel() {
		methods = new HashMap<MethodDescriptor, MethodDescriptor>();
	}
	@Override
	public void init(WidgetAdapter adapter, EventSetDescriptor eventSet) {
		this.adapter = adapter;
		this.eventSet = eventSet;
	}

	@Override
	public void addMethod(MethodDescriptor methodDesc) {
		methods.put(methodDesc, methodDesc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean createEventMethod(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		String[] inters;
		try {
			inters = type.getSuperInterfaceNames();
		} catch (JavaModelException e) {
			e.printStackTrace();
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
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
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
			e.printStackTrace();
		}
	}

	@Override
	public String createListenerInstance(ImportRewrite imports) {
		return "this";
	}

	@Override
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

	@Override
	public String getDisplayName(MethodDescriptor methodDesc) {
		return methodDesc.getDisplayName();
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
	public boolean parse(TypeDeclaration type) {
		MethodDescriptor[] mListeners = eventSet.getListenerMethodDescriptors();
		boolean success = false;
		for (MethodDescriptor mListener : mListeners) {
			if (createEventMethod(adapter, eventSet, mListener, type))
				success = true;
		}
		return success;
	}
	private boolean createEventMethod(WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener, TypeDeclaration type) {
		MethodDeclaration[] mds = type.getMethods();
		boolean success = false;
		for (MethodDeclaration md : mds) {
			String mdName = md.getName().getFullyQualifiedName();
			if (adapter.isRoot()) {
				if (mdName.equals("initComponent")) {
					if (createEventMethodForWidget(type, adapter, esd, mListener, md))
						success = true;
					break;
				}
			} else {
				String getName = getGetMethodName(adapter.getName());
				if (mdName.equals(getName)) {
					if (createEventMethodForWidget(type, adapter, esd, mListener, md))
						success = true;
					break;
				}
			}
		}
		return success;
	}
	private String getGetMethodName(String fieldName) {
		return NamespaceManager.getInstance().getGetMethodName(fieldName);
	}

	@SuppressWarnings("unchecked")
	private boolean createEventMethodForWidget(TypeDeclaration type, WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener,
			MethodDeclaration md) {
		Block body = md.getBody();
		List statements = body.statements();
		if (!adapter.isRoot()) {
			IfStatement ifstatement = (IfStatement) statements.get(0);
			Statement thenstatement = ifstatement.getThenStatement();
			if (thenstatement instanceof Block) {
				statements = ((Block) thenstatement).statements();
			}
		}
		boolean success = false;
		for (Object stmt : statements) {
			Statement statement = (Statement) stmt;
			if (statement instanceof ExpressionStatement) {
				if (processWidgetCreationStatement(type, adapter, esd, mListener, statement))
					success = true;
			}
		}
		return success;
	}
	private boolean processWidgetCreationStatement(TypeDeclaration type, WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener,
			Statement statement) {
		ExpressionStatement expressionStatement = (ExpressionStatement) statement;
		Expression expression = expressionStatement.getExpression();
		if (expression instanceof MethodInvocation) {
			MethodInvocation mi = (MethodInvocation) expression;
			if (adapter.isRoot()) {
				return createAddMethod(type, adapter, esd, mListener, mi);
			} else {
				Expression optionalExpression = mi.getExpression();
				if (optionalExpression instanceof SimpleName) {
					SimpleName simplename = (SimpleName) optionalExpression;
					String idName = simplename.getFullyQualifiedName();
					if (idName.equals(adapter.getName()))
						return createAddMethod(type, adapter, esd, mListener, mi);
					else
						return false;
				} else
					return false;
			}
		} else
			return false;
	}
	private boolean createAddMethod(TypeDeclaration type, WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener, MethodInvocation mi) {
		Method addm = esd.getAddListenerMethod();
		String addmName = addm.getName();
		String mName = mi.getName().getFullyQualifiedName();
		if (mName.equals(addmName)) {
			return processAddListenerStatement(type, adapter, esd, mListener, mi);
		} else
			return false;
	}
	@SuppressWarnings("unchecked")
	private boolean processAddListenerStatement(TypeDeclaration type, WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener,
			MethodInvocation mi) {
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
	@Override
	public void removeMethod(MethodDescriptor methodDesc) {
		methods.remove(methodDesc);
	}

}
