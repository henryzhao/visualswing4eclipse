package org.dyno.visual.swing.plugin.spi;

import java.awt.Component;
import java.util.List;

import org.eclipse.jface.action.MenuManager;

public interface IContextMenuCustomizer {
	void fillContextMenu(MenuManager menuManager, List<Component>selected);
}
