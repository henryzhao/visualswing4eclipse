/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.designer;

import javax.swing.Icon;
/**
 * 
 * Node
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
class Node {
	private String name;
	private Object value;
	private Icon icon;

	public Node(String name) {
		this.name = name;
	}

	public void setValue(Object v) {
		this.value = v;
	}

	public Object getValue() {
		return this.value;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public Icon getIcon() {
		return this.icon;
	}

	public String toString() {
		return name;
	}
}