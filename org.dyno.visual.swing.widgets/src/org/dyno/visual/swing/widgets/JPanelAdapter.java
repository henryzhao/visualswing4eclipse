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

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.event.MouseInputListener;

import org.dyno.visual.swing.base.EditorAction;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.ILayoutBean;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.undo.BottomAlignmentOperation;
import org.dyno.visual.swing.widgets.undo.CenterAlignmentOperation;
import org.dyno.visual.swing.widgets.undo.KeyDownOperation;
import org.dyno.visual.swing.widgets.undo.KeyLeftOperation;
import org.dyno.visual.swing.widgets.undo.KeyRightOperation;
import org.dyno.visual.swing.widgets.undo.KeyUpOperation;
import org.dyno.visual.swing.widgets.undo.LeftAlignmentOperation;
import org.dyno.visual.swing.widgets.undo.MiddleAlignmentOperation;
import org.dyno.visual.swing.widgets.undo.PageDownOperation;
import org.dyno.visual.swing.widgets.undo.PageUpOperation;
import org.dyno.visual.swing.widgets.undo.RightAlignmentOperation;
import org.dyno.visual.swing.widgets.undo.SameHeightOperation;
import org.dyno.visual.swing.widgets.undo.SameWidthOperation;
import org.dyno.visual.swing.widgets.undo.TopAlignmentOperation;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("unchecked")
public class JPanelAdapter extends CompositeAdapter {
	private boolean intermediate = false;
	private WidgetAdapter delegate;

	public JPanelAdapter() {
		super(null);
	}

	public Component getContentArea() {
		return getWidget();
	}

	@Override
	public String getName() {
		if (delegate != null)
			return delegate.getName();
		return name;
	}

	@Override
	public String getID() {
		if (delegate != null)
			return delegate.getID();
		return super.getID();
	}

	void setDelegate(WidgetAdapter delegate) {
		this.delegate = delegate;
	}

	void setWidgetWithoutAttach(Component widget) {
		this.widget = widget;
		this.dirty = false;
	}

	@Override
	public Object getAdapter(Class adapterClass) {
		if (adapterClass == MouseInputListener.class) {
			LayoutAdapter adapter = getLayoutAdapter();
			if (adapter != null)
				return adapter.getAdapter(adapterClass);
		}
		return super.getAdapter(adapterClass);
	}

	@Override
	public Component getRootPane() {
		if (delegate != null)
			return delegate.getRootPane();
		return super.getRootPane();
	}

	@Override
	public Class<?> getDefaultLayout() {
		if (delegate != null)
			return ((CompositeAdapter) delegate).getDefaultLayout();
		Component comp = getWidget();
		if (comp.getClass() == JPanel.class) {
			return FlowLayout.class;
		} else if (isRoot() && comp.getClass().getSuperclass() == JPanel.class) {
			return FlowLayout.class;
		}
		Class superClazz = comp.getClass().getSuperclass();
		try {
			Container container = (Container) superClazz.newInstance();
			LayoutManager lm = container.getLayout();
			if (lm == null)
				return null;
			else
				return lm.getClass();
		} catch (Exception e) {
			return FlowLayout.class;
		}
	}

	@Override
	public List<WidgetAdapter> getDropWidget() {
		if (delegate != null)
			return delegate.getDropWidget();
		return super.getDropWidget();
	}

	public WidgetAdapter getRootAdapter() {
		if (isInternalFrameContentPane()) {
			JInternalFrame jif = getTopFrame();
			return WidgetAdapter.getWidgetAdapter(jif).getRootAdapter();
		}
		return super.getRootAdapter();
	}

	@Override
	public boolean isRoot() {
		if (isInternalFrameContentPane()) {
			JInternalFrame jif = getTopFrame();
			return WidgetAdapter.getWidgetAdapter(jif).isRoot();
		} else if (delegate != null)
			return delegate.isRoot();
		return super.isRoot();
	}

	private boolean isInternalFrameContentPane() {
		Component comp = widget;
		comp = comp.getParent();
		if (comp instanceof JLayeredPane) {
			comp = comp.getParent();
			if (comp instanceof JRootPane) {
				comp = comp.getParent();
				if (comp instanceof JInternalFrame)
					return true;
			}
		}
		return false;
	}

	private JInternalFrame getTopFrame() {
		Component comp = widget;
		while (!(comp instanceof JInternalFrame || comp == null)) {
			comp = comp.getParent();
		}
		return (JInternalFrame) comp;
	}

	@Override
	public Component cloneWidget() {
		JPanel panel = (JPanel) super.cloneWidget();
		JPanel jp = (JPanel) getWidget();
		LayoutManager layout = jp.getLayout();
		if (layout == null) {
			panel.setLayout(null);
			int count = this.getChildCount();
			for (int i = 0; i < count; i++) {
				Component widget = getChild(i);
				WidgetAdapter child = WidgetAdapter.getWidgetAdapter(widget);
				Rectangle bounds = widget.getBounds();
				Component copy = child.cloneWidget();
				copy.setBounds(bounds);
				panel.add(copy);
			}
		} else {
			getLayoutAdapter().cloneLayout(panel);
		}
		return panel;
	}

	protected Component createWidget() {
		JPanel jp = new JPanel();
		jp.setSize(100, 100);
		ILayoutBean bean = LayoutAdapter.getDefaultLayoutBean();
		if (bean != null)
			bean.initConainerLayout(jp, null);
		return jp;
	}

	@Override
	public void addAfter(Component hovering, Component dragged) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null) {
			int hoveringIndex = getComponentIndex(hovering);
			if (hoveringIndex == -1)
				jpanel.add(dragged);
			else if (hoveringIndex == jpanel.getComponentCount() - 1) {
				jpanel.add(dragged);
			} else {
				jpanel.add(dragged, hoveringIndex + 1);
			}
		} else {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			layoutAdapter.addAfter(hovering, dragged);
		}
	}

	@Override
	public Point getHotspotPoint() {
		if (delegate != null)
			return delegate.getHotspotPoint();
		return super.getHotspotPoint();
	}

	private int getComponentIndex(Component child) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			Component comp = getChild(i);
			if (comp == child)
				return i;
		}
		return -1;
	}

	@Override
	public void addBefore(Component hovering, Component dragged) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null) {
			int hoveringIndex = getComponentIndex(hovering);
			if (hoveringIndex == -1)
				jpanel.add(dragged, 0);
			else if (hoveringIndex == 0) {
				jpanel.add(dragged, 0);
			} else {
				jpanel.add(dragged, hoveringIndex);
			}
		} else {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			layoutAdapter.addBefore(hovering, dragged);
		}
	}

	@Override
	public void addChild(Component widget) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null) {
			jpanel.add(widget);
		} else {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			layoutAdapter.addChild(widget);
		}
	}

	@Override
	public boolean doAlignment(String id) {
		JPanel jp = (JPanel) getWidget();
		LayoutManager layout = jp.getLayout();
		if (layout == null) {
			IUndoableOperation operation = null;
			if (id.equals(EditorAction.ALIGNMENT_LEFT))
				operation = doLeft();
			else if (id.equals(EditorAction.ALIGNMENT_CENTER))
				operation = doCenter();
			else if (id.equals(EditorAction.ALIGNMENT_RIGHT))
				operation = doRight();
			else if (id.equals(EditorAction.ALIGNMENT_TOP))
				operation = doTop();
			else if (id.equals(EditorAction.ALIGNMENT_BOTTOM))
				operation = doBottom();
			else if (id.equals(EditorAction.ALIGNMENT_MIDDLE))
				operation = doMiddle();
			else if (id.equals(EditorAction.SAME_WIDTH))
				operation = doSameWidth();
			else if (id.equals(EditorAction.SAME_HEIGHT))
				operation = doSameHeight();
			if (operation != null) {
				operation.addContext(getUndoContext());
				IOperationHistory operationHistory = PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
				try {
					operationHistory.execute(operation, null, null);
					return true;
				} catch (Exception e) {
					WidgetPlugin.getLogger().error(e);
					return false;
				}
			} else {
				return false;
			}
		} else {
			return getLayoutAdapter().doAlignment(id);
		}
	}

	@Override
	public IUndoableOperation doKeyPressed(KeyEvent e) {
		JPanel jp = (JPanel) getWidget();
		LayoutManager layout = jp.getLayout();
		if (layout == null) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				return new KeyLeftOperation(this);
			case KeyEvent.VK_RIGHT:
				return new KeyRightOperation(this);
			case KeyEvent.VK_UP:
				return new KeyUpOperation(this);
			case KeyEvent.VK_DOWN:
				return new KeyDownOperation(this);
			case KeyEvent.VK_PAGE_UP:
				return new PageUpOperation(this);
			case KeyEvent.VK_PAGE_DOWN:
				return new PageDownOperation(this);
			}
			return null;
		} else {
			return getLayoutAdapter().doKeyPressed(e);
		}
	}

	private IUndoableOperation doSameHeight() {
		return new SameHeightOperation(getSelectedWidgets());
	}

	private IUndoableOperation doSameWidth() {
		return new SameWidthOperation(getSelectedWidgets());
	}

	private IUndoableOperation doMiddle() {
		return new MiddleAlignmentOperation(getSelectedWidgets());
	}

	private IUndoableOperation doBottom() {
		return new BottomAlignmentOperation(getSelectedWidgets());
	}

	private IUndoableOperation doTop() {
		return new TopAlignmentOperation(getSelectedWidgets());
	}

	private IUndoableOperation doRight() {
		return new RightAlignmentOperation(getSelectedWidgets());
	}

	private IUndoableOperation doCenter() {
		return new CenterAlignmentOperation(getSelectedWidgets());
	}

	private IUndoableOperation doLeft() {
		return new LeftAlignmentOperation(getSelectedWidgets());
	}

	@Override
	public LayoutAdapter getLayoutAdapter() {
		if (layoutAdapter == null) {
			LayoutManager layout = ((JPanel) getWidget()).getLayout();
			if (layout != null) {
				layoutAdapter = LayoutAdapter.getLayoutAdapter((JPanel) getWidget());
				layoutAdapter.setContainer((JPanel) getWidget());
			}
		}
		return layoutAdapter;
	}

	public void setLayoutAdapter(LayoutAdapter layoutAdapter) {
		this.layoutAdapter = layoutAdapter;
	}

	private LayoutAdapter layoutAdapter;

	public String toString() {
		if (isRoot()) {
			return "[" + getWidgetName() + getLayoutName() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return getName() + " [" + getWidgetName() + getLayoutName() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private String getLayoutName() {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		String layoutName = layout == null ? "null" : layout.getClass() //$NON-NLS-1$
				.getName();
		boolean default_layout = LayoutAdapter.DEFAULT_LAYOUT.equals(layoutName);
		layoutName = layout == null ? "null" : getLayoutAdapter().getName(); //$NON-NLS-1$
		return default_layout ? "" : "(" + layoutName + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Override
	public boolean allowChildResize(Component child) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null) {
			return true;
		} else {
			return getLayoutAdapter().isChildResizable();
		}
	}

	@Override
	protected WidgetAdapter getDelegateAdapter() {
		return this.delegate;
	}

	@Override
	public boolean needGenBoundCode() {
		JPanel panel = (JPanel) getWidget();
		LayoutManager layout = panel.getLayout();
		return layout == null;
	}

	public boolean removeChild(Component child) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null) {
			jpanel.remove(child);
			jpanel.validate();
			return true;
		} else {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			boolean success = layoutAdapter.removeChild(child);
			if (success) {
				jpanel.validate();
				layoutAdapter.setContainer(jpanel);
				return true;
			} else
				return false;
		}
	}

	@Override
	public boolean isChildVisible(Component child) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null) {
			return child.isVisible();
		} else {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			return layoutAdapter.isWidgetVisible(child);
		}
	}

	@Override
	public void showChild(Component widget) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null) {
			if (!widget.isVisible())
				widget.setVisible(true);
		} else {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			layoutAdapter.showChild(widget);
		}
	}

	@Override
	public void adjustLayout(Component widget) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null)
			getLayoutAdapter().adjustLayout(widget);
	}

	@Override
	public boolean isSelectionAlignResize(String id) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null)
			return getLayoutAdapter().isSelectionAlignResize(id);
		else
			return true;
	}

	public boolean isIntermediate() {
		return intermediate;
	}

	public void setIntermediate(boolean intermediate) {
		this.intermediate = intermediate;
	}

	@Override
	protected Component newWidget() {
		return new JPanel();
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		JPanel panel = (JPanel) getWidget();
		LayoutManager layout = panel.getLayout();
		if (layout == null) {
			child.setBounds((Rectangle) constraints);
			panel.add(child);
		} else {
			getLayoutAdapter().addChildByConstraints(child, constraints);
		}
	}

	@Override
	public Object getChildConstraints(Component child) {
		JPanel panel = (JPanel) getWidget();
		LayoutManager layout = panel.getLayout();
		if (layout == null) {
			return child.getBounds();
		} else {
			return getLayoutAdapter().getChildConstraints(child);
		}
	}

	@Override
	public Class getWidgetClass() {
		return JPanel.class;
	}

	public boolean isFocused() {
		WidgetAdapter focused = getFocusedAdapter();
		if (delegate != null)
			return focused == delegate;
		else
			return focused == this;
	}
}
