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

package org.dyno.visual.swing.layouts;

import java.io.Serializable;

/**
 * 
 * Spring
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class Spring implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private int minimum;
	private int preferred;

	public Spring(int min, int pref) {
		minimum = min;
		preferred = pref;
	}

	@Override
	public Object clone() {
		return new Spring(minimum, preferred);
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getPreferred() {
		return preferred;
	}

	public void setPreferred(int preferred) {
		this.preferred = preferred;
	}
}

