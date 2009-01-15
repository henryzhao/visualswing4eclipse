package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import javax.swing.JMenuItem;

import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.IEditor;

public class JRadioButtonMenuItemEditorAdapter extends WidgetEditorAdapter {

	private LabelEditor editor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		if (editor == null) {
			editor = new LabelEditor();
		}
		return editor;
	}

	@Override
	public Object getWidgetValue(int x, int y) {
		Component me = adaptable.getWidget();
		JMenuItem jmi = (JMenuItem)me;
		return jmi.getText();
	}

	@Override
	public void setWidgetValue(Object value) {
		Component me = adaptable.getWidget();
		JMenuItem jmi = (JMenuItem)me;
		jmi.setText(value==null?"":value.toString()); //$NON-NLS-1$
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = adaptable.getWidget().getWidth();
		int h = adaptable.getWidget().getHeight();
		Component widget = adaptable.getWidget();
		FontMetrics fm = widget.getFontMetrics(widget.getFont());
		int fh = fm.getHeight() + VER_TEXT_PAD;
		Component me = adaptable.getWidget();
		JMenuItem jmi = (JMenuItem)me;
		int fw = fm.stringWidth(jmi.getText()) + HOR_TEXT_PAD;
		int fx = (w - fw) / 2;
		int fy = (h - fh) / 2;
		return new Rectangle(fx, fy, fw, fh);
	}
	private static final int HOR_TEXT_PAD = 20;
	private static final int VER_TEXT_PAD = 4;

}
