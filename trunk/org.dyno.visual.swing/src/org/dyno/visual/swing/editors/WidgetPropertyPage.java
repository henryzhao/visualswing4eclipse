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

import java.lang.reflect.Field;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.PropertySheetPage;

public class WidgetPropertyPage extends PropertySheetPage {
	private boolean initialized = false;
	private Action fAction;
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		initFilteredAction();
		final Tree tree = (Tree) getControl();
		tree.addListener(SWT.MeasureItem,
				new org.eclipse.swt.widgets.Listener() {
					@Override
					public void handleEvent(org.eclipse.swt.widgets.Event event) {
						event.height = 18;
					}
				});
	}

	private void initFilteredAction() {
		try {
			Field f = getClass().getSuperclass().getDeclaredField(
					"filterAction");
			f.setAccessible(true);
			fAction = (Action) f.get(this);
			fAction.setChecked(true);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (!initialized) {
			fAction.run();
			initialized = true;
		}
		super.selectionChanged(part, selection);
	}
}
