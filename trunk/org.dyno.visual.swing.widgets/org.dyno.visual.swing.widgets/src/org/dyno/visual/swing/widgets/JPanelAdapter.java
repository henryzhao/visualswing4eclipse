/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.dyno.visual.swing.base.Azimuth;
import org.dyno.visual.swing.base.EditorAction;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.ILayoutBean;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.actions.NullLayoutAction;
import org.dyno.visual.swing.widgets.actions.SetLayoutAction;
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
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.PlatformUI;
public class JPanelAdapter extends CompositeAdapter {
	private static int VAR_INDEX = 0;
	private boolean intermediate = false;

	public JPanelAdapter() {
		super("jPanel" + (VAR_INDEX++));
	}

	@Override
	public JComponent cloneWidget() {
		JPanel panel = (JPanel) super.cloneWidget();
		JPanel jp = (JPanel) getWidget();
		LayoutManager layout = jp.getLayout();
		if (layout == null) {
			panel.setLayout(null);
			int count = this.getChildCount();
			for (int i = 0; i < count; i++) {
				JComponent widget = getChild(i);
				WidgetAdapter child = WidgetAdapter.getWidgetAdapter(widget);
				Rectangle bounds = widget.getBounds();
				JComponent copy = child.cloneWidget();
				copy.setBounds(bounds);
				panel.add(copy);
			}
		} else {
			getLayoutAdapter().cloneLayout(panel);
		}
		return panel;
	}

	protected JComponent createWidget() {
		JPanel jp = new JPanel();
		jp.setSize(100, 100);
		ILayoutBean bean = LayoutAdapter.getDefaultLayoutBean();
		if (bean != null)
			bean.initConainerLayout(jp, null);
		return jp;
	}

	@Override
	public void addAfter(JComponent hovering, JComponent dragged) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null) {
			int hoveringIndex = getComponentIndex(hovering);
			if (hoveringIndex == -1)
				jpanel.add(dragged);
			else if (hoveringIndex == getWidget().getComponentCount() - 1) {
				jpanel.add(dragged);
			} else {
				jpanel.add(dragged, hoveringIndex + 1);
			}
		} else {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			layoutAdapter.addAfter(hovering, dragged);
		}
	}

	private int getComponentIndex(Component child) {
		int count = getWidget().getComponentCount();
		for (int i = 0; i < count; i++) {
			Component comp = getWidget().getComponent(i);
			if (comp == child)
				return i;
		}
		return -1;
	}

	@Override
	public void addBefore(JComponent hovering, JComponent dragged) {
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
	public void addChild(JComponent widget) {
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
					e.printStackTrace();
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

	public LayoutAdapter getLayoutAdapter() {
		if (layoutAdapter == null) {
			LayoutManager layout = getWidget().getLayout();
			if (layout != null) {
				layoutAdapter = LayoutAdapter.getLayoutAdapter(getWidget());
				layoutAdapter.setContainer(getWidget());
			}
		}
		return layoutAdapter;
	}
	public void setLayoutAdapter(LayoutAdapter layoutAdapter){
		this.layoutAdapter = layoutAdapter;
	}
	private LayoutAdapter layoutAdapter;

	@Override
	public JComponent getChild(int index) {
		return (JComponent) getWidget().getComponent(index);
	}

	@Override
	public int getChildCount() {
		return getWidget().getComponentCount();
	}

	public String toString() {
		if (isRoot()) {
			return "[" + getWidgetName() + getLayoutName() + "]";
		} else {
			return getName() + " [" + getWidgetName() + getLayoutName() + "]";
		}
	}

	private String getLayoutName() {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		String layoutName = layout == null ? "null" : layout.getClass().getName();
		boolean default_layout = LayoutAdapter.DEFAULT_LAYOUT.equals(layoutName);
		layoutName = layout == null ? "null" : getLayoutAdapter().getName();
		return default_layout ? "" : "(" + layoutName + ")";
	}

	@Override
	public int getIndexOfChild(JComponent child) {
		int size = getChildCount();
		for (int i = 0; i < size; i++) {
			Component comp = getWidget().getComponent(i);
			if (comp == child)
				return i;
		}
		return -1;
	}

	@Override
	public boolean allowChildResize() {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null) {
			return true;
		} else {
			return getLayoutAdapter().isChildResizable();
		}
	}

	@Override
	public boolean dragOver(Point p) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null) {
			int state = getState();
			if (state == Azimuth.STATE_BEAN_HOVER) {
				setMascotLocation(p);
			} else {
				resize_widget(p);
			}
			return true;
		} else {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			return layoutAdapter.dragOver(p);
		}
	}

	@Override
	public boolean dragEnter(Point p) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null) {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			return layoutAdapter.dragEnter(p);
		} else
			return true;
	}

	@Override
	public boolean dragExit(Point p) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null) {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			return layoutAdapter.dragExit(p);
		} else
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
		getDropWidget().getWidget().doLayout();
	}

	@Override
	public boolean drop(Point p) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null) {
			int state = getState();
			WidgetAdapter adapter = getDropWidget();
			JComponent child = adapter.getComponent();
			Point htsp = adapter.getHotspotPoint();
			switch (state) {
			case Azimuth.STATE_BEAN_HOVER:
				child.setLocation(p.x - htsp.x, p.y - htsp.y);
				break;
			default:
				Point pt = getMascotLocation();
				child.setLocation(pt.x - htsp.x, pt.y - htsp.y);
				break;
			}
			jpanel.add(child);
			doLayout();
			clearAllSelected();
			adapter.setSelected(true);
			adapter.setDirty(true);
			getWidget().validate();
			repaintDesigner();
			return true;
		} else {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			WidgetAdapter adapter = getDropWidget();
			if (layoutAdapter.drop(p)) {
				doLayout();
				clearAllSelected();
				adapter.setSelected(true);
				adapter.setDirty(true);
				getWidget().validate();
				repaintDesigner();
				layoutAdapter.setContainer(getWidget());
				return true;
			} else
				return false;
		}
	}

	@Override
	public void paintFocused(Graphics clipg) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null) {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			layoutAdapter.paintFocused(clipg);
		}
	}

	@Override
	public void paintBaselineAnchor(Graphics g) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null) {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			layoutAdapter.paintBaselineAnchor(g);
		}
	}

	public boolean removeChild(JComponent child) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null) {
			getWidget().remove(child);
			getWidget().validate();
			return true;
		} else {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			boolean success = layoutAdapter.removeChild(child);
			if (success) {
				getWidget().validate();
				layoutAdapter.setContainer(getWidget());
				return true;
			} else
				return false;
		}
	}

	@Override
	protected boolean isChildVisible(JComponent child) {
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
	public void showChild(JComponent widget) {
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
	public void fillContextAction(MenuManager menu) {
		super.fillContextAction(menu);
		MenuManager layoutMenu = new MenuManager("Set Layout", "#SET_LAYOUT");
		fillLayoutAction(layoutMenu);
		menu.add(layoutMenu);		
	}

	@Override
	public void fillConstraintsAction(MenuManager menu, JComponent child) {
		JPanel jpanel = (JPanel)getWidget();
		LayoutManager layout = jpanel.getLayout();
		if(layout!=null)
			getLayoutAdapter().fillConstraintsAction(menu, child);
	}

	private void fillLayoutAction(MenuManager layoutMenu) {
		Action nullLayoutAction = new NullLayoutAction(this);
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null)
			nullLayoutAction.setChecked(true);
		layoutMenu.add(nullLayoutAction);
		for (String layoutClass : LayoutAdapter.getLayoutClasses()) {
			IConfigurationElement config = LayoutAdapter.getLayoutConfig(layoutClass);
			SetLayoutAction action = new SetLayoutAction(this, config);
			if (layout != null) {
				String currLayoutClass = layout.getClass().getName();
				if (currLayoutClass.equals(layoutClass)) {
					action.setChecked(true);
				}
			}
			layoutMenu.add(action);
		}
	}

	@Override
	public void adjustLayout(JComponent widget) {
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

	@Override
	protected String createGetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createGetCode(imports));
		genAddCode(imports, builder);
		return builder.toString();
	}

	void genAddCode(ImportRewrite imports, StringBuilder builder) {
		JPanel panel = (JPanel) getWidget();
		LayoutManager layout = panel.getLayout();
		if (layout == null) {
			builder.append(getFieldName(getName()) + "." + "setLayout(null);\n");
			int count = getChildCount();
			for (int i = 0; i < count; i++) {
				JComponent child = getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
				String getMethodName = getGetMethodName(childAdapter.getName());
				builder.append(getFieldName(getName()) + "." + "add(" + getMethodName + "());\n");
			}
		} else {
			builder.append(getLayoutAdapter().createCode(imports));
		}
	}

	@Override
	protected String createInitCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createInitCode(imports));
		JPanel panel = (JPanel) getWidget();
		LayoutManager layout = panel.getLayout();
		if (layout == null) {
			builder.append("setLayout(null);\n");
			int count = getChildCount();
			for (int i = 0; i < count; i++) {
				JComponent child = getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
				String getMethodName = getGetMethodName(childAdapter.getName());
				builder.append("add(" + getMethodName + "());\n");
			}
		} else {
			builder.append(getLayoutAdapter().createCode(imports));
		}
		return builder.toString();
	}

	@Override
	public boolean needGenBoundCode() {
		JPanel panel = (JPanel) getWidget();
		LayoutManager layout = panel.getLayout();
		return layout == null;
	}

	public boolean isIntermediate() {
		return intermediate;
	}

	public void setIntermediate(boolean intermediate) {
		this.intermediate = intermediate;
	}

	@Override
	protected JComponent newWidget() {
		return new JPanel();
	}

	@Override
	public void addChildByConstraints(JComponent child, Object constraints) {
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
	public Object getChildConstraints(JComponent child) {
		JPanel panel = (JPanel) getWidget();
		LayoutManager layout = panel.getLayout();
		if (layout == null) {
			return child.getBounds();
		} else {
			return getLayoutAdapter().getChildConstraints(child);
		}
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
}