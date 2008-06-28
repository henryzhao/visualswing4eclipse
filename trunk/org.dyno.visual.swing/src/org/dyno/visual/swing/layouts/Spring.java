package org.dyno.visual.swing.layouts;

import java.io.Serializable;

public class Spring implements Serializable {
	private static final long serialVersionUID = 1L;
	private int minimum;
	private int preferred;
	public Spring(int min, int pref) {
		minimum = min;
		preferred = pref;
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
