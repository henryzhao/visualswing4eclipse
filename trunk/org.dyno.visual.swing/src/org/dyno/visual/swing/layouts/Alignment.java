package org.dyno.visual.swing.layouts;

import java.io.Serializable;

public abstract class Alignment implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int PREFERRED = -1;
	private Spring spring;

	public Alignment(Spring spring) {
		this.spring = spring;
	}
	public Alignment(int min, int pref){
		this.spring = new Spring(min, pref);
	}
	public Spring getSpring() {
		return spring;
	}

	public void setSpring(Spring spring) {
		this.spring = spring;
	}
}
