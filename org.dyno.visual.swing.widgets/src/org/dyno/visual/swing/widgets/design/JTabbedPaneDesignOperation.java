package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Point;

import javax.swing.JTabbedPane;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JTabbedPaneDesignOperation extends CompositeDesignOperation {

	@Override
	public boolean dragEnter(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragEnter(p);
		hovered = true;
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragExit(p);
		hovered = false;
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragOver(p);
		adaptable.setMascotLocation(p);
		return true;
	}

	@Override
	public boolean drop(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.drop(p);
		adaptable.clearAllSelected();
		for (WidgetAdapter adapter : adaptable.getDropWidget()) {
			Component child = adapter.getParentContainer();
			JTabbedPane jtp = (JTabbedPane) adaptable.getWidget();
			if(adapter.getName()==null)
				adapter.setName(adaptable.getNamespace().nextName(adapter.getBasename()));
			jtp.addTab(adapter.getName(), child);
			jtp.setSelectedComponent(child);
			adapter.setSelected(true);
			adapter.requestNewName();
		}
		hovered = false;
		return true;
	}

	private boolean hovered;

	public boolean isHovered() {
		return hovered;
	}
}
