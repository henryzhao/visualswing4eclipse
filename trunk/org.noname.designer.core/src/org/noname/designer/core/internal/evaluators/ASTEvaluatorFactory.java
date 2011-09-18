package org.noname.designer.core.internal.evaluators;

import org.eclipse.core.runtime.IAdapterFactory;
import org.noname.designer.core.interfaces.IEvaluator;

public abstract class ASTEvaluatorFactory implements IAdapterFactory {
	private Class[] adapters = { IEvaluator.class };

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IEvaluator.class) {
			return createEvaluator(adaptableObject);
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return adapters;
	}

	protected abstract IEvaluator createEvaluator(Object adaptable);
}
