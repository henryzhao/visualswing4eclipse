/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.properties;

import org.dyno.visual.swing.base.WidgetProperty;
import org.eclipse.jface.viewers.IStructuredSelection;

public class HorizontalTextPositionProperty extends WidgetProperty {
	@Override
	public boolean isPropertySet(String lnfClassname, IStructuredSelection bean) {
		return false;
	}

}
