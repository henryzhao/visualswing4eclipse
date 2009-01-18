package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.editors.TabIconEditor;

public class JTabbedPaneEditorAdapter extends CompositeEdtiorAdapter {

	private IEditor lblEditor;
	private IEditor iconEditor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		if (lblEditor == null) {
			lblEditor = new LabelEditor();
		}
		if (iconEditor == null) {
			iconEditor = new TabIconEditor();
		}
		JTabbedPane jtp = (JTabbedPane) adaptable.getWidget();
		int index = getTabIndexAt(jtp, x, y);
		if (index != -1) {
			Rectangle bounds = jtp.getBoundsAt(index);
			if (jtp.getTabPlacement() == SwingConstants.TOP
					|| jtp.getTabPlacement() == SwingConstants.BOTTOM) {
				if (x > bounds.x && x < bounds.x + bounds.width / 2)
					return iconEditor;
				else
					return lblEditor;
			} else {
				if (y > bounds.y && y < bounds.y + bounds.height / 2)
					return iconEditor;
				else
					return lblEditor;
			}
		} else
			return null;
	}

	@Override
	public Object getWidgetValue(int x, int y) {
		JTabbedPane jtp = (JTabbedPane) adaptable.getWidget();
		int index = getTabIndexAt(jtp, x, y);
		if (index != -1) {
			Rectangle bounds = jtp.getBoundsAt(index);
			if (jtp.getTabPlacement() == SwingConstants.TOP
					|| jtp.getTabPlacement() == SwingConstants.BOTTOM) {
				if (x > bounds.x && x < bounds.x + bounds.width / 2)
					return jtp.getIconAt(index);
				else
					return jtp.getTitleAt(index);
			} else {
				if (y > bounds.y && y < bounds.y + bounds.height / 2)
					return jtp.getIconAt(index);
				else
					return jtp.getTitleAt(index);
			}
		} else
			return null;
	}

	@Override
	public void setWidgetValue(Object value) {
		JTabbedPane jtp = (JTabbedPane) adaptable.getWidget();
		int index = jtp.getSelectedIndex();
		if (index != -1) {
			if(value!=null){
				if (value instanceof Icon){
					jtp.setIconAt(index, (Icon)value);
					jtp.repaint();
				}else
					jtp.setTitleAt(index, (String)value);
			}else{
				jtp.setIconAt(index, null);
			}
		}
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		JTabbedPane jtp = (JTabbedPane) adaptable.getWidget();
		int index = getTabIndexAt(jtp, x, y);
		if (index != -1) {
			return jtp.getBoundsAt(index);
		} else
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
