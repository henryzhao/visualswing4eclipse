package org.dyno.visual.swing.plugin.context;

import java.awt.Component;
import java.util.List;

import org.dyno.visual.swing.base.ContextCustomizerAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.MenuManager;

public class SingleAdapterCustomizer extends ContextCustomizerAdapter {
	@Override
	public void fillContextMenu(MenuManager menuManager, WidgetAdapter rootAdapter, List<Component> selected) {
		if(selected.size()==1){
			Component context_comp = selected.get(0);
			WidgetAdapter contextAdapter=WidgetAdapter.getWidgetAdapter(context_comp);
			if(contextAdapter!=null)
				contextAdapter.fillContextAction(menuManager);
		}
	}
}
