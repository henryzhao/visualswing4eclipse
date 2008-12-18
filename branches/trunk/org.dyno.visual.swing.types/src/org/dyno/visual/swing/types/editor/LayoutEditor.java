
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

package org.dyno.visual.swing.types.editor;

import org.dyno.visual.swing.base.FactoryEditor;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.types.items.LayoutItems;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class LayoutEditor extends FactoryEditor {
	public LayoutEditor() {
		super(new LayoutItems());
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		if(value==null)
			return null;
		BorderAdapter adapter = BorderAdapter.getBorderAdapter(value.getClass());
		return adapter.getInitJavaCode(value, imports);
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if(value==null)
			return "null";
		BorderAdapter adapter = BorderAdapter.getBorderAdapter(value.getClass());
		return adapter.getJavaCode(value, imports);
	}
}

