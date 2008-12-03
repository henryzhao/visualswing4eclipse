
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

package org.dyno.visual.swing.widgets.grouplayout;

class Quartet {
	public int masc;
	public int axis;
	public int start;
	public int end;
	public Anchor anchor;

	public Quartet(int a, int m, int s, int e, Anchor anchor) {
		axis = a;
		masc = m;
		start = s;
		end = e;
		this.anchor = anchor;
	}
}

