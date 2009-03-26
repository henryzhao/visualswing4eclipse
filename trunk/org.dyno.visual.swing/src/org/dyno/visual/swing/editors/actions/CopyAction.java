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

import java.awt.Component;

import org.dyno.visual.swing.base.EditorAction;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.designer.WidgetSelection;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
/**
 * 
 * CopyAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class CopyAction extends EditorAction {
	public CopyAction() {
		setId(ActionFactory.COPY.getId());
		setToolTipText(Messages.CopyAction_Copy_Components);
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
		setAccelerator(SWT.CTRL | 'C');
		setRetargetable(true);
		setEnabled(false);
	}

	@Override
	public void updateState() {
		VisualDesigner designer = getDesigner();
		if(designer==null)
			return;
		WidgetSelection selection = new WidgetSelection(designer.getRoot());
		WidgetAdapter rootAdapter = WidgetAdapter.getWidgetAdapter(designer.getRoot());
		setEnabled(!selection.isEmpty()
				&& !rootAdapter.isSelected());
	}

	@Override
	public void run() {
		VisualDesigner designer = getDesigner();
		if(designer==null)
			return;
		designer.getClipboard().clear();
		for (Component child : designer.getSelectedComponents()) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			designer.getClipboard().add((WidgetAdapter) adapter.clone());
		}
		designer.publishSelection();
	}
	@Override
	public ActionFactory getActionFactory() {
		return ActionFactory.COPY;
	}
}

