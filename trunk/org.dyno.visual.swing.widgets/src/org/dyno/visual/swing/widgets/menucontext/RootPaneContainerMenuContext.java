package org.dyno.visual.swing.widgets.menucontext;

import java.awt.Component;

import javax.swing.JMenuBar;

import org.dyno.visual.swing.base.CompositeMenuContext;
import org.dyno.visual.swing.plugin.spi.IMenuContext;
import org.dyno.visual.swing.plugin.spi.RootPaneContainerAdapter;
import org.dyno.visual.swing.widgets.JPanelAdapter;
import org.eclipse.jface.action.MenuManager;

public class RootPaneContainerMenuContext extends CompositeMenuContext {

	@Override
	public void fillContextAction(MenuManager menu) {
		super.fillContextAction(menu);
		JPanelAdapter contentAdapter = (JPanelAdapter) ((RootPaneContainerAdapter) adaptable)
				.getContentAdapter();
		if (contentAdapter != null) {
			JPanelMenuContext context = (JPanelMenuContext) contentAdapter
					.getAdapter(IMenuContext.class);
			context.fillSetLayoutAction(menu);
		}
	}

	@Override
	public void fillConstraintsAction(MenuManager menu, Component child) {
		if (!(child instanceof JMenuBar)) {
			JPanelAdapter contentAdapter = (JPanelAdapter) ((RootPaneContainerAdapter) adaptable)
					.getContentAdapter();
			if (contentAdapter != null) {
				CompositeMenuContext context = (CompositeMenuContext) contentAdapter
						.getAdapter(IMenuContext.class);
				context.fillConstraintsAction(menu, child);
			}
		}
	}
}
