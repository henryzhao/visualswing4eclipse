package org.noname.designer.core.internal.evaluators;

import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.noname.designer.core.interfaces.EvaluationContext;
import org.noname.designer.core.interfaces.IEvaluator;
import org.noname.designer.core.interfaces.VariableContext;

public class BlockEvaluator implements IEvaluator {
	private Block block;

	public BlockEvaluator(Block block) {
		this.block = block;
	}

	/**
	 * @param OK
	 *            | CONTINUE | BREAK | RETURN | THROW | RETURN_VALUE
	 */
	@Override
	public int evaluate(EvaluationContext context) {
		VariableContext varCtx = new VariableContext();
		context.peek().push(varCtx);
		List statements = block.statements();
		try {
			if (statements != null && !statements.isEmpty()) {
				for (int i = 0; i < statements.size(); i++) {
					Statement statement = (Statement) statements.get(i);
					IEvaluator evaluator = (IEvaluator) Platform.getAdapterManager().getAdapter(statement, IEvaluator.class);
					int result = evaluator.evaluate(context);
					switch (result) {
					case CONTINUE:
						return CONTINUE;
					case BREAK:
						return BREAK;
					case RETURN:
						return RETURN;
					case RETURN_VALUE:
						return RETURN_VALUE;
					case THROW:
						return THROW;
					}
				}
			}
			return OK;
		} finally {
			context.peek().popup();
		}
	}

}
