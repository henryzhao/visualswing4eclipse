package org.dyno.visual.swing.editors.actions;

import javax.swing.SwingUtilities;
import javax.swing.UIManager.LookAndFeelInfo;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.Action;

public class LnfAction extends Action {
	private LookAndFeelInfo info;
	private WidgetAdapter adapter;
	public LnfAction(WidgetAdapter adapter, LookAndFeelInfo info) {
		super(info.getName(), AS_RADIO_BUTTON);
		this.info = info;
		this.adapter = adapter;
		String lnf = adapter.getLnfClassname();
		setChecked(lnf != null && lnf.equals(info.getClassName()));
	}

	@Override
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				changeLnf();
			}
		});
	}

	private void changeLnf() {
		String lnf = adapter.getLnfClassname();
		if (lnf != null
				&& !lnf.getClass().getName().equals(info.getClassName())) {
			try {
				adapter.setLnfClassname(info.getClassName());
				SwingUtilities.updateComponentTreeUI(adapter.getDesigner());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
