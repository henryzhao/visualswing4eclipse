package org.dyno.visual.swing.widgets;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.Editor;
import org.dyno.visual.swing.plugin.spi.ExtensionRegistry;

public class JInternalFrameAdapter extends CompositeAdapter {
	private static int VAR_INDEX = 0;
	private static JDesktopPane desktopPane = new JDesktopPane();
	private JPanel contentPane;
	private CompositeAdapter contentAdapter;

	public JInternalFrameAdapter() {
		super("jInternalFrame" + (VAR_INDEX++));
	}

	@Override
	public JComponent cloneWidget() {
		JInternalFrame copy = (JInternalFrame) super.cloneWidget();
		CompositeAdapter content = getContentAdapter();
		copy.setContentPane(content.cloneWidget());
		return copy;
	}

	private JPanel getContentPane() {
		JInternalFrame jif = (JInternalFrame) getWidget();
		contentPane = (JPanel) jif.getContentPane();
		return contentPane;
	}

	private CompositeAdapter getContentAdapter() {
		if (contentAdapter == null) {
			contentAdapter = (CompositeAdapter) ExtensionRegistry.createWidgetAdapter(JPanel.class);
			((JPanelAdapter)contentAdapter).setIntermediate(true);
		}
		JInternalFrame jif = (JInternalFrame) getWidget();
		contentPane = (JPanel) jif.getContentPane();
		contentAdapter.setWidget(contentPane);
		return contentAdapter;
	}

	@Override
	protected JComponent createWidget() {
		JInternalFrame jif = new JInternalFrame();
		Dimension size = new Dimension(100, 100);
		jif.setSize(size);
		layoutContainer(jif);
		jif.validate();
		jif.addNotify();
		desktopPane.add(jif);
		jif.setVisible(true);
		return jif;
	}

	@Override
	public boolean widgetPressed(int x, int y) {
		JInternalFrame jif = (JInternalFrame) getWidget();
		jif.toFront();
		return true;
	}

	private LabelEditor editor;

	@Override
	public Editor getEditorAt(int x, int y) {
		if (getCaptionBounds().contains(x, y)) {
			if (editor == null)
				editor = new LabelEditor();
			return editor;
		}
		return null;
	}

	private Rectangle getCaptionBounds() {
		JInternalFrame jif = (JInternalFrame) getWidget();
		int w = jif.getWidth();
		return new Rectangle(0, 0, w, 30);
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		if (getCaptionBounds().contains(x, y)) {
			return new Rectangle(4, 4, getWidget().getWidth() - 8, 23);
		} else
			return null;
	}

	@Override
	public Object getWidgetValue() {
		JInternalFrame jif = (JInternalFrame) getWidget();
		return jif.getTitle();
	}

	@Override
	public void setWidgetValue(Object value) {
		JInternalFrame jif = (JInternalFrame) getWidget();
		jif.setTitle((String) value);
	}

	@Override
	public JComponent getChild(int index) {
		return getContentAdapter().getChild(index);
	}

	@Override
	public int getChildCount() {
		return getContentAdapter().getChildCount();
	}

	@Override
	public int getIndexOfChild(JComponent child) {
		return getContentAdapter().getIndexOfChild(child);
	}

	private boolean inContent;

	@Override
	public boolean dragEnter(Point p) {
		if (isInContentPane(p)) {
			if (!inContent) {
				inContent = true;
				Point cp = SwingUtilities.convertPoint(getWidget(), p, getContentPane());
				return getContentAdapter().dragEnter(cp);
			} else
				return true;
		} else {
			if (inContent) {
				inContent = false;
				Point cp = SwingUtilities.convertPoint(getWidget(), p, getContentPane());
				return getContentAdapter().dragExit(cp);
			} else {
				setMascotLocation(p);
				return true;
			}
		}
	}

	private boolean isInContentPane(Point p) {
		return getContentBounds().contains(p);
	}

	private Rectangle getContentBounds() {
		JPanel jpanel = getContentPane();
		int w = jpanel.getWidth();
		int h = jpanel.getHeight();
		Rectangle bounds = new Rectangle(0, 0, w, h);
		return SwingUtilities.convertRectangle(jpanel, bounds, getWidget());
	}

	@Override
	public boolean dragExit(Point p) {
		if (isInContentPane(p)) {
			if (inContent) {
				inContent = false;
				Point cp = SwingUtilities.convertPoint(getWidget(), p, getContentPane());
				return getContentAdapter().dragExit(cp);
			} else
				return true;
		} else {
			if (inContent) {
				inContent = false;
				Point cp = SwingUtilities.convertPoint(getWidget(), p, getContentPane());
				return getContentAdapter().dragExit(cp);
			} else {
				setMascotLocation(p);
				return true;
			}
		}
	}

	@Override
	public boolean dragOver(Point p) {
		if (isInContentPane(p)) {
			if (!inContent) {
				inContent = true;
				Point cp = SwingUtilities.convertPoint(getWidget(), p, getContentPane());
				return getContentAdapter().dragEnter(cp);
			} else {
				Point cp = SwingUtilities.convertPoint(getWidget(), p, getContentPane());
				return getContentAdapter().dragOver(cp);
			}
		} else {
			if (inContent) {
				inContent = false;
				Point cp = SwingUtilities.convertPoint(getWidget(), p, getContentPane());
				return getContentAdapter().dragExit(cp);
			} else {
				setMascotLocation(p);
				return true;
			}
		}
	}

	@Override
	public void paintFocused(Graphics clipg) {
		if (inContent) {
			Rectangle rect = getContentBounds();
			Graphics g = clipg.create(rect.x, rect.y, rect.width, rect.height);
			getContentAdapter().paintFocused(g);
			g.dispose();
		}
	}

	@Override
	public boolean drop(Point p) {
		if (isInContentPane(p)) {
			inContent = false;
			Point cp = SwingUtilities.convertPoint(getWidget(), p, getContentPane());
			return getContentAdapter().drop(cp);
		} else {
			inContent = false;
			Toolkit.getDefaultToolkit().beep();
			return true;
		}
	}

}
