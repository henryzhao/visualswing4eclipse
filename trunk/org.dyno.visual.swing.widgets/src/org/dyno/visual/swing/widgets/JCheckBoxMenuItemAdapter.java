package org.dyno.visual.swing.widgets;

import java.awt.Component;

import javax.swing.JCheckBoxMenuItem;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JCheckBoxMenuItemAdapter extends WidgetAdapter {
	private static int VAR_INDEX = 0;
	public JCheckBoxMenuItemAdapter(){
		super("jCheckBoxMenuItem" + (VAR_INDEX++));
	}

	@Override
	protected Component createWidget() {
		JCheckBoxMenuItem jmi = new JCheckBoxMenuItem();
		jmi.setText("check item");
		jmi.setSize(jmi.getPreferredSize());
		jmi.doLayout();
		return jmi;
	}

	@Override
	protected Component newWidget() {
		return new JCheckBoxMenuItem();
	}

	@Override
	public Component cloneWidget() {
		JCheckBoxMenuItem jmi = (JCheckBoxMenuItem) super.cloneWidget();
		JCheckBoxMenuItem origin = (JCheckBoxMenuItem)getWidget();
		jmi.setText(origin.getText());
		jmi.setSelected(origin.isSelected());
		return jmi;
	}
	
}
