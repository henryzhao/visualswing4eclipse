package org.noname.designer.core.internal.evaluators;

import org.eclipse.jdt.core.dom.Block;
import org.noname.designer.core.interfaces.IEvaluator;

public class BlockEvaluatorFactory extends ASTEvaluatorFactory {
	@Override
	protected IEvaluator createEvaluator(Object adaptable) {
		if(adaptable instanceof Block){
			Block block = (Block) adaptable;
			return new BlockEvaluator(block);
		}
		return null;
	}
}
