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

package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.EditorAction;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.ui.actions.ActionFactory;
/**
 * 
 * SelectAllAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class SelectAllAction extends EditorAction {
	private static String SELECT_ALL_ICON = "/icons/selall.png"; //$NON-NLS-1$

	public SelectAllAction() {
		setId(ActionFactory.SELECT_ALL.getId());
		setToolTipText(Messages.SelectAllAction_Select_All);
		setAccelerator(SWT.CTRL | 'A');
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(SELECT_ALL_ICON));
		setRetargetable(true);
		setEnabled(false);
	}
	@Override
	public void updateState() {
		VisualDesigner designer = getDesigner();
		if(designer==null)
			return;
		WidgetAdapter rootAdapter = WidgetAdapter.getWidgetAdapter(designer.getRoot());
		setEnabled(((CompositeAdapter) rootAdapter)
				.getChildCount() > 0);
	}

	@Override
	public void run() {
		VisualDesigner designer = getDesigner();
		if(designer==null)
			return;
		CompositeAdapter rootAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(designer.getRoot());
		rootAdapter.setSelected(false);
		rootAdapter.selectChildren();
		designer.publishSelection();
		designer.repaint();
	}

	@Override
	public ActionFactory getActionFactory() {
		return ActionFactory.SELECT_ALL;
	}

}

