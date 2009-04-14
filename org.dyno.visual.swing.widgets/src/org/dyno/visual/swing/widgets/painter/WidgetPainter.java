package org.dyno.visual.swing.widgets.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.IConstants;
import org.dyno.visual.swing.plugin.spi.IDesignOperation;
import org.dyno.visual.swing.plugin.spi.IPainter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class WidgetPainter implements IPainter, IAdaptableContext, IConstants {
	private static Icon FORBIDDEN_ICON;
	private static JWindow DUMMY_DIALOG;
	private static final int MAX_WIDTH = 100;
	private static final int MAX_HEIGHT = 100;
	static {
		FORBIDDEN_ICON = new ImageIcon(WidgetAdapter.class.getResource("/icons/forbidden.png")); //$NON-NLS-1$
		initializeDummyDialog();
	}

	private static synchronized void initializeDummyDialog() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				DUMMY_DIALOG = new JWindow();
				DUMMY_DIALOG.setLayout(null);
				DUMMY_DIALOG.setSize(MAX_WIDTH, MAX_HEIGHT);
				DUMMY_DIALOG.setLocation(-2 * MAX_WIDTH, -2 * MAX_HEIGHT);
				DUMMY_DIALOG.setVisible(true);
			}
		});
	}

	protected WidgetAdapter adaptable;

	public void paintAnchor(Graphics g) {
	}

	public void paintGrid(Graphics clipg) {
	}

	protected IDesignOperation getOperation() {
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
		paintComponent(g);
	}

	private int last_width;
	private int last_height;
	private Image offscreen;

	protected void paintComponent(Graphics g) {
		if (DUMMY_DIALOG != null && DUMMY_DIALOG.isVisible()) {
			Component root = adaptable.getParentContainer();
			int w = root.getWidth();
			int h = root.getHeight();
			if (offscreen == null || last_width != w || last_height != h) {
				DUMMY_DIALOG.add(root);
				offscreen = root.createImage(w, h);
				Graphics offg = offscreen.getGraphics();
				root.print(offg);
				last_width = w;
				last_height = h;
				DUMMY_DIALOG.remove(root);
			}
			g.drawImage(offscreen, 1, 1, root);
			Color old = g.getColor();
			g.setColor(SELECTION_COLOR);
			g.drawRect(0, 0, w + 1, h + 1);
			g.setColor(old);
		}
	}

	public void setAdaptable(IAdaptable adaptable) {
		this.adaptable = (WidgetAdapter) adaptable;
	}
}
