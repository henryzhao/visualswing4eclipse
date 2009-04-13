package org.dyno.visual.swing.widgets.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.IConstants;
import org.dyno.visual.swing.plugin.spi.IDesignOperation;
import org.dyno.visual.swing.plugin.spi.IPainter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class WidgetPainter implements IPainter, IAdaptableContext, IConstants {
	private static Icon FORBIDDEN_ICON;
	static {
		FORBIDDEN_ICON = new ImageIcon(WidgetAdapter.class
				.getResource("/icons/forbidden.png")); //$NON-NLS-1$
	}	
	protected WidgetAdapter adaptable;
	
	public void paintAnchor(Graphics g) {
	}

	
	public void paintGrid(Graphics clipg) {
	}
	protected IDesignOperation getOperation(){
		return (IDesignOperation) adaptable.getAdapter(IDesignOperation.class);
	}
	protected void paintForbiddenMascot(Graphics g) {
		Point p = adaptable.getMascotLocation();
		FORBIDDEN_ICON.paintIcon(adaptable.getWidget(), g, p.x - 16, p.y - 16);
	}
	
	public void paintHint(Graphics g) {
	}

	
	public void paintHovered(Graphics clipg) {
	}

	public void paintMascot(Graphics g) {
		if (adaptable.getWidget() instanceof JComponent)
			paintComponent(g);
	}

	protected void paintComponent(Graphics g) {
		JComponent root =(JComponent) adaptable.getParentContainer();
		int w = root.getWidth();
		int h = root.getHeight();
		ArrayList<Component> comps = new ArrayList<Component>();
		unsetDB(comps, root);
		Graphics clipg = g.create(1, 1, w, h);
		root.paint(clipg);
		clipg.dispose();
		setDB(comps);
		Color old = g.getColor();
		g.setColor(SELECTION_COLOR);
		g.drawRect(0, 0, w + 1, h + 1);
		g.setColor(old);
	}

	private void setDB(ArrayList<Component> db) {
		for (Component comp : db) {
			if (comp instanceof JComponent) {
				((JComponent) comp).setDoubleBuffered(true);
			}
		}
	}

	private void unsetDB(ArrayList<Component> db, Container container) {
		if (container instanceof JComponent && container.isDoubleBuffered()) {
			((JComponent) container).setDoubleBuffered(false);
			db.add(container);
		}
		int count = container.getComponentCount();
		for (int i = 0; i < count; i++) {
			Component component = container.getComponent(i);
			if (component instanceof Container)
				unsetDB(db, (Container) component);
		}
	} 
	
	
	public void setAdaptable(IAdaptable adaptable) {
		this.adaptable = (WidgetAdapter) adaptable;
	}
}
