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
import org.dyno.visual.swing.designer.WidgetSelection;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.undo.DeleteOperation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
/**
 * 
 * DeleteAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class DeleteAction extends EditorAction {
	public DeleteAction() {
		setId(ActionFactory.DELETE.getId());
		setToolTipText(Messages.DeleteAction_Delete_Components);
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		setAccelerator(SWT.DEL);
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
		IOperationHistory operationHistory = PlatformUI.getWorkbench()
				.getOperationSupport().getOperationHistory();
		IUndoableOperation operation = new DeleteOperation(designer
				.getSelectedComponents(), designer.getRoot());
		operation.addContext(designer.getUndoContext());
		try {
			operationHistory.execute(operation, null, null);
		} catch (ExecutionException e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		designer.invalidate();
		designer.doLayout();
		designer.repaint();
	}

	@Override
	public ActionFactory getActionFactory() {
		return ActionFactory.DELETE;
	}

}

