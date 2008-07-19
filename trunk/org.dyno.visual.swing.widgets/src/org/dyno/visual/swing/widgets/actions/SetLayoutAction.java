package org.dyno.visual.swing.widgets.actions;

import javax.swing.JPanel;

import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.widgets.JPanelAdapter;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.Action;

public class SetLayoutAction extends Action {
	private IConfigurationElement config;
	private JPanelAdapter jpaneladapter;
	public SetLayoutAction(JPanelAdapter adapter, IConfigurationElement config) {
		super(config.getAttribute("name"), AS_RADIO_BUTTON);
		this.config = config;
		this.jpaneladapter = adapter;
	}

	public void run() {
		JPanel jpanel = (JPanel) jpaneladapter.getWidget();
		LayoutAdapter adapter = LayoutAdapter.createLayoutAdapter(config);
		adapter.initConainerLayout(jpanel);
		jpaneladapter.setLayoutAdapter(null);
		jpaneladapter.doLayout();
		jpanel.validate();
		jpaneladapter.changeNotify();
	}
}
