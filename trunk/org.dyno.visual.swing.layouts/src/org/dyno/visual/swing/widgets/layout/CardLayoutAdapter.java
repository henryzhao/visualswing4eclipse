package org.dyno.visual.swing.widgets.layout;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Stroke;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.ILayoutBean;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetProperty;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class CardLayoutAdapter extends LayoutAdapter implements ILayoutBean {
	private boolean hovered;

	@Override
	public void initConainerLayout(Container container) {
		container.setLayout(new CardLayout());
	}

	@Override
	public boolean dragEnter(Point p) {
		hovered = true;
		return super.dragEnter(p);
	}

	@Override
	public boolean isWidgetVisible(JComponent child) {
		return child.isVisible();
	}

	@Override
	public void showChild(JComponent widget) {
		CardLayout cardLayout = (CardLayout) container.getLayout();
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(widget);
		String name = adapter.getName();
		cardLayout.show(container, name);
	}

	@Override
	public boolean dragExit(Point p) {
		hovered = false;
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
		CardLayout layout = (CardLayout) container.getLayout();
		container.add(todrop.getComponent(), todrop.getName());
		layout.show(container, todrop.getName());
		parent.getRootAdapter().getWidget().validate();
		parent.clearAllSelected();
		todrop.setSelected(true);
		hovered = false;
		return true;
	}

	@Override
	public void paintFocused(Graphics g) {
		if (hovered) {
			Insets insets = container.getInsets();
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(GREEN_COLOR);
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(STROKE);
			g2d.setColor(RED_COLOR);
			g2d.drawRect(insets.left, insets.top, container.getWidth() - insets.left - insets.right, container.getHeight() - insets.top - insets.bottom);
			g2d.setStroke(oldStroke);
		}
	}

	@Override
	public boolean canAcceptMoreComponent() {
		return true;
	}

	@Override
	public boolean cloneLayout(JComponent panel) {
		panel.setLayout(copyLayout(panel));
		int count = container.getComponentCount();
		for (int i = 0; i < count; i++) {
			JComponent child = (JComponent) container.getComponent(i);
			WidgetAdapter cAdapter = WidgetAdapter.getWidgetAdapter(child);
			panel.add(cAdapter.cloneWidget(), cAdapter.getName());
		}
		return true;
	}

	@Override
	protected LayoutManager copyLayout(Container con) {
		CardLayout layout = (CardLayout) container.getLayout();
		CardLayout copy = new CardLayout();
		copy.setHgap(layout.getHgap());
		copy.setVgap(layout.getVgap());
		return copy;
	}

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		CardLayout layout = (CardLayout) container.getLayout();
		int hgap = layout.getHgap();
		int vgap = layout.getVgap();
		String strLayoutName= imports.addImport("java.awt.CardLayout");
		if (hgap != 0 || vgap != 0) {
			return "new "+strLayoutName+"(" + hgap + ", " + vgap + ")";
		} else {
			return "new "+strLayoutName+"()";
		}
	}

	@Override
	protected String getChildConstraints(JComponent child, ImportRewrite imports) {
		return "\""+WidgetAdapter.getWidgetAdapter(child).getName()+"\"";
	}

	@Override
	protected IWidgetPropertyDescriptor[] getLayoutProperties() {
		WidgetProperty hgapProperty = new WidgetProperty("hgap","hgap", CardLayout.class);
		WidgetProperty vgapProperty = new WidgetProperty("vgap", "vgap", CardLayout.class);
		return new IWidgetPropertyDescriptor[]{hgapProperty, vgapProperty};
	}
	
}