package org.dyno.visual.swing.widgets.actions;

import javax.swing.JPanel;

import org.dyno.visual.swing.widgets.JPanelAdapter;
import org.eclipse.jface.action.Action;

public class NullLayoutAction extends Action {
	private JPanelAdapter adapter;
	public NullLayoutAction(JPanelAdapter adapter) {
		super("Null Layout", AS_RADIO_BUTTON);
		this.adapter = adapter;
	}

	public void run() {
		JPanel jpanel = (JPanel) adapter.getWidget();
		jpanel.setLayout(null);
		adapter.setLayoutAdapter(null);
		adapter.doLayout();
		jpanel.validate();
	}
}
