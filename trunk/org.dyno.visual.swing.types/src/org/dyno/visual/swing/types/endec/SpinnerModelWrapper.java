package org.dyno.visual.swing.types.endec;

import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.dyno.visual.swing.types.editor.spinnermodels.SpinnerModelType;
import org.dyno.visual.swing.types.editor.spinnermodels.SpinnerNumberModelType;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class SpinnerModelWrapper implements ICodeGen {

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return null;
		SpinnerModelType type = SpinnerNumberModelType.getSpinnerModelType(value);
		return type.getInitJavaCode(value, imports);
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return "null";
		SpinnerModelType type = SpinnerNumberModelType.getSpinnerModelType(value);
		return type.getJavaCode(value, imports);
	}

}
