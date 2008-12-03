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

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
/**
 * 
 * FactoryEditor
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public abstract class FactoryEditor implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;
	protected IFactoryProvider provider;
	public FactoryEditor(IFactoryProvider provider){
		this.provider = provider;
	}
	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		return new FactoryCellEditor(bean, parent, provider);
	}

	@Override
	public Object decodeValue(Object value) {
		return value;
	}

	@Override
	public Object encodeValue(Object value) {
		return value;
	}

}

