/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.base.NamespaceManager;
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
	public boolean removeChild(Component child) {
		if (child instanceof JMenuBar) {
			JInternalFrame jframe = (JInternalFrame) getWidget();
			jframe.setJMenuBar(null);
			return true;
		} else
			return getContentAdapter().removeChild(child);
	}	
	@Override
	public void clearSelection() {
		setSelected(false);
		JInternalFrame jframe = (JInternalFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb != null) {
			WidgetAdapter jmbAdapter = WidgetAdapter.getWidgetAdapter(jmb);
			jmbAdapter.clearSelection();
		}
		getContentAdapter().clearSelection();
	}
	@Override
	public Component cloneWidget() {
		JInternalFrame copy = (JInternalFrame) super.cloneWidget();
		JInternalFrame jframe = (JInternalFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb != null) {
			WidgetAdapter jmbAdapter = WidgetAdapter.getWidgetAdapter(jmb);
			JMenuBar copyjmb=(JMenuBar) jmbAdapter.cloneWidget();
			copy.setJMenuBar(copyjmb);
		}
		CompositeAdapter content = getContentAdapter();
		copy.setContentPane((JComponent) content.cloneWidget());
		return copy;
	}

	@Override
	protected String createInitCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createInitCode(imports));
		((JPanelAdapter)getContentAdapter()).createAddCode(imports, builder);
		JInternalFrame jframe = (JInternalFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb != null) {
			builder.append("setJMenuBar(");
			WidgetAdapter jmbAdapter = WidgetAdapter.getWidgetAdapter(jmb);
			String getName=NamespaceManager.getInstance().getGetMethodName(jmbAdapter.getName());
			builder.append(getName+"()");
			builder.append(");\n");
		}
		return builder.toString();
	}

	@Override
	protected String createGetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createGetCode(imports));
		JPanel panel = getContentPane();
		JPanelAdapter adapter = (JPanelAdapter) ExtensionRegistry
				.createWidgetAdapter(panel);
		adapter.setName(getName());
		adapter.genAddComponentCode(imports, builder);
		adapter.detachWidget();
		return builder.toString();
	}

	protected boolean createConstructor(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		IMethod cons = type.getMethod(type.getElementName(), new String[0]);
		if (!cons.exists()) {
			StringBuilder builder = new StringBuilder();
			builder.append("public " + type.getElementName() + "(){\n");
			builder.append("initComponent();\n");
			builder.append("}\n");
			try {
				type.createMethod(JavaUtil.formatCode(builder.toString()),
						null, false, null);
			} catch (JavaModelException e) {
				WidgetPlugin.getLogger().error(e);
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
			contentAdapter = (CompositeAdapter) ExtensionRegistry
					.createWidgetAdapter(JPanel.class);
			((JPanelAdapter) contentAdapter).setIntermediate(true);
			JInternalFrame jif = (JInternalFrame) getWidget();
			contentPane = (JPanel) jif.getContentPane();
			contentAdapter.setWidget(contentPane);
		}
		return contentAdapter;
	}

	@Override
	public boolean isEnclosingContainer() {
		return true;
	}

	@Override
	public boolean interceptPoint(Point p, int ad) {
		JInternalFrame comp = (JInternalFrame) getWidget();
		return p.x >= -ad
				&& p.y >= -ad
				&& p.x < comp.getWidth() + ad
				&& p.y < comp.getHeight() + ad
				&& !(p.x >= ad && p.y >= ad + TITLE_HEIGHT
						&& p.x < comp.getWidth() - ad && p.y < comp.getHeight()
						- ad);
	}

	private static int TITLE_HEIGHT = 22;

	@Override
	protected Component createWidget() {
		JInternalFrame jif = new JInternalFrame();
		Dimension size = new Dimension(100, 100);
		WidgetAdapter contentAdapter = ExtensionRegistry
				.createWidgetAdapter(JPanel.class);
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
		JInternalFrame jif = (JInternalFrame) getWidget();
		JMenuBar jmb = jif.getJMenuBar();
		if (jmb == null)
			return getContentAdapter().getChild(index);
		else if (index == 0)
			return jmb;
		else
			return getContentAdapter().getChild(index - 1);
	}

	@Override
	public int getChildCount() {
		JInternalFrame jif = (JInternalFrame) getWidget();
		JMenuBar jmb=jif.getJMenuBar();		
		int count = getContentAdapter().getChildCount();
		return jmb==null?count:count+1;
	}

	@Override
	public int getIndexOfChild(Component child) {
		JInternalFrame jif = (JInternalFrame) getWidget();
		JMenuBar jmb = jif.getJMenuBar();
		if (jmb == null)
			return getContentAdapter().getIndexOfChild(child);
		else if (child == jmb)
			return 0;
		else
			return getContentAdapter().getIndexOfChild(child) + 1;
	}

	private boolean inContent;

	@Override
	public boolean dragEnter(Point p) {
		if (!isDroppingMenuBar() && !isDroppingMenuItem() && isInContentPane(p)) {
			inContent = true;
			Point cp = SwingUtilities.convertPoint(getWidget(), p,
					getContentPane());
			return getContentAdapter().dragEnter(cp);
		}
		setMascotLocation(p);
		return true;
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
		if (inContent) {
			inContent = false;
			Point cp = SwingUtilities.convertPoint(getWidget(), p,
					getContentPane());
			return getContentAdapter().dragExit(cp);
		} else{
			setMascotLocation(p);
			return true;
		}
	}

	@Override
	public boolean dragOver(Point p) {
		if (!isDroppingMenuBar() && !isDroppingMenuItem()) {
			if (isInContentPane(p)) {
				if (!inContent) {
					inContent = true;
					Point cp = SwingUtilities.convertPoint(getWidget(), p,
							getContentPane());
					return getContentAdapter().dragEnter(cp);
				} else {
					Point cp = SwingUtilities.convertPoint(getWidget(), p,
							getContentPane());
					return getContentAdapter().dragOver(cp);
				}
			} else {
				if (inContent) {
					inContent = false;
					Point cp = SwingUtilities.convertPoint(getWidget(), p,
							getContentPane());
					return getContentAdapter().dragExit(cp);
				} else {
					setMascotLocation(p);
					return true;
				}
			}
		}
		setMascotLocation(p);
		return true;
	}

	@Override
	public void paintFocused(Graphics g) {
		 if (isDroppingMenuBar()) {
			JInternalFrame jif = (JInternalFrame) getWidget();
			if (jif.getJMenuBar() == null) {
				Rectangle rect = getContentBounds();
				Graphics clipg = g.create(rect.x, rect.y, rect.width,
						rect.height);
				clipg.setColor(Color.GREEN);
				int h = getDropWidget().get(0).getWidget().getHeight();
				clipg.drawRect(0, 0, rect.width, h);
				clipg.dispose();
			} 
		}
	}

	@Override
	public void paintBaselineAnchor(Graphics g) {
		if (!isDroppingMenuBar() && !isDroppingMenuItem()) {
			if (inContent) {
				Rectangle rect = getContentBounds();
				Graphics clipg = g.create(rect.x, rect.y, rect.width,
						rect.height);
				getContentAdapter().paintBaselineAnchor(clipg);
				clipg.dispose();
			}
		}else if(isDroppingMenuItem()){
			paintForbiddenMascot(g);
		}else if(isDroppingMenuBar()){
			JInternalFrame jif = (JInternalFrame) getWidget();
			if (jif.getJMenuBar() != null) {
				paintForbiddenMascot(g);
			}
		}
	}

	@Override
	public boolean drop(Point p) {
		if (!isDroppingMenuBar() && !isDroppingMenuItem()) {
		inContent = false;
			if (isInContentPane(p)) {
				Point cp = SwingUtilities.convertPoint(getWidget(), p,
						getContentPane());
				return getContentAdapter().drop(cp);
			} else {
				Toolkit.getDefaultToolkit().beep();
				return true;
			}
		}else if (isDroppingMenuBar()) {
			JInternalFrame jif = (JInternalFrame) getWidget();
			if(jif.getJMenuBar()==null){
				WidgetAdapter jmenuBarAdapter=getDropWidget().get(0);
				JMenuBar jmb=(JMenuBar)jmenuBarAdapter.getWidget();
				jif.setJMenuBar(jmb);
				clearAllSelected();
				jmenuBarAdapter.setSelected(true);
				jmenuBarAdapter.addNotify();
			}else{
				Toolkit.getDefaultToolkit().beep();
				return false;
			}
		}
		setMascotLocation(p);
		return true;
	}

	@Override
	protected Component newWidget() {
		return new JInternalFrame();
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		if(child instanceof JMenuBar){
			JInternalFrame jif = (JInternalFrame) getWidget();
			jif.setJMenuBar((JMenuBar)child);
		}else
			getContentAdapter().addChildByConstraints(child, constraints);
	}

	@Override
	public Object getChildConstraints(Component child) {
		if (child instanceof JMenuBar)
			return null;		
		return getContentAdapter().getChildConstraints(child);
	}

	@Override
	public Class getWidgetClass() {
		return JInternalFrame.class;
	}

}
