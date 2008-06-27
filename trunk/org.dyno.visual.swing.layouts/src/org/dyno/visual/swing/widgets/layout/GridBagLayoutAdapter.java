package org.dyno.visual.swing.widgets.layout;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.LayoutBean;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class GridBagLayoutAdapter extends LayoutAdapter implements LayoutBean {

	@Override
	public void initConainerLayout(Container container) {
		container.setLayout(new GridBagLayout());
	}

	@Override
	public boolean dragEnter(Point p) {
		return super.dragEnter(p);
	}

	@Override
	public boolean dragExit(Point p) {
		return super.dragExit(p);
	}

	@Override
	public boolean dragOver(Point p) {
		return super.dragOver(p);
	}

	@Override
	public boolean drop(Point p) {
		WidgetAdapter parent = WidgetAdapter.getWidgetAdapter(container);
		WidgetAdapter todrop = parent.getDropWidget();
		container.add(todrop.getComponent(), new GridBagConstraints());
		parent.getRootAdapter().getWidget().validate();
		parent.clearAllSelected();
		todrop.setSelected(true);
		return super.drop(p);
	}

	@Override
	public boolean canAcceptMoreComponent() {
		return true;
	}

	@Override
	public void cloneLayout(JPanel panel) {
		GridBagLayout layout = (GridBagLayout) container.getLayout();
		panel.setLayout(copyLayout(panel));
		int count = container.getComponentCount();
		for (int i = 0; i < count; i++) {
			JComponent child = (JComponent) container.getComponent(i);
			WidgetAdapter cAdapter = WidgetAdapter.getWidgetAdapter(child);
			panel.add(cAdapter.cloneWidget(), layout.getConstraints(child));
		}
	}

	@Override
	protected LayoutManager copyLayout(Container con) {
		GridBagLayout copy = new GridBagLayout();
		return copy;
	}

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		String layoutName = imports.addImport("java.awt.GridBagLayout");
		return "new " + layoutName + "()";
	}
}
