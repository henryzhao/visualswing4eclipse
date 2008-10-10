package org.dyno.visual.swing.widgets;

import java.awt.Component;

import javax.swing.JMenuBar;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JMenuBarAdapter extends CompositeAdapter {
	private static int VAR_INDEX = 0;
	public JMenuBarAdapter(){
		super("jMenuBar" + (VAR_INDEX++));
	}
	@Override
	public Component cloneWidget() {
		JMenuBar copy = (JMenuBar) super.cloneWidget();
		JMenuBar origin = (JMenuBar) getWidget();
		int count = origin.getMenuCount();
		for(int i=0;i<count;i++){
			Component child = getChild(i);
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			copy.add(adapter.cloneWidget());
		}
		return copy;
	}
	public boolean isResizable() {
		return false;
	}
	
	@Override
	public boolean isMoveable() {
		return false;
	}
	@Override
	public int getChildCount() {
		JMenuBar origin = (JMenuBar) getWidget();
		return origin.getMenuCount();
	}

	@Override
	public Component getChild(int index) {
		JMenuBar origin = (JMenuBar) getWidget();
		return origin.getMenu(index);
	}

	@Override
	public void addChild(Component widget) {
		JMenuBar origin = (JMenuBar) getWidget();
		origin.add(widget);
	}

	@Override
	public void removeAllChild() {
		JMenuBar origin = (JMenuBar) getWidget();
		origin.removeAll();
	}

	@Override
	public boolean removeChild(Component child) {
		JMenuBar origin = (JMenuBar) getWidget();
		origin.remove(child);
		return true;
	}
	
	@Override
	protected Component createWidget() {
		JMenuBar jmb = new JMenuBar();
		jmb.setSize(100, 25);
		jmb.doLayout();
		return jmb;
	}

	@Override
	protected Component newWidget() {
		return new JMenuBar();
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
	}

	@Override
	public Object getChildConstraints(Component child) {
		return null;
	}
}
