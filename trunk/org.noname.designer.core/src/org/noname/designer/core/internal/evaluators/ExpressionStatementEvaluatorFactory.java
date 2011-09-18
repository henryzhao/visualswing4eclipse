package org.noname.designer.core.internal.evaluators;

import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.noname.designer.core.interfaces.IEvaluator;

public class ExpressionStatementEvaluatorFactory extends ASTEvaluatorFactory {

	@Override
	protected IEvaluator createEvaluator(Object adaptable) {
		if(adaptable instanceof ExpressionStatement){
			ExpressionStatement expStmt = (ExpressionStatement) adaptable;
			return new ExpressionStatementEvaluator(expStmt);
		}
		return null;
	}

}
