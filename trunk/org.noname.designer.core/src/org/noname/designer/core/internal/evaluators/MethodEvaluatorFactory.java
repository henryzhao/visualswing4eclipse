package org.noname.designer.core.internal.evaluators;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.noname.designer.core.interfaces.IEvaluator;

public class MethodEvaluatorFactory extends ASTEvaluatorFactory{
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adaptableObject instanceof MethodDeclaration){
			MethodDeclaration method = (MethodDeclaration) adaptableObject;
			if(adapterType==IEvaluator.class){
				return new MethodEvaluator(method);
			}
		}
		return null;
	}

	@Override
	protected IEvaluator createEvaluator(Object adaptable) {
		if(adaptable instanceof MethodDeclaration){
			MethodDeclaration method = (MethodDeclaration) adaptable;
			return new MethodEvaluator(method);
		}
		return null;
	}
}
