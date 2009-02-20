package org.dyno.visual.swing.parser;

import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.plugin.spi.IWidgetListener;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetEvent;

@SuppressWarnings("unchecked")
public class WidgetRemovalListener implements IWidgetListener{
	@Override
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

	@Override
	public void widgetMoved(WidgetEvent event) {
	}

	@Override
	public void widgetRemoved(WidgetEvent event) {
		WidgetAdapter adapter = event.getParent().getRootAdapter();
		List<String> names = (List<String>) adapter.getProperty("removed.components");
		if (names == null) {
			names = new ArrayList<String>();
			adapter.setProperty("removed.components", names);
		}
		WidgetAdapter targetAdapter = event.getTarget();
		String ID = targetAdapter.getID();
		if (!names.contains(ID))
			names.add(ID);
	}

	@Override
	public void widgetResized(WidgetEvent event) {
	}
}
