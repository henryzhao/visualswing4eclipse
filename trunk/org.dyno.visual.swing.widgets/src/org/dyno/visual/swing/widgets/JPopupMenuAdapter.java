package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPopupMenu;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JPopupMenuAdapter extends CompositeAdapter {

	@Override
	public Component cloneWidget() {
		JPopupMenu copy = (JPopupMenu) super.cloneWidget();
		JPopupMenu origin = (JPopupMenu) getWidget();
		int count = origin.getComponentCount();
		for(int i=0;i<count;i++){
			Component child = origin.getComponent(i);
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			copy.add(adapter.cloneWidget());
		}
		return copy;
	}

	@Override
	public Component getChild(int index) {
		JPopupMenu origin = (JPopupMenu) getWidget();
		return origin.getComponent(index);
	}

	@Override
	public int getChildCount() {
		JPopupMenu origin = (JPopupMenu) getWidget();
		return origin.getComponentCount();
	}

	@Override
	public int getIndexOfChild(Component child) {
		int count = getChildCount();
		for(int i=0;i<count;i++){
			if(getChild(i)==child)
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
		JPopupMenu menu = new JPopupMenu();
		menu.setSize(new Dimension(72,100));
		return menu;
	}

	@Override
	protected Component newWidget() {
		return new JPopupMenu();
	}
}
