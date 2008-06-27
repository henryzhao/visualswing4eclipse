package org.dyno.visual.swing.layouts;

import java.io.Serializable;

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
}
