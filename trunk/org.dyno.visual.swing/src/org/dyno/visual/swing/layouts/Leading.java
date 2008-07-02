package org.dyno.visual.swing.layouts;

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
}
