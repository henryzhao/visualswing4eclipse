package org.dyno.visual.swing.parser.adapters;

import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.base.WidgetProperty;
import org.dyno.visual.swing.parser.NamespaceUtil;
import org.dyno.visual.swing.parser.spi.IWidgetASTParser;
import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.IConstants;
import org.dyno.visual.swing.plugin.spi.IValueParser;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

@SuppressWarnings("unchecked")
public class WidgetASTParser implements IWidgetASTParser, IConstants, IAdaptableContext {
	protected WidgetAdapter adaptable;

	private MethodDeclaration getMethodDeclaration(TypeDeclaration type, String name) {
		MethodDeclaration[] methods = type.getMethods();
		for (MethodDeclaration method : methods) {
			if (method.getName().getFullyQualifiedName().equals(name))
				return method;
		}
		return null;
	}

	@Override
	public void parse(String lnfClassname, TypeDeclaration type) {
		List statements = getBeanPropertyInitStatements(adaptable, type);
		for (Object stmt : statements) {
			Statement statement = (Statement) stmt;
			if (statement instanceof ExpressionStatement) {
				ExpressionStatement es = (ExpressionStatement) statement;
				Expression expression = es.getExpression();
				if (expression instanceof MethodInvocation) {
					MethodInvocation mi = (MethodInvocation) expression;
					Expression optional = mi.getExpression();
					if (isShouldCheck(optional)) {
						String mName = mi.getName().getFullyQualifiedName(); 
						if (mName.startsWith("set")) {
							parseSetStatement(lnfClassname, mi);
						}else if(mName.startsWith("add")){
							parseAddStatement(lnfClassname, mi);
						}
					}
				}
			}
		}
	}
	protected void parseAddStatement(String lnfClassname,  MethodInvocation mi){
		
	}
	protected void parseSetStatement(String lnfClassname,  MethodInvocation mi) {
		Object bean = adaptable.getWidget();
		IStructuredSelection sel = new StructuredSelection(bean);
		ArrayList<IWidgetPropertyDescriptor> properties = adaptable.getPropertyDescriptors();
		for (IWidgetPropertyDescriptor property : properties) {
			if (property.isPropertySet(lnfClassname, sel) && property instanceof WidgetProperty) {
				String setMethodName = mi.getName().getFullyQualifiedName();
				WidgetProperty wp = (WidgetProperty) property;
				String writeMethodName = wp.getPropertyDescriptor().getWriteMethod().getName();
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

	private boolean isShouldCheck(Expression optional) {
		if (optional == null) {
			return adaptable.isRoot();
		} else if (optional instanceof ThisExpression) {
			return adaptable.isRoot();
		} else if (optional instanceof FieldAccess) {
			FieldAccess fieldAccess = (FieldAccess) optional;
			Expression accessExpression = fieldAccess.getExpression();
			if (accessExpression instanceof SimpleName) {
				String fieldName = ((SimpleName) accessExpression).getFullyQualifiedName();
				if (fieldName.equals(adaptable.getName()))
					return true;
			}
		} else if (optional instanceof SimpleName) {
			String fieldName = ((SimpleName) optional).getFullyQualifiedName();
			if (fieldName.equals(adaptable.getName()))
				return true;
		}
		return false;
	}

	private List getBeanPropertyInitStatements(WidgetAdapter adapter, TypeDeclaration type) {
		List statements;
		if (adapter.isRoot()) {
			MethodDeclaration initMethod = getMethodDeclaration(type, INIT_METHOD_NAME);
			if (initMethod != null) {
				Block body = initMethod.getBody();
				statements = body.statements();
			}else{
				initMethod = getMethodDeclaration(type, type.getName().getFullyQualifiedName());
				if(initMethod!=null){
					Block body = initMethod.getBody();
					statements = body.statements();
				}else{
					statements = new ArrayList();
				}
			}
		} else {
			String getMethodName = NamespaceUtil.getGetMethodName(adapter, adapter.getID());
			MethodDeclaration getMethod = getMethodDeclaration(type, getMethodName);
			if (getMethod != null) {
				Block body = getMethod.getBody();
				statements = body.statements();
				if(!statements.isEmpty()){
					Object first=statements.get(0);
					if (first instanceof IfStatement) {
						IfStatement ifs = (IfStatement) statements.get(0);
						Statement thenstmt = ifs.getThenStatement();
						if (thenstmt instanceof Block) {
							Block block = (Block) thenstmt;
							statements = block.statements();
						}
					}
				}
			} else {
				MethodDeclaration initMethod = getMethodDeclaration(type, INIT_METHOD_NAME);
				Block body = initMethod.getBody();
				statements = body.statements();
			}
		}
		return statements;
	}

	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adaptable = (WidgetAdapter) adaptable;
	}
}
