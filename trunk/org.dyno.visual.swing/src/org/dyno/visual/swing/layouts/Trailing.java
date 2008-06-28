package org.dyno.visual.swing.layouts;

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
}
