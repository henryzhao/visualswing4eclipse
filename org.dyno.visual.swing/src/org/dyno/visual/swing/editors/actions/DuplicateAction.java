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
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.EditorAction;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.designer.WidgetSelection;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
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
		WidgetSelection selection = new WidgetSelection(designer.getRoot());
		List<WidgetAdapter> copyedList = new ArrayList<WidgetAdapter>();
		for (Component child : selection) {
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			WidgetAdapter cloneAdapter = (WidgetAdapter) childAdapter.clone();
			Component comp = cloneAdapter.getWidget();
			comp.setSize(child.getSize());
			cloneAdapter.setHotspotPoint(new Point(child.getWidth()/2, child.getHeight()/2));
			copyedList.add(cloneAdapter);
		}
		System.out.println("copy list size:"+copyedList.size());
		designer.setSelectedWidget(copyedList);
	}
	@Override
	public void addToMenu(IMenuManager editMenu) {
		editMenu.insertAfter(ITextEditorActionConstants.PASTE, this);
	}
}

