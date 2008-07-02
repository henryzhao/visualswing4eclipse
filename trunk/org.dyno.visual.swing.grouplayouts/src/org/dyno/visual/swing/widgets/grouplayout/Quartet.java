package org.dyno.visual.swing.widgets.grouplayout;

class Quartet {
	public int masc;
	public int axis;
	public int start;
	public int end;
	public Anchor anchor;

	public Quartet(int a, int m, int s, int e, Anchor anchor) {
		axis = a;
		masc = m;
		start = s;
		end = e;
		this.anchor = anchor;
	}
}
