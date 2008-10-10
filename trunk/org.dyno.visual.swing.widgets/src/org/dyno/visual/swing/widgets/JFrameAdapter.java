package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.designborder.FrameBorder;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.MenuManager;

public class JFrameAdapter extends CompositeAdapter {
	private JPanelAdapter contentAdapter;
	private JComponent rootPane;
	private JRootPane jrootPane;

	public JFrameAdapter() {
		super("jFrame");
		createContentAdapter();
	}

	@Override
	public void setWidget(Component widget) {
		super.setWidget(widget);
		createContentAdapter();
	}

	private void createContentAdapter() {
		contentAdapter = (JPanelAdapter) ExtensionRegistry.createWidgetAdapter(JPanel.class);
		contentAdapter.setDelegate(this);
		JFrame me = (JFrame) getWidget();
		rootPane = (JComponent) me.getContentPane();
		jrootPane = me.getRootPane();
		contentAdapter.setWidget(rootPane);
	}

	public void doLayout() {
		contentAdapter.doLayout();
	}

	protected void createPostInitCode(StringBuilder builder, ImportRewrite imports) {
		Dimension size = rootPane.getSize();
		String cName = imports.addImport("java.awt.Dimension");
		builder.append("getContentPane().setPreferredSize(new " + cName + "(" + rootPane.getWidth() + ", " + rootPane.getHeight() + "));\n");
		builder.append("getContentPane().setSize(" + size.width + ", " + size.height + ");\n");
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		if (child instanceof JMenuBar) {
			JFrame jframe = (JFrame) getWidget();
			jframe.setJMenuBar((JMenuBar) child);
		} else
			contentAdapter.addChildByConstraints(child, constraints);
	}

	@Override
	public void clearSelection() {
		setSelected(false);
		JFrame jframe = (JFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb != null) {
			WidgetAdapter jmbAdapter = WidgetAdapter.getWidgetAdapter(jmb);
			jmbAdapter.clearSelection();
		}
		contentAdapter.clearSelection();
	}

	@Override
	public int getCursorLocation(Point p) {
		int w = jrootPane.getWidth();
		int h = jrootPane.getHeight();
		int x = p.x;
		int y = p.y;
		if (x < -ADHERE_PAD) {
			return OUTER;
		} else if (x < ADHERE_PAD) {
			if (y < -ADHERE_PAD) {
				return OUTER;
			} else if (y < ADHERE_PAD) {
				return LEFT_TOP;
			} else if (y < h - ADHERE_PAD) {
				return LEFT;
			} else if (y < h + ADHERE_PAD) {
				return LEFT_BOTTOM;
			} else {
				return OUTER;
			}
		} else if (x < w - ADHERE_PAD) {
			if (y < -ADHERE_PAD) {
				return OUTER;
			} else if (y < ADHERE_PAD) {
				return TOP;
			} else if (y < h - ADHERE_PAD) {
				return INNER;
			} else if (y < h + ADHERE_PAD) {
				return BOTTOM;
			} else {
				return OUTER;
			}
		} else if (x < w + ADHERE_PAD) {
			if (y < -ADHERE_PAD) {
				return OUTER;
			} else if (y < ADHERE_PAD) {
				return RIGHT_TOP;
			} else if (y < h - ADHERE_PAD) {
				return RIGHT;
			} else if (y < h + ADHERE_PAD) {
				return RIGHT_BOTTOM;
			} else {
				return OUTER;
			}
		} else {
			return OUTER;
		}
	}

	@Override
	protected Component createWidget() {
		return new JFrame();
	}

	@Override
	public Object getChildConstraints(Component child) {
		if (child instanceof JMenuBar)
			return null;
		return contentAdapter.getChildConstraints(child);
	}

	@Override
	public boolean isRoot() {
		return true;
	}

	@Override
	public Component getRootPane() {
		return jrootPane;
	}

	@Override
	public Border getDesignBorder() {
		FrameBorder frameBorder = new FrameBorder((JFrame) getWidget());
		return frameBorder;
	}

	@Override
	public Rectangle getDesignBounds() {
		Rectangle bounds = contentAdapter.getWidget().getBounds();
		if (bounds.width <= 0)
			bounds.width = 400;
		if (bounds.height <= 0)
			bounds.height = 300;
		bounds.y = 44;
		bounds.x = 24;
		return bounds;
	}

	@Override
	protected Component newWidget() {
		return new JFrame();
	}

	@Override
	public Component cloneWidget() {
		JRootPane jrp = new JRootPane();
		JFrame jframe = (JFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb != null) {
			WidgetAdapter jmbAdapter = WidgetAdapter.getWidgetAdapter(jmb);
			JMenuBar jmenubar = (JMenuBar) jmbAdapter.cloneWidget();
			jrp.setJMenuBar(jmenubar);
		}
		Container container = (Container) contentAdapter.cloneWidget();
		jrp.setContentPane(container);
		return jrp;
	}

	public Component getComponent() {
		return contentAdapter.getWidget();
	}

	@Override
	public void addAfter(Component hovering, Component dragged) {
		contentAdapter.addAfter(hovering, dragged);
	}

	@Override
	public void addBefore(Component hovering, Component dragged) {
		contentAdapter.addBefore(hovering, dragged);
	}

	@Override
	public void addChild(Component widget) {
		contentAdapter.addChild(widget);
	}

	@Override
	public boolean doAlignment(String id) {
		return contentAdapter.doAlignment(id);
	}

	@Override
	public IUndoableOperation doKeyPressed(KeyEvent e) {
		return contentAdapter.doKeyPressed(e);
	}

	@Override
	public Component getChild(int index) {
		JFrame jframe = (JFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb == null)
			return contentAdapter.getChild(index);
		else if (index == 0)
			return jmb;
		else
			return contentAdapter.getChild(index - 1);
	}

	@Override
	public int getChildCount() {
		JFrame jframe = (JFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		int count = contentAdapter.getChildCount();
		return jmb == null ? count : (count + 1);
	}

	public String toString() {
		if (isRoot()) {
			return "[" + getWidgetName() + "]";
		} else {
			return getName() + " [" + getWidgetName() + "]";
		}
	}

	@Override
	public int getIndexOfChild(Component child) {
		JFrame jframe = (JFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb == null)
			return contentAdapter.getIndexOfChild(child);
		else if (jmb == child)
			return 0;
		else
			return contentAdapter.getIndexOfChild(child) + 1;
	}

	@Override
	public boolean allowChildResize() {
		return contentAdapter.allowChildResize();
	}

	@Override
	public boolean dragOver(Point p) {
		return contentAdapter.dragOver(p);
	}

	public Point convertToGlobal(Point p) {
		JFrame jframe = (JFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb == null)
			return contentAdapter.convertToGlobal(p);
		else {
			p = SwingUtilities.convertPoint(jrootPane, p, rootPane);
			return contentAdapter.convertToGlobal(p);
		}
	}

	public Point convertToLocal(Point p) {
		return contentAdapter.convertToLocal(p);
	}

	@Override
	public boolean dragEnter(Point p) {
		return contentAdapter.dragEnter(p);
	}

	public WidgetAdapter getRootAdapter() {
		return this;
	}

	@Override
	public boolean dragExit(Point p) {
		return contentAdapter.dragExit(p);
	}

	@Override
	public boolean drop(Point p) {
		return contentAdapter.drop(p);
	}

	@Override
	public void paintFocused(Graphics clipg) {
		JFrame jframe = (JFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb != null) {
			Rectangle bounds = rootPane.getBounds();
			bounds.x = bounds.y = 0;
			bounds = SwingUtilities.convertRectangle(rootPane, bounds, jrootPane);
			clipg = clipg.create(bounds.x, bounds.y, bounds.width, bounds.height);
		}
		contentAdapter.paintFocused(clipg);
		if (jmb != null) {
			clipg.dispose();
		}
	}

	@Override
	public void paintBaselineAnchor(Graphics clipg) {
		JFrame jframe = (JFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb != null) {
			Rectangle bounds = rootPane.getBounds();
			bounds.x = bounds.y = 0;
			bounds = SwingUtilities.convertRectangle(rootPane, bounds, jrootPane);
			clipg = clipg.create(bounds.x, bounds.y, bounds.width, bounds.height);
		}
		contentAdapter.paintBaselineAnchor(clipg);
		if (jmb != null) {
			clipg.dispose();
		}
	}

	@Override
	protected String createInitCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createInitCode(imports));
		contentAdapter.createAddCode(imports, builder);
		return builder.toString();
	}

	public boolean removeChild(Component child) {
		if (child instanceof JMenuBar) {
			JFrame jframe = (JFrame) getWidget();
			jframe.setJMenuBar(null);
			return true;
		} else
			return contentAdapter.removeChild(child);
	}

	@Override
	protected boolean isChildVisible(Component child) {
		return contentAdapter.isChildVisible(child);
	}

	@Override
	public void showChild(Component widget) {
		contentAdapter.showChild(widget);
	}

	@Override
	public void fillContextAction(MenuManager menu) {
		super.fillContextAction(menu);
		contentAdapter.fillSetLayoutAction(menu);
	}

	@Override
	public void fillConstraintsAction(MenuManager menu, Component child) {
		if (!(child instanceof JMenuBar))
			contentAdapter.fillConstraintsAction(menu, child);
	}

	@Override
	public void adjustLayout(Component widget) {
		contentAdapter.adjustLayout(widget);
	}

	@Override
	public boolean isSelectionAlignResize(String id) {
		return contentAdapter.isSelectionAlignResize(id);
	}

	protected boolean createConstructor(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		IMethod cons = type.getMethod(type.getElementName(), new String[0]);
		if (!cons.exists()) {
			StringBuilder builder = new StringBuilder();
			builder.append("public " + type.getElementName() + "(){\n");
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
}
