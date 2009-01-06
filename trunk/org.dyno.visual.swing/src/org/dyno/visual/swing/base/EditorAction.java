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

package org.dyno.visual.swing.base;

import java.awt.Component;

import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.designer.WidgetSelection;
import org.dyno.visual.swing.editors.VisualSwingEditor;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

/**
 * 
 * EditorAction
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public abstract class EditorAction extends Action {

	public static final String ALIGNMENT_BOTTOM = "bottom";
	public static final String ALIGNMENT_CENTER = "center";
	public static final String ALIGNMENT_LEFT = "left";
	public static final String ALIGNMENT_MIDDLE = "middle";
	public static final String ALIGNMENT_RIGHT = "right";
	public static final String ALIGNMENT_TOP = "top";

	public static final String SAME_WIDTH = "same_width";
	public static final String SAME_HEIGHT = "same_height";

	public static final String PREVIEW = "preview";
	public static final String SOURCE = "source";
	public static final String DUPLICATE = "duplicate";

	protected VisualSwingEditor editor;
	private boolean retargetable;

	public EditorAction() {
	}
	public void updateState() {
		VisualDesigner designer = getDesigner();
		if(designer==null)
			return;
		setEnabled(isAlignResize(designer, 1, getId()));
	}
	private boolean isAlignResize(VisualDesigner designer, int count, String id) {
		WidgetSelection selection = new WidgetSelection(designer.getRoot());
		if (selection.size() > count) {
			WidgetAdapter parentAdapter = null;
			for (Component selected : selection) {
				WidgetAdapter selectedAdapter = WidgetAdapter
						.getWidgetAdapter(selected);
				WidgetAdapter selectedParent = selectedAdapter
						.getParentAdapter();
				if (parentAdapter == null) {
					parentAdapter = selectedParent;
				} else if (parentAdapter != selectedParent) {
					return false;
				}
			}
			if (parentAdapter == null) {
				return false;
			} else {
				for (Component selected : selection) {
					WidgetAdapter selectedAdapter = WidgetAdapter
							.getWidgetAdapter(selected);
					if (!selectedAdapter.isResizable()) {
						return false;
					}
				}
				return ((CompositeAdapter) parentAdapter)
						.isSelectionAlignResize(id);
			}
		} else {
			return false;
		}
	}
	@Override
	public void run() {
		VisualDesigner designer = getDesigner();
		if (designer == null)
			return;
		Component child = designer.getSelectedComponents().get(0);
		WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
		CompositeAdapter parentAdapter = (CompositeAdapter) childAdapter
				.getParentAdapter();
		parentAdapter.doAlignment(getId());
		designer.publishSelection();
	}

	public void setEditor(VisualSwingEditor editor) {
		this.editor = editor;
		this.editor.addAction(this);
	}

	protected VisualDesigner getDesigner() {
		if (editor == null)
			return null;
		VisualDesigner designer = editor.getDesigner();
		if (designer == null)
			return null;
		if (designer.isWidgetEditing())
			return null;
		return designer;
	}

	protected void setRetargetable(boolean b) {
		this.retargetable = b;
	}

	public boolean isRetargetable() {
		return retargetable;
	}

	public ActionFactory getActionFactory() {
		return null;
	}

	public void addToMenu(IMenuManager editMenu) {
		editMenu.add(this);
	}
}
