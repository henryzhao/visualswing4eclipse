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
import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.EditorAction;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.designer.WidgetSelection;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.undo.DuplicateOperation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
/**
 * 
 * DuplicateAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class DuplicateAction extends EditorAction {
	private static String DUPLICATE_ACTION_ICON = "/icons/duplicate.png"; //$NON-NLS-1$

	public DuplicateAction() {
		setId(DUPLICATE);
		setText(Messages.DuplicateAction_Duplicate_Components);
		setToolTipText(Messages.DuplicateAction_Duplicate_Components);
		setAccelerator(SWT.CTRL | 'D');
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(DUPLICATE_ACTION_ICON));
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
		CompositeAdapter rootAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(designer.getRoot());
		List<Component> copyedList = new ArrayList<Component>();
		copyedList.addAll(designer.getSelectedComponents());
		IOperationHistory operationHistory = PlatformUI.getWorkbench()
				.getOperationSupport().getOperationHistory();
		IUndoableOperation operation = new DuplicateOperation(copyedList);
		operation.addContext(designer.getUndoContext());
		try {
			operationHistory.execute(operation, null, null);
		} catch (ExecutionException e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		rootAdapter.doLayout();
		designer.getRoot().validate();
		designer.publishSelection();
	}
	@Override
	public void addToMenu(IMenuManager editMenu) {
		editMenu.insertAfter(ITextEditorActionConstants.PASTE, this);
	}
}

