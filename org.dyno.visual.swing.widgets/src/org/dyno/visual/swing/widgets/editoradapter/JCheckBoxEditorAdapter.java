package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;

public class JCheckBoxEditorAdapter extends TextWidgetEditorAdapter {

	private static final int BOX_THUMB = 16;

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		Rectangle bounds = super.getEditorBounds(x, y);
		bounds.x += BOX_THUMB;
		bounds.width -= BOX_THUMB;
		return bounds;
	}
}
