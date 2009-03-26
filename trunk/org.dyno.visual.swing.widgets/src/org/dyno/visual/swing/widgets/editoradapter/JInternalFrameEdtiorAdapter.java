package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;

import javax.swing.JInternalFrame;

import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.IEditor;

public class JInternalFrameEdtiorAdapter extends RootPaneContainerEditorAdapter {

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
		JInternalFrame jif = (JInternalFrame) adaptable.getWidget();
		int w = jif.getWidth();
		return new Rectangle(0, 0, w, 30);
	}

	@Override
	public Rectangle getEditorBounds() {
		if (getCaptionBounds().contains(hotspot)) {
			return new Rectangle(4, 4, adaptable.getWidget().getWidth() - 8, 23);
		} else
			return null;
	}

	@Override
	public Object getWidgetValue() {
		JInternalFrame jif = (JInternalFrame) adaptable.getWidget();
		return jif.getTitle();
	}

	@Override
	public void setWidgetValue(Object value) {
		JInternalFrame jif = (JInternalFrame) adaptable.getWidget();
		jif.setTitle((String) value);
	}
}
