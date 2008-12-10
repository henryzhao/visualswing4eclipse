
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

package org.dyno.visual.swing.widgets;

import java.awt.Component;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JComponentAdapter extends WidgetAdapter {

	public JComponentAdapter() {
	}

	protected Component createWidget() {
		return null;
	}

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		return null;
	}

	@Override
	protected Component newWidget() {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JComponent.class;
	}
}

