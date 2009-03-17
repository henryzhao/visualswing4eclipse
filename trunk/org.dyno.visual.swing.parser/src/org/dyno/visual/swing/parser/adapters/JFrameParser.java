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
package org.dyno.visual.swing.parser.adapters;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class JFrameParser extends RootPaneContainerParser {
	@Override
	protected JMenuBar getJMenuBar() {
		JFrame jframe = (JFrame) adaptable.getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		return jmb;
	}
}
