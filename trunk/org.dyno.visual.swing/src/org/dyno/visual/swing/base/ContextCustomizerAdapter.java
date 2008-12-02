package org.dyno.visual.swing.base;

import java.awt.Component;
import java.awt.Graphics;
import java.util.List;

import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.IContextCustomizer;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.MenuManager;

public class ContextCustomizerAdapter implements IContextCustomizer {

	@Override
	public void fillContextMenu(MenuManager menuManager,
			WidgetAdapter rootAdapter, List<Component> selected) {
	}

	@Override
	public void fillIAdapterMenu(MenuManager manager,
			WidgetAdapter rootAdapter, List<IAdapter> iadapters) {
	}

	@Override
	public void fillInvisibleAdapterMenu(MenuManager menuManager,
			WidgetAdapter rootAdapter, List<InvisibleAdapter> selected) {
	}

	@Override
	public void fillInvisibleRootMenu(MenuManager menuManager,
			WidgetAdapter rootAdapter) {
	}

	@Override
	public void paintContext(Graphics g, WidgetAdapter rootAdapter) {
	}

}
