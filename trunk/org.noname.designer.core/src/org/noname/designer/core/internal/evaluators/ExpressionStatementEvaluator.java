package org.noname.designer.core.internal.evaluators;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.noname.designer.core.interfaces.EvaluationContext;
import org.noname.designer.core.interfaces.IEvaluator;

public class ExpressionStatementEvaluator implements IEvaluator {
	private ExpressionStatement expressionStatement;
	public ExpressionStatementEvaluator(ExpressionStatement expStmt) {
		expressionStatement = expStmt;
	}

	/**
	 * @return OK | THROW
	 */
	@Override
	public int evaluate(EvaluationContext context) {
		Expression expression = expressionStatement.getExpression();
		IEvaluator evaluator = (IEvaluator) Platform.getAdapterManager().getAdapter(expression, IEvaluator.class);
		return evaluator.evaluate(context);
	}

}
