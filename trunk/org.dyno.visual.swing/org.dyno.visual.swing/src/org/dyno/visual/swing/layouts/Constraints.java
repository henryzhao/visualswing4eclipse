/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.layouts;

import java.io.Serializable;

/**
 * 
 * Constraints
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class Constraints implements Serializable {
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
