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
 * Leading
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class Leading extends Alignment {
	private static final long serialVersionUID = 1L;
	private int leading;
	private int size;

	public Leading(int leading, int size, Spring spring) {
		super(spring);
		this.leading = leading;
		this.size = size;
	}
	public Leading(int leading, int min, int pref){
		this(leading, PREFERRED, min, pref);
	}
	public Leading(int leading, int size, int min, int pref) {
		super(min, pref);
		this.leading = leading;
		this.size = size;
	}
	public int getLeading() {
		return leading;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setLeading(int leading) {
		this.leading = leading;
	}
	
	@Override
	public Object clone() {
		return new Leading(leading, size, (Spring) getSpring().clone());
	}	
}

