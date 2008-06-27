package org.dyno.visual.swing.designer;

import javax.swing.Icon;

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