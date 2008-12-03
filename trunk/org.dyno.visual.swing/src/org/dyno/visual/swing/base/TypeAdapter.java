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

import java.util.Comparator;

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.dyno.visual.swing.plugin.spi.ICloner;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
/**
 * 
 * TypeAdapter
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class TypeAdapter {
	private ICodeGen endec;
	@SuppressWarnings("unchecked")
	private Comparator comparator;
	private ILabelProviderFactory renderer;
	private ICellEditorFactory editor;
	private ICloner cloner;

	public ICodeGen getEndec() {
		if (endec != null)
			return endec;
		else
			return editor;
	}

	public void setEndec(ICodeGen endec) {
		this.endec = endec;
	}

	@SuppressWarnings("unchecked")
	public Comparator getComparator() {
		return comparator;
	}

	@SuppressWarnings("unchecked")
	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}

	public ILabelProviderFactory getRenderer() {
		return renderer;
	}

	public void setRenderer(ILabelProviderFactory renderer) {
		this.renderer = renderer;
	}

	public ICellEditorFactory getEditor() {
		return editor;
	}

	public void setEditor(ICellEditorFactory editor) {
		this.editor = editor;
	}

	public void setCloner(ICloner cloner) {
		this.cloner = cloner;
	}

	public ICloner getCloner() {
		return cloner;
	}
}

