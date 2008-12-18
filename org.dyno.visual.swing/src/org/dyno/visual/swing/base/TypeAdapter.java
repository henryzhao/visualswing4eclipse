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
import org.dyno.visual.swing.plugin.spi.IEndec;
import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.dyno.visual.swing.plugin.spi.IValueParser;
/**
 * 
 * TypeAdapter
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class TypeAdapter {
	private ICodeGen codegen;
	@SuppressWarnings("unchecked")
	private Comparator comparator;
	private ILabelProviderFactory renderer;
	private ICellEditorFactory editor;
	private ICloner cloner;
	private IValueParser parser;
	private IEndec endec;
	public IEndec getEndec() {
		return endec;
	}

	public void setEndec(IEndec endec) {
		this.endec = endec;
	}

	public IValueParser getParser() {
		return parser;
	}

	public void setParser(IValueParser parser) {
		this.parser = parser;
	}

	public ICodeGen getCodegen() {
		if (codegen != null)
			return codegen;
		else
			return editor;
	}

	public void setCodegen(ICodeGen codegen) {
		this.codegen = codegen;
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

