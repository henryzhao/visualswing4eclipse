/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.editors;

import java.util.List;

import org.dyno.visual.swing.designer.VisualDesigner;
/**
 * 
 * ComponentTreeInput
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class ComponentTreeInput {
	private VisualDesigner root;
	private List<?> invisibles;

	public ComponentTreeInput(VisualDesigner root) {
		assert root != null;
		this.root = root;
	}

	public ComponentTreeInput(VisualDesigner root, List<?> invisibles) {
		this.root = root;
		this.invisibles = invisibles;
	}

	public VisualDesigner getDesigner() {
		return root;
	}

	public List<?> getInvisibles() {
		return invisibles;
	}
}
