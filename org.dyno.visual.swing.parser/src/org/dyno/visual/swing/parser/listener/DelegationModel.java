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
import java.util.List;

import org.dyno.visual.swing.parser.NamespaceUtil;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;

@SuppressWarnings("unchecked")
public class DelegationModel extends AbstractInnerModel {
	
	public void addMethod(MethodDescriptor methodDesc) {
		String methodName;
		if (adapter.isRoot())
			methodName = eventSet.getName() + NamespaceUtil.getCapitalName(methodDesc.getName());
		else
			methodName = adapter.getID() + NamespaceUtil.getCapitalName(eventSet.getName()) + NamespaceUtil.getCapitalName(methodDesc.getName());
		IEventMethod content = new EventDelegation(methodDesc, methodName);
		methods.put(methodDesc, content);
	}

	
	protected boolean parse(WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener, ClassInstanceCreation instanceExpression) {
		AnonymousClassDeclaration acd = instanceExpression.getAnonymousClassDeclaration();
		List bodys = acd.bodyDeclarations();
		Method[] lMethods = esd.getListenerMethods();
		for (Object element : bodys) {
			if (!(element instanceof MethodDeclaration)) {
				return false;
			}
			MethodDeclaration method = (MethodDeclaration) element;
			if (method.isConstructor())
				return false;
			List modifiers = method.modifiers();
			boolean isPublic = false;
			for (Object mod : modifiers) {
				IExtendedModifier exm = (IExtendedModifier) mod;
				if (exm instanceof Modifier) {
					Modifier m = (Modifier) exm;
					if (m.getKeyword().equals(ModifierKeyword.PUBLIC_KEYWORD)) {
						isPublic = true;
					}
				}
			}
			if (!isPublic)
				return false;
			boolean isLM = false;
			List params = method.parameters();
			for (Method m : lMethods) {
				if (m.getName().equals(method.getName().getFullyQualifiedName())) {
					if (params != null && params.size() == 1) {
						SingleVariableDeclaration svd = (SingleVariableDeclaration) params.get(0);
						if (!svd.isVarargs()) {
							Type type = svd.getType();
							if (!type.isArrayType() && !type.isParameterizedType() && !type.isPrimitiveType() && !type.isWildcardType()) {
								Class<?>[] types = m.getParameterTypes();
								Class evtType = types[0];
								if (type.isQualifiedType()) {
									QualifiedType qt = (QualifiedType) type;
									if (qt.getName().getFullyQualifiedName().equals(evtType.getName())) {
										isLM = true;
									}
								} else if (type.isSimpleType()) {
									SimpleType st = (SimpleType) type;
									String name = st.getName().getFullyQualifiedName();
									if (evtType.getName().indexOf(name) != -1) {
										isLM = true;
									}
								}
							}
						}
					}
				}
			}
			if (!isLM)
				return false;
		}
		for (Object element : bodys) {
			if (element instanceof MethodDeclaration) {
				if (processListenerMethod(adapter, esd, mListener, (MethodDeclaration) element)) {
					return true;
				}
			}
		}
		return false;
	}

	
	protected IEventMethod getDelegatingContent(WidgetAdapter adapter, EventSetDescriptor eventSet, MethodDescriptor methodDesc, Block body, SingleVariableDeclaration var) {
		List statements = body.statements();
		if (statements.size() == 1) {
			Object stmt = statements.get(0);
			if (stmt instanceof ExpressionStatement) {
				ExpressionStatement es = (ExpressionStatement) stmt;
				Expression expression = es.getExpression();
				if (expression instanceof MethodInvocation) {
					MethodInvocation mi = (MethodInvocation) expression;
					Expression optional = mi.getExpression();
					if (optional == null||optional instanceof ThisExpression) {
						List list = mi.arguments();
						if (list.size() != 1) {
							return new CodeSnippet(adapter, eventSet, methodDesc, es.toString());
						}
						Expression exp = (Expression) list.get(0);
						if (exp instanceof SimpleName) {
							SimpleName sn = (SimpleName) exp;
							SimpleName varn = var.getName();
							if (sn.getFullyQualifiedName().equals(varn.getFullyQualifiedName()))
								return new EventDelegation(methodDesc, mi.getName().getFullyQualifiedName());
						}
						return new CodeSnippet(adapter, eventSet, methodDesc, es.toString());
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
}
