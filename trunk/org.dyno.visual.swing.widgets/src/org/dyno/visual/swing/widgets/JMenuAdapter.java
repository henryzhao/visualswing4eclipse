package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JMenuAdapter extends CompositeAdapter {
	private static int VAR_INDEX = 0;

	public JMenuAdapter() {
		super("jMenu" + (VAR_INDEX++));
	}

	@Override
	public Component cloneWidget() {
		JMenu copy = (JMenu) super.cloneWidget();
		JMenu origin = (JMenu) getWidget();
		copy.setText(origin.getText());
		int count = origin.getMenuComponentCount();
		for (int i = 0; i < count; i++) {
			Component child = origin.getMenuComponent(i);
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			copy.add(adapter.cloneWidget());
		}
		return copy;
	}

	public boolean isMoveable() {
		return false;
	}
	public boolean isResizable() {
		return false;
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
		JMenu jmi = (JMenu)me;
		return jmi.getText();
	}

	@Override
	public void setWidgetValue(Object value) {
		Component me = getWidget();
		JMenu jmi = (JMenu)me;
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
		JMenu jmi = (JMenu)me;
		int fw = fm.stringWidth(jmi.getText()) + HOR_TEXT_PAD;
		int fx = (w - fw) / 2;
		int fy = (h - fh) / 2;
		return new Rectangle(fx, fy, fw, fh);
	}
	private static final int HOR_TEXT_PAD = 20;
	private static final int VER_TEXT_PAD = 4;


	@Override
	public boolean widgetPressed(MouseEvent e) {
		JMenu jmenu = (JMenu) getWidget();
		Container parent = jmenu.getParent();
		if (parent instanceof JMenuBar) {
			jmenu.dispatchEvent(e);
		} else if (parent instanceof JPopupMenu) {
			MouseEvent me = new MouseEvent(jmenu, MouseEvent.MOUSE_ENTERED, e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e
					.isPopupTrigger(), e.getButton());
			jmenu.dispatchEvent(me);
		}
		return true;
	}

	public CompositeAdapter getParentAdapter() {
		Component me = getWidget();
		Component parent = me.getParent();
		if (parent instanceof JMenuBar)
			return (CompositeAdapter) WidgetAdapter.getWidgetAdapter(parent);
		else if (parent instanceof JPopupMenu) {
			JPopupMenu jpm = (JPopupMenu) parent;
			parent = jpm.getInvoker();
			return (CompositeAdapter) WidgetAdapter.getWidgetAdapter(parent);
		} else
			return null;
	}

	@Override
	public Component getChild(int index) {
		JMenu origin = (JMenu) getWidget();
		return origin.getMenuComponent(index);
	}

	@Override
	public int getChildCount() {
		JMenu origin = (JMenu) getWidget();
		return origin.getMenuComponentCount();
	}

	@Override
	public int getIndexOfChild(Component child) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			if (getChild(i) == child)
				return i;
		}
		return -1;
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
	}

	@Override
	public Object getChildConstraints(Component child) {
		return null;
	}

	@Override
	protected Component createWidget() {
		JMenu menu = new JMenu(){

			@Override
			public void setPopupMenuVisible(boolean b) {
				if(!b){
					StackTraceElement[] trace = Thread.currentThread().getStackTrace();
					for(StackTraceElement stack:trace){
						if(stack.getClassName().indexOf("MouseGrabber")!=-1&&stack.getMethodName().equals("cancelPopupMenu")){
							return;
						}
					}
				}
				super.setPopupMenuVisible(b);
			}
			
		};
		WidgetAdapter menuAdapter = ExtensionRegistry.createWidgetAdapter(JMenuItem.class);
		JMenuItem jmenu=(JMenuItem)menuAdapter.getWidget();
		jmenu.setText("menu item");
		menu.add(jmenu);
		menu.setText("menu");
		menu.setSize(menu.getPreferredSize());
		menu.doLayout();
		return menu;
	}

	@Override
	protected Component newWidget() {
		return new JMenu();
	}
}
