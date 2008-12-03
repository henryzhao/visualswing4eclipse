
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

package org.dyno.visual.swing.lnfs.meta;

import javax.swing.SwingConstants;

public class MetaJCheckBoxValue extends MetaAbstractButtonValue {
	private static final long serialVersionUID = 1L;

	public MetaJCheckBoxValue() {
		put("opaque", true);
        put("defaultCapable", true);
		put("doubleBuffered", false);
		put("alignmentY", 0.5f);
		put("rolloverEnabled", false);
		put("borderPainted", false);
		put("horizontalAlignment", SwingConstants.LEADING);
		put("rolloverEnabled", true);
	}

}

