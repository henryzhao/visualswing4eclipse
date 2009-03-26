package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Component;
import java.awt.Rectangle;

import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.IEditor;

public class JWindowEditorAdapter extends RootPaneContainerEditorAdapter {
	private LabelEditor editor;
	@Override
	public IEditor getEditorAt() {
		if (getCaptionBounds().contains(hotspot)) {
			if (editor == null)
				editor = new LabelEditor();
			return editor;
		}
		return null;
	}

	private Rectangle getCaptionBounds() {
		Component jif = adaptable.getRootPane();
		int w = jif.getWidth();
		return new Rectangle(0, -23, w, 23);
	}

	@Override
	public Rectangle getEditorBounds() {
		if (getCaptionBounds().contains(hotspot)) {
			Component jif = adaptable.getRootPane();
			int w = jif.getWidth();
			return new Rectangle(19, -20, w -75, 22);
		} else
			return null;
	}
}
