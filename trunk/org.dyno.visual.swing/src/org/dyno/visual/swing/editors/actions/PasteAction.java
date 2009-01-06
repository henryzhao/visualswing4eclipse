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

import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.EditorAction;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.undo.PasteOperation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
/**
 * 
 * PasteAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class PasteAction extends EditorAction {
	public PasteAction() {
		setId(ActionFactory.PASTE.getId());
		setToolTipText(Messages.PasteAction_Paste_Components);
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		setAccelerator(SWT.CTRL | 'V');
		setRetargetable(true);
		setEnabled(false);
	}
	@Override
	public void updateState() {
		VisualDesigner designer = getDesigner();
		if(designer==null)
			return;
		setEnabled(!designer.getClipboard().isEmpty());
	}

	@Override
	public void run() {
		VisualDesigner designer = getDesigner();
		if(designer==null)
			return;
		CompositeAdapter rootAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(designer.getRoot());
		List<WidgetAdapter> copyedList = new ArrayList<WidgetAdapter>();
		copyedList.addAll(designer.getClipboard());
		IOperationHistory operationHistory = PlatformUI.getWorkbench()
				.getOperationSupport().getOperationHistory();
		IUndoableOperation operation = new PasteOperation(copyedList,
				rootAdapter);
		operation.addContext(designer.getUndoContext());
		try {
			operationHistory.execute(operation, null, null);
		} catch (ExecutionException e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		rootAdapter.doLayout();
		designer.getClipboard().clear();
		designer.getRoot().validate();
		designer.publishSelection();
	}
	@Override
	public ActionFactory getActionFactory() {
		return ActionFactory.PASTE;
	}

}

