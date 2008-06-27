package org.dyno.visual.swing.widgets;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.dyno.visual.swing.plugin.spi.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public abstract class ComplexWidgetAdapter extends WidgetAdapter {

	public ComplexWidgetAdapter() {
		super();
	}

	public ComplexWidgetAdapter(String name) {
		super(name);
	}

	@Override
	public JComponent getComponent() {
		if (parent == null) {
			WidgetAdapter jspa = ExtensionRegistry.createWidgetAdapter(JScrollPane.class);
			parent = ((JScrollPane) jspa.getWidget());
			parent.setViewportView(getWidget());
			parent.addNotify();
			parent.setSize(getWidget().getSize());
			layoutContainer(parent);
			parent.validate();
		}
		return parent;
	}

	private JScrollPane parent;

	@Override
	public void paintMascot(Graphics g) {
		paintComponent(g, getComponent());
	}

}
