/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

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
