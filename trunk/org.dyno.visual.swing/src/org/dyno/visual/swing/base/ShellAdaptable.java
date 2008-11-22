/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.base;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.widgets.Shell;

public class ShellAdaptable implements IAdaptable{
	private Shell shell;
	public ShellAdaptable(Shell shell){
		this.shell = shell;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapter) {
		if(adapter==Shell.class)
			return shell;
		return null;
	}

}
