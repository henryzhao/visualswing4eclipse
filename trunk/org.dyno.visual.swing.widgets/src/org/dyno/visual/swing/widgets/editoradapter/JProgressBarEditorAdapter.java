package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;

import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.IntegerTextEditor;

public class JProgressBarEditorAdapter extends WidgetEditorAdapter {

	private IEditor iEditor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		if (iEditor == null) {
			iEditor = new IntegerTextEditor();
		}
		return iEditor;
	}

	@Override
	public Object getWidgetValue(int x, int y) {
		return ((JProgressBar) adaptable.getWidget()).getValue();
	}

	@Override
	public void setWidgetValue(Object value) {
		((JProgressBar) adaptable.getWidget()).setValue(((Number) value).intValue());
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = adaptable.getWidget().getWidth();
		int h = adaptable.getWidget().getHeight();
		int oritention = ((JProgressBar) adaptable.getWidget()).getOrientation();
		if (oritention == SwingConstants.HORIZONTAL) {
			return new Rectangle((w - 40) / 2, 0, 40, 20);
		} else {
			return new Rectangle(0, (h - 20) / 2, w, 20);
		}
	}

}
