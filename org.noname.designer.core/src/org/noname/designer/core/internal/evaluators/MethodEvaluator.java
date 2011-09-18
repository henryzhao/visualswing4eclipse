package org.noname.designer.core.internal.evaluators;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.noname.designer.core.interfaces.EvaluationContext;
import org.noname.designer.core.interfaces.IEvaluator;

public class MethodEvaluator implements IEvaluator {
	private MethodDeclaration method;

	public MethodEvaluator(MethodDeclaration method) {
		this.method = method;
	}

	/**
	 * @return RETURN | THROW | RETURN_VALUE
	 */
	@Override
	public int evaluate(EvaluationContext context) {
		Block body = method.getBody();
		IEvaluator evaluator = (IEvaluator) Platform.getAdapterManager()
				.getAdapter(body, IEvaluator.class);
		int result = evaluator.evaluate(context);
		switch (result) {
		case RETURN:
			return RETURN;
		case RETURN_VALUE:
			return RETURN_VALUE;
		case THROW:
			return THROW;
		}
		return RETURN;
	}

}
