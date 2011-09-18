package org.noname.designer.core.internal.evaluators;

import org.eclipse.jdt.core.dom.MethodInvocation;
import org.noname.designer.core.interfaces.IEvaluator;

public class MethodInvocationEvaluatorFactory extends ASTEvaluatorFactory {

	@Override
	protected IEvaluator createEvaluator(Object adaptable) {
		if(adaptable instanceof MethodInvocation){
			MethodInvocation mi = (MethodInvocation) adaptable;
			return new MethodInvocationEvaluator(mi);
		}
		return null;
	}

}
