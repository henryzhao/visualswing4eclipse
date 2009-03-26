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
import java.util.List;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

@SuppressWarnings("unchecked")
public class AnonymousInnerModel extends AbstractInnerModel {
	private AnonymousClassDeclaration anonymousClass;
	@Override
	public void addMethod(MethodDescriptor methodDesc) {
		CodeSnippet content = new CodeSnippet(adapter, eventSet, methodDesc, "");
		methods.put(methodDesc, content);
	}

	@Override
	protected void createNewListenerContent(ImportRewrite imports, StringBuilder builder, boolean hasAdapter){
		if (anonymousClass != null) {
			builder.append(anonymousClass.toString());
		} else {
			createNewListener(imports, builder, hasAdapter);
		}
	}

	@Override
	protected boolean parse(WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener, ClassInstanceCreation instanceExpression) {
		AnonymousClassDeclaration acd = instanceExpression.getAnonymousClassDeclaration();
		anonymousClass = acd;
		List bodys = acd.bodyDeclarations();
		for (Object element : bodys) {
			if (element instanceof MethodDeclaration) {
				if (processListenerMethod(adapter, esd, mListener, (MethodDeclaration) element)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected IEventMethod getDelegatingContent(WidgetAdapter adapter, EventSetDescriptor eventSet, MethodDescriptor methodDesc, Block body, SingleVariableDeclaration var) {
		List statements = body.statements();
		StringBuilder builder = new StringBuilder();
		for (Object stmt : statements) {
			builder.append(stmt.toString());
		}
		return new CodeSnippet(adapter, eventSet, methodDesc, builder.toString());
	}
}
