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
/**
 * 
 * Trailing
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class Trailing extends Alignment {
	private static final long serialVersionUID = 1L;
	private int trailing;
	private int size;

	public Trailing(int trailing, int size, Spring spring) {
		super(spring);
		this.trailing = trailing;
		this.size = size;
	}
	public Trailing(int trailing, int min, int pref){
		this(trailing, PREFERRED, min, pref);
	}
	public Trailing(int trailing, int size, int min, int pref){
		super(min, pref);
		this.trailing = trailing;
		this.size = size;
	}
	public int getTrailing() {
		return trailing;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setTrailing(int trailing) {
		this.trailing = trailing;
	}
	@Override
	public Object clone(){
		return new Trailing(trailing, size, (Spring) getSpring().clone());
	}
	
	
}

