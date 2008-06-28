package org.dyno.visual.swing.widgets;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.dyno.visual.swing.plugin.spi.Azimuth;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JDesktopPaneAdapter extends CompositeAdapter {
	protected static Color RED_COLOR = new Color(255, 164, 0);
	protected static Color GREEN_COLOR = new Color(164, 255, 0);
	protected static Stroke STROKE;
	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 2 }, 0);
	}
	private static int VAR_INDEX = 0;

	public JDesktopPaneAdapter() {
		super("jDesktopPane" + (VAR_INDEX++));
	}

	public boolean needGenBoundCode() {
		return true;
	}

	@Override
	protected JComponent createWidget() {
		JDesktopPane pane = new JDesktopPane();
		Dimension size = new Dimension(100, 100);
		pane.setSize(size);
		return pane;
	}

	@Override
	public boolean allowChildResize() {
		return true;
	}

	@Override
	protected String createGetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createGetCode(imports));
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			JComponent child = getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			String getMethodName = getGetMethodName(childAdapter.getName());
			builder.append(getFieldName(getName()) + "." + "add(" + getMethodName + "());\n");
		}
		return builder.toString();
	}

	@Override
	public JComponent getChild(int index) {
		JDesktopPane jdp = (JDesktopPane) getWidget();
		JInternalFrame[] frames = jdp.getAllFrames();
		return frames[index];
	}

	@Override
	public int getChildCount() {
		JDesktopPane desktopPane = (JDesktopPane) getWidget();
		JInternalFrame[] frames = desktopPane.getAllFrames();
		int count = frames == null ? 0 : frames.length;
		return count;
	}

	@Override
	public int getIndexOfChild(JComponent child) {
		JDesktopPane desktopPane = (JDesktopPane) getWidget();
		JInternalFrame[] frames = desktopPane.getAllFrames();
		for (int i = 0; i < frames.length; i++) {
			if (frames[i] == child)
				return i;
		}
		return -1;
	}

	@Override
	public JComponent cloneWidget() {
		JDesktopPane copy = (JDesktopPane) super.cloneWidget();
		JDesktopPane pane = (JDesktopPane) getWidget();
		for (JInternalFrame frame : pane.getAllFrames()) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(frame);
			JInternalFrame copy_frame = (JInternalFrame) adapter.cloneWidget();
			copy.add(copy_frame);
			copy_frame.setVisible(true);
		}
		return copy;
	}

	@Override
	public boolean isChildMoveable() {
		return true;
	}

	@Override
	protected boolean isChildVisible(JComponent child) {
		int i = getIndexOfChild(child);
		return i == 0;
	}

	@Override
	public void showChild(JComponent widget) {
		JInternalFrame jif = (JInternalFrame) widget;
		jif.toFront();
	}

	private boolean forbid;

	@Override
	public boolean dragEnter(Point p) {
		JComponent comp = getDropWidget().getWidget();
		forbid = !(comp instanceof JInternalFrame);
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		forbid = false;
		return true;
	}

	@Override
	public void paintFocused(Graphics clipg) {
		if (forbid) {
			int w = getWidget().getWidth();
			int h = getWidget().getHeight();
			Graphics2D g2d = (Graphics2D) clipg;
			g2d.setColor(RED_COLOR);
			Composite oldComposite = g2d.getComposite();
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
			g2d.setComposite(composite);
			g2d.fillRect(0, 0, w, h);
			Stroke oldStroke = g2d.getStroke();
			g2d.setColor(GREEN_COLOR);
			g2d.setStroke(STROKE);
			g2d.drawRect(0, 0, w, h);
			g2d.setStroke(oldStroke);
			g2d.setComposite(oldComposite);
		}
	}

	@Override
	public boolean dragOver(Point p) {
		int state = getState();
		if (state == Azimuth.STATE_BEAN_HOVER) {
			setMascotLocation(p);
		} else {
			resize_widget(p);
		}
		return true;
	}

	private void resize_widget(Point p) {
		int state = getState();
		Dimension min = new Dimension(10, 10);
		Dimension size = getDropWidget().getWidget().getSize();
		Point hotspot = getMascotLocation();
		int w = min.width;
		int h = min.height;
		switch (state) {
		case Azimuth.STATE_BEAN_RESIZE_RIGHT_BOTTOM:
			w = p.x - hotspot.x;
			h = p.y - hotspot.y;
			break;
		case Azimuth.STATE_BEAN_RESIZE_BOTTOM:
			w = size.width;
			h = p.y - hotspot.y;
			break;
		case Azimuth.STATE_BEAN_RESIZE_LEFT_BOTTOM:
			w = size.width + hotspot.x - p.x;
			h = p.y - hotspot.y;
			hotspot.x = p.x;
			break;
		case Azimuth.STATE_BEAN_RESIZE_LEFT:
			w = size.width + hotspot.x - p.x;
			h = size.height;
			hotspot.x = p.x;
			break;
		case Azimuth.STATE_BEAN_RESIZE_LEFT_TOP:
			w = size.width + hotspot.x - p.x;
			h = size.height + hotspot.y - p.y;
			hotspot.x = p.x;
			hotspot.y = p.y;
			break;
		case Azimuth.STATE_BEAN_RESIZE_TOP:
			w = size.width;
			h = size.height + hotspot.y - p.y;
			hotspot.y = p.y;
			break;
		case Azimuth.STATE_BEAN_RESIZE_RIGHT_TOP:
			w = p.x - hotspot.x;
			h = size.height + hotspot.y - p.y;
			hotspot.y = p.y;
			break;
		case Azimuth.STATE_BEAN_RESIZE_RIGHT:
			w = p.x - hotspot.x;
			h = size.height;
			break;
		}
		if (w <= min.width)
			w = min.width;
		if (h <= min.height)
			h = min.height;
		setMascotLocation(hotspot);
		getDropWidget().getWidget().setSize(w, h);
		getDropWidget().doLayout();
	}

	@Override
	public boolean drop(Point p) {
		if (!forbid) {
			JDesktopPane jtp = (JDesktopPane) getWidget();
			JInternalFrame jif = (JInternalFrame) getDropWidget().getWidget();
			Point htsp = getDropWidget().getHotspotPoint();
			int state = getState();
			switch (state) {
			case Azimuth.STATE_BEAN_HOVER:
				jif.setLocation(p.x - htsp.x, p.y - htsp.y);
				break;
			default:
				Point pt = getMascotLocation();
				jif.setLocation(pt.x - htsp.x, pt.y - htsp.y);
				break;
			}
			jtp.add(jif);
			jif.setVisible(true);
			clearAllSelected();
			getDropWidget().setSelected(true);
			getWidget().validate();
			jif.toFront();
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
		return true;
	}
}
