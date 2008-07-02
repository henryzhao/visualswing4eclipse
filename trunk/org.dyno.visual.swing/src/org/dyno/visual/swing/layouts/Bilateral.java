package org.dyno.visual.swing.layouts;

public class Bilateral extends Alignment {
	private static final long serialVersionUID = 1L;
	private int leading;
	private int trailing;

	public Bilateral(int leading, int trailing, Spring spring) {
		super(spring);
		this.leading = leading;
		this.trailing = trailing;
	}
	public Bilateral(int leading, int trailing, int min){
		this(leading, trailing, min, PREFERRED);
	}
	public Bilateral(int leading, int trailing, int min, int pref){
		super(min, pref);
		this.leading = leading;
		this.trailing = trailing;
	}
	public int getLeading() {
		return leading;
	}

	public int getTrailing() {
		return trailing;
	}

	public void setLeading(int leading) {
		this.leading = leading;
	}

	public void setTrailing(int trailing) {
		this.trailing = trailing;
	}
}
