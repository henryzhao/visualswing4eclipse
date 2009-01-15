package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;

import javax.swing.JTabbedPane;

import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.IEditor;

public class JTabbedPaneEditorAdapter extends CompositeEdtiorAdapter {

	private IEditor iEditor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		if (iEditor == null) {
			iEditor = new LabelEditor();
		}
		return iEditor;
	}
	@Override
	public Object getWidgetValue(int x, int y) {
		JTabbedPane jtp = (JTabbedPane) adaptable.getWidget();
		int index = getTabIndexAt(jtp, x, y);
		if (index != -1) {
			return jtp.getTitleAt(index);
		}
		return null;		
	}

	@Override
	public void setWidgetValue(Object value) {
		JTabbedPane jtp = (JTabbedPane) adaptable.getWidget();
		int index = jtp.getSelectedIndex();
		if (index != -1) {
			jtp.setTitleAt(index, (String) value);
		}
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		JTabbedPane jtp = (JTabbedPane) adaptable.getWidget();
		int index = getTabIndexAt(jtp, x, y);
		if (index != -1) {
			return jtp.getBoundsAt(index);
		}
		return null;
	}

	private int getTabIndexAt(JTabbedPane jtp, int x, int y) {
		int count = jtp.getTabCount();
		for (int i = 0; i < count; i++) {
			Rectangle rect = jtp.getBoundsAt(i);
			if ((rect != null) && rect.contains(x, y)) {
				return i;
			}
		}
		return -1;
	}
}
