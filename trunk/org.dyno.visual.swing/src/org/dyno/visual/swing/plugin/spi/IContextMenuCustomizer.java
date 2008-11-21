package org.dyno.visual.swing.plugin.spi;

import java.awt.Component;
import java.util.List;

import org.eclipse.jface.action.MenuManager;

public interface IContextMenuCustomizer {
	void fillContextMenu(MenuManager menuManager, WidgetAdapter rootAdapter, List<Component>selected);
	void fillInvisibleRootMenu(MenuManager menuManager, WidgetAdapter rootAdapter);
	void fillInvisibleAdapterMenu(MenuManager menuManager, WidgetAdapter rootAdapter, List<IAdapter> selected);
}
