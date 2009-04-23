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

import org.dyno.visual.swing.base.EditorAction;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
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
		boolean notEmpty =!designer.getClipboard().isEmpty();
		boolean hasFocused=designer.getFocusedContainer()!=null;
		setEnabled(hasFocused&&notEmpty);
	}

	@Override
	public void run() {
		VisualDesigner designer = getDesigner();
		if(designer==null)
			return;
		List<WidgetAdapter> copyedList = new ArrayList<WidgetAdapter>();
		copyedList.addAll(designer.getClipboard());
		designer.setSelectedWidget(copyedList);
	}
	@Override
	public ActionFactory getActionFactory() {
		return ActionFactory.PASTE;
	}

}

