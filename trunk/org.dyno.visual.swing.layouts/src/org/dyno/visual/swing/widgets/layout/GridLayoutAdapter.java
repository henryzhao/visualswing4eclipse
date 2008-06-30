package org.dyno.visual.swing.widgets.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.ILayoutBean;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetProperty;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class GridLayoutAdapter extends LayoutAdapter implements ILayoutBean {
	private Rectangle placement;
	private int index;

	@Override
	public boolean dragExit(Point p) {
		placement = null;
		return true;
	}

	private boolean drag(Point p) {
		Insets insets = container.getInsets();
		int width = container.getWidth();
		int height = container.getHeight();
		WidgetAdapter parent = WidgetAdapter.getWidgetAdapter(container);
		if (container.getComponentCount() == 0) {
			placement = new Rectangle(insets.left, insets.top, width - insets.left - insets.right, height - insets.top - insets.bottom);
			index = -1;
		} else {
			Component comp = container.getComponent(0);
			int w = comp.getWidth();
			int h = comp.getHeight();
			int n = width / w;
			int xi = p.x / w;
			int yi = p.y / h;
			int i = yi * n + xi;
			int count = container.getComponentCount();
			if (i >= count) {
				yi = count / n;
				xi = count % n;
				index = -1;
			} else {
				index = i;
			}
			int x = xi * w - THUMB_PAD / 2;
			int y = yi * h + THUMB_PAD / 2;
			int thumb_width = THUMB_PAD;
			int thumb_height = h - THUMB_PAD;
			placement = new Rectangle(x + insets.left, y + insets.top, thumb_width, thumb_height);
		}
		parent.setMascotLocation(p);
		return true;
	}

	@Override
	public boolean dragEnter(Point p) {
		return drag(p);
	}

	@Override
	public boolean dragOver(Point p) {
		return drag(p);
	}

	@Override
	public boolean drop(Point p) {
		drag(p);
		WidgetAdapter parent = WidgetAdapter.getWidgetAdapter(container);
		WidgetAdapter todrop = parent.getDropWidget();
		if (index != -1)
			container.add(todrop.getComponent(), index);
		else
			container.add(todrop.getComponent());
		parent.getRootAdapter().getWidget().validate();
		parent.clearAllSelected();
		todrop.setSelected(true);
		placement = null;
		return true;
	}

	@Override
	public void paintFocused(Graphics g) {
		if (placement != null) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(STROKE);
			g2d.setColor(RED_COLOR);
			g2d.drawRect(placement.x, placement.y, placement.width, placement.height);
		}
	}

	@Override
	public void initConainerLayout(Container container) {
		GridLayout layout = new GridLayout(2, 2);
		container.setLayout(layout);
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
			panel.add(cAdapter.cloneWidget());
		}
		return true;
	}

	@Override
	protected LayoutManager copyLayout(Container con) {
		GridLayout layout = (GridLayout) container.getLayout();
		GridLayout copy = new GridLayout(layout.getRows(), layout.getColumns());
		copy.setHgap(layout.getHgap());
		copy.setVgap(layout.getVgap());
		return copy;
	}

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		GridLayout layout = (GridLayout) container.getLayout();
		int rows = layout.getRows();
		int columns = layout.getColumns();
		int hgap = layout.getHgap();
		int vgap = layout.getVgap();
		String layoutName = imports.addImport("java.awt.GridLayout");
		if (hgap != 0 || vgap != 0) {
			return "new "+layoutName+"(" + rows + ", " + columns + ", " + hgap + ", " + vgap + ")";
		} else {
			if (columns != 0 || rows != 1) {
				return "new "+layoutName+"(" + rows + ", " + columns + ")";
			} else {
				return "new "+layoutName+"()";
			}
		}
	}

	@Override
	protected IWidgetPropertyDescriptor[] getLayoutProperties() {
		WidgetProperty rowsProperty = new WidgetProperty("rows", "rows", GridLayout.class);
		WidgetProperty columnsProperty = new WidgetProperty("columns", "columns", GridLayout.class);
		WidgetProperty hgapProperty = new WidgetProperty("hgap","hgap", GridLayout.class);
		WidgetProperty vgapProperty = new WidgetProperty("vgap", "vgap", GridLayout.class);
		return new IWidgetPropertyDescriptor[]{rowsProperty, columnsProperty, hgapProperty, vgapProperty};
	}
	
}
