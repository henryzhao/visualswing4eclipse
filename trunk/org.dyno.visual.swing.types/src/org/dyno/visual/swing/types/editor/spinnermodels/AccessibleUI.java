package org.dyno.visual.swing.types.editor.spinnermodels;

import org.eclipse.swt.widgets.Control;

public interface AccessibleUI {
	Control getAccessibleUI();
	void setValue(Object value);
	Object getValue();
	String isInputValid();
}
