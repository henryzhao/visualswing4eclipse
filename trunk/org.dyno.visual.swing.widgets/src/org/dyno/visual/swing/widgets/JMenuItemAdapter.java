package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JMenuItemAdapter extends WidgetAdapter {
	private static int VAR_INDEX = 0;

	public JMenuItemAdapter() {
		super("jMenuItem" + (VAR_INDEX++));
	}

	@Override
	protected Component createWidget() {
		JMenuItem jmi = new JMenuItem();
		jmi.setText("menu item");
		jmi.setSize(jmi.getPreferredSize());
		jmi.doLayout();
		return jmi;
	}

	public CompositeAdapter getParentAdapter() {
		Component me = getWidget();
		JPopupMenu jpm = (JPopupMenu) me.getParent();
		Component parent = jpm.getInvoker();
		return (CompositeAdapter) WidgetAdapter.getWidgetAdapter(parent);
	}

	public boolean isMoveable() {
		return true;
	}
	public boolean isResizable() {
		return false;
	}
	@Override
	protected Component newWidget() {
		JMenuItem jmi = new JMenuItem();
		return jmi;
	}
	
	private LabelEditor editor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		if (editor == null) {
			editor = new LabelEditor();
		}
		return editor;
	}

	@Override
	public Object getWidgetValue() {
		Component me = getWidget();
		JMenuItem jmi = (JMenuItem)me;
		return jmi.getText();
	}

	@Override
	public void setWidgetValue(Object value) {
		Component me = getWidget();
		JMenuItem jmi = (JMenuItem)me;
		jmi.setText(value==null?"":value.toString());
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		Component widget = getWidget();
		FontMetrics fm = widget.getFontMetrics(widget.getFont());
		int fh = fm.getHeight() + VER_TEXT_PAD;
		Component me = getWidget();
		JMenuItem jmi = (JMenuItem)me;
		int fw = fm.stringWidth(jmi.getText()) + HOR_TEXT_PAD;
		int fx = (w - fw) / 2;
		int fy = (h - fh) / 2;
		return new Rectangle(fx, fy, fw, fh);
	}
	private static final int HOR_TEXT_PAD = 20;
	private static final int VER_TEXT_PAD = 4;


	@Override
	public boolean widgetPressed(MouseEvent e) {
		JMenuItem jmenu = (JMenuItem) getWidget();
		Container parent = jmenu.getParent();
		if (parent instanceof JPopupMenu) {
			boolean selected=jmenu.isSelected();
			jmenu.setSelected(!selected);
		}
		return true;
	}

	@Override
	public Component cloneWidget() {
		JMenuItem jmi = (JMenuItem) super.cloneWidget();
		JMenuItem origin = (JMenuItem) getWidget();
		jmi.setText(origin.getText());
		return jmi;
	}
}
