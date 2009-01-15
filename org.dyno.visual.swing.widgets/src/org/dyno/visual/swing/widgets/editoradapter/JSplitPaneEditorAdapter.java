package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;

import javax.swing.JSplitPane;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.editors.TransparentSplitterEditor;

public class JSplitPaneEditorAdapter extends CompositeEdtiorAdapter {

	private IEditor editor;
	@Override
	public IEditor getEditorAt(int x, int y) {
		if(editor==null)
			editor = new TransparentSplitterEditor((JSplitPane) adaptable.getWidget());
		return editor;
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = adaptable.getWidget().getWidth();
		int h = adaptable.getWidget().getHeight();
		return new Rectangle(0, 0, w, h);
	}

	@Override
	public Object getWidgetValue(int x, int y) {
		JSplitPane jsp = (JSplitPane) adaptable.getWidget();
		return jsp.getDividerLocation();
	}

	@Override
	public void setWidgetValue(Object value) {
		JSplitPane jsp = (JSplitPane) adaptable.getWidget();
		int div = value==null?0:((Integer)value).intValue();
		jsp.setDividerLocation(div);
	}

}
