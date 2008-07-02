package org.dyno.visual.swing.types.editor.spinnermodels;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class SpinnerModelContentProvider implements IStructuredContentProvider {
	private SpinnerModelType[]types;
	@Override
	public void dispose() {
		this.types = null;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.types = (SpinnerModelType[]) newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {		
		return types;
	}

}
