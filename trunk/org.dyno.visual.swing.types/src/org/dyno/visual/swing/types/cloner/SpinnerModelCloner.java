package org.dyno.visual.swing.types.cloner;

import org.dyno.visual.swing.plugin.spi.ICloner;
import org.dyno.visual.swing.types.editor.spinnermodels.SpinnerModelType;

public class SpinnerModelCloner implements ICloner {

	@Override
	public Object clone(Object object) {
		SpinnerModelType type = SpinnerModelType.getSpinnerModelType(object);
		return type.clone(object);
	}

}
