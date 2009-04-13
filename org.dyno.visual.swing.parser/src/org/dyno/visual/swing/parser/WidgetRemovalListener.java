package org.dyno.visual.swing.parser;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IWidgetListener;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetEvent;

@SuppressWarnings("unchecked")
public class WidgetRemovalListener implements IWidgetListener{
	
	public void widgetAdded(WidgetEvent event) {
		WidgetAdapter adapter = event.getParent().getRootAdapter();
		List<String> names = (List<String>) adapter.getProperty("removed.components");
		if (names == null) {
			names = new ArrayList<String>();
			adapter.setProperty("removed.components", names);
		}
		WidgetAdapter targetAdapter = event.getTarget();
		String ID = targetAdapter.getID();
		if (names.contains(ID))
			names.remove(ID);
	}

	
	public void widgetMoved(WidgetEvent event) {
	}

	
	public void widgetRemoved(WidgetEvent event) {
		WidgetAdapter adapter = event.getParent().getRootAdapter();
		List<String> names = (List<String>) adapter.getProperty("removed.components");
		if (names == null) {
			names = new ArrayList<String>();
			adapter.setProperty("removed.components", names);
		}
		WidgetAdapter targetAdapter = event.getTarget();
		removeNameRecursively(targetAdapter, names);
	}

	private void removeNameRecursively(WidgetAdapter targetAdapter, List<String> names) {
		String ID = targetAdapter.getID();
		if (!names.contains(ID))
			names.add(ID);
		Component targetComponent = targetAdapter.getWidget();
		if(targetComponent instanceof JComponent){
			JComponent target = (JComponent) targetComponent;
			if(JavaUtil.getComponentPopupMenu(target)!=null){
				JPopupMenu jpm = JavaUtil.getComponentPopupMenu(target);
				if(jpm!=null&&WidgetAdapter.getWidgetAdapter(jpm)!=null){
					WidgetAdapter jpmAdapter = WidgetAdapter.getWidgetAdapter(jpm);
					removeNameRecursively(jpmAdapter, names);
				}
			}
		}
		if(targetAdapter instanceof CompositeAdapter){
			CompositeAdapter composite = (CompositeAdapter)targetAdapter;
			int count = composite.getChildCount();
			for(int i=0;i<count;i++){
				Component child = composite.getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
				removeNameRecursively(childAdapter, names);
			}
		}
	}

	
	public void widgetResized(WidgetEvent event) {
	}
}
