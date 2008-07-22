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
 * Alignment
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public abstract class Alignment implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	public static final int PREFERRED = -1;
	private Spring spring;

	public Alignment(Spring spring) {
		this.spring = spring;
	}

	public Alignment(int min, int pref) {
		this.spring = new Spring(min, pref);
	}

	public Spring getSpring() {
		return spring;
	}

	public void setSpring(Spring spring) {
		this.spring = spring;
	}

	@Override
	public abstract Object clone();
}
