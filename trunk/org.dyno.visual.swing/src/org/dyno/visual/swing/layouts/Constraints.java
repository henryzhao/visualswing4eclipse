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
 * Constraints
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class Constraints implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private Alignment horizontal;
	private Alignment vertical;

	public Constraints(Alignment h, Alignment v) {
		this.horizontal = h;
		this.vertical = v;
	}

	public Alignment getHorizontal() {
		return horizontal;
	}

	@Override
	public Object clone() {
		return new Constraints((Alignment) (horizontal == null ? null : horizontal.clone()), (Alignment) (vertical == null ? null : vertical.clone()));
	}

	public Alignment getVertical() {
		return vertical;
	}

	public void setHorizontal(Alignment horizontal) {
		this.horizontal = horizontal;
	}

	public void setVertical(Alignment vertical) {
		this.vertical = vertical;
	}
}
