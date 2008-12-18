/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.views.properties.PropertySheetPage;

public class WidgetPropertyPage extends PropertySheetPage {
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		final Tree tree = (Tree) getControl();
		tree.addListener(SWT.MeasureItem, new org.eclipse.swt.widgets.Listener(){
			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event event) {
				event.height = 18;
			}
		});
	}
}

