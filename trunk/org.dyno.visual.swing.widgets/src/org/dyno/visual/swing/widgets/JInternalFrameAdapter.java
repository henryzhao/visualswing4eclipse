/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JInternalFrameAdapter extends CompositeAdapter {
	private static int VAR_INDEX = 0;
	private static JDesktopPane desktopPane = new JDesktopPane();
	private JPanel contentPane;
	private CompositeAdapter contentAdapter;

	public JInternalFrameAdapter() {
		super("jInternalFrame" + (VAR_INDEX++));
	}

	@Override
	public Component getRootPane() {
		return getWidget();
	}
	public void doLayout() {
		CompositeAdapter content = getContentAdapter();
		content.doLayout();
	}

	@Override
	public Component cloneWidget() {
		JInternalFrame copy = (JInternalFrame) super.cloneWidget();
		CompositeAdapter content = getContentAdapter();
		copy.setContentPane((JComponent)content.cloneWidget());
		return copy;
	}
	@Override
	protected String createInitCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createInitCode(imports));
		JPanel panel = getContentPane();
		JPanelAdapter adapter = (JPanelAdapter) ExtensionRegistry.createWidgetAdapter(panel);
		adapter.setName(getName());
		adapter.createAddCode(imports, builder);
		adapter.detachWidget();
		return builder.toString();
	}
	
	@Override
	protected String createGetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createGetCode(imports));
		JPanel panel = getContentPane();
		JPanelAdapter adapter = (JPanelAdapter) ExtensionRegistry.createWidgetAdapter(panel);
		adapter.setName(getName());
		adapter.genAddComponentCode(imports, builder);
		adapter.detachWidget();
		return builder.toString();
	}
	protected boolean createConstructor(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		IMethod cons = type.getMethod(type.getElementName(), new String[0]);
		if(!cons.exists()){
			StringBuilder builder = new StringBuilder();
			builder.append("public "+type.getElementName()+"(){\n");
			builder.append("initComponent();\n");
			builder.append("}\n");
			try {
				type.createMethod(JavaUtil.formatCode(builder.toString()), null, false, null);
			} catch (JavaModelException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}	


	@Override
	public boolean needGenBoundCode() {
		JPanel panel = getContentPane();
		LayoutManager layout = panel.getLayout();
		return layout == null;
	}

	private JPanel getContentPane() {
		JInternalFrame jif = (JInternalFrame) getWidget();
		contentPane = (JPanel) jif.getContentPane();
		return contentPane;
	}

	private CompositeAdapter getContentAdapter() {
		if (contentAdapter == null) {
			contentAdapter = (CompositeAdapter) ExtensionRegistry.createWidgetAdapter(JPanel.class);
			((JPanelAdapter) contentAdapter).setIntermediate(true);
		}
		JInternalFrame jif = (JInternalFrame) getWidget();
		contentPane = (JPanel) jif.getContentPane();
		contentAdapter.setWidget(contentPane);
		return contentAdapter;
	}

	@Override
	public boolean isEnclosingContainer() {
		return true;
	}

	@Override
	public boolean interceptPoint(Point p, int ad) {
		JInternalFrame comp = (JInternalFrame) getWidget();
		return p.x >= -ad && p.y >= -ad && p.x < comp.getWidth() + ad && p.y < comp.getHeight() + ad
				&& !(p.x >= ad && p.y >= ad + TITLE_HEIGHT && p.x < comp.getWidth() - ad && p.y < comp.getHeight() - ad);
	}
	private static int TITLE_HEIGHT = 22;
	@Override
	protected Component createWidget() {
		JInternalFrame jif = new JInternalFrame();
		Dimension size = new Dimension(100, 100);
		WidgetAdapter contentAdapter = ExtensionRegistry.createWidgetAdapter(JPanel.class);
		jif.add(contentAdapter.getWidget(), BorderLayout.CENTER);
		jif.setSize(size);
		layoutContainer(jif);
		jif.validate();
		jif.addNotify();
		desktopPane.add(jif);
		jif.setVisible(true);
		return jif;
	}

	@Override
	public boolean widgetPressed(MouseEvent e) {
		JInternalFrame jif = (JInternalFrame) getWidget();
		jif.toFront();
		return true;
	}

	private LabelEditor editor;

	@Override
	public IEditor getEditorAt(int x, int y) {
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
	public Component getChild(int index) {
		return getContentAdapter().getChild(index);
	}

	@Override
	public int getChildCount() {
		return getContentAdapter().getChildCount();
	}

	@Override
	public int getIndexOfChild(Component child) {
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

	@Override
	protected Component newWidget() {
		return new JInternalFrame();
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		getContentAdapter().addChildByConstraints(child, constraints);
	}

	@Override
	public Object getChildConstraints(Component child) {
		return getContentAdapter().getChildConstraints(child);
	}

}
