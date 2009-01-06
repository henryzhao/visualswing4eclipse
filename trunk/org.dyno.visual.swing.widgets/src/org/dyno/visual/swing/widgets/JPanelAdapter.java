
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.Border;

import org.dyno.visual.swing.base.Azimuth;
import org.dyno.visual.swing.base.EditorAction;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IEditor;
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
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.PlatformUI;

public class JPanelAdapter extends CompositeAdapter {
	private boolean intermediate = false;
	private WidgetAdapter delegate;

	public JPanelAdapter() {
		super(null);
	}
	@Override
	public String getName() {
		if(delegate!=null)
			return delegate.getName();
		return name;
	}

	void setDelegate(WidgetAdapter delegate) {
		this.delegate = delegate;
	}

	void setWidgetWithoutAttach(Component widget) {
		this.widget = widget;
		this.dirty = false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapterClass) {
		Object adaptable = super.getAdapter(adapterClass);
		if(adaptable==null){
			LayoutAdapter adapter=getLayoutAdapter();
			if(adapter!=null)
				return adapter.getAdapter(adapterClass);
			else
				return null;
		}else
			return adaptable;
		
	}
	@Override
	public Component getRootPane() {
		if(delegate!=null)
			return delegate.getRootPane();
		return super.getRootPane();
	}

	@Override
	public Class<?> getDefaultLayout() {
		if(delegate!=null)
			return ((CompositeAdapter)delegate).getDefaultLayout();
		return FlowLayout.class;
	}

	@Override
	public Object getWidgetValue() {
		JPanel panel = (JPanel) getWidget();
		Border border = panel.getBorder();
		if (border != null) {
			BorderAdapter borderAdapter = BorderAdapter.getBorderAdapter(border.getClass());
			if(borderAdapter!=null){
				return borderAdapter.getWidgetValue(panel);
			}
		} 
		return null;
	}

	@Override
	public void setWidgetValue(Object value) {
		JPanel panel = (JPanel) getWidget();
		Border border = panel.getBorder();
		if (border != null) {
			BorderAdapter borderAdapter = BorderAdapter.getBorderAdapter(border.getClass());
			if(borderAdapter!=null){
				borderAdapter.setWidgetValue(panel, value);
			}
		} 
	}

	@Override
	public IEditor getEditorAt(int x, int y) {
		JPanel panel = (JPanel) getWidget();
		Border border = panel.getBorder();
		if (border != null) {
			BorderAdapter borderAdapter = BorderAdapter.getBorderAdapter(border.getClass());
			if(borderAdapter!=null){
				return borderAdapter.getEditorAt(panel, x, y);
			}
		} 
		return null;
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		JPanel panel = (JPanel) getWidget();
		Border border = panel.getBorder();
		if (border != null) {
			BorderAdapter borderAdapter = BorderAdapter.getBorderAdapter(border.getClass());
			if(borderAdapter!=null){
				return borderAdapter.getEditorBounds(panel, x, y);
			}
		} 
		return null;
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

	private int getComponentIndex(Component child) {
		JPanel jpanel = (JPanel) getWidget();
		int count = jpanel.getComponentCount();
		for (int i = 0; i < count; i++) {
			Component comp = jpanel.getComponent(i);
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
				IOperationHistory operationHistory = PlatformUI.getWorkbench()
						.getOperationSupport().getOperationHistory();
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
				layoutAdapter = LayoutAdapter
						.getLayoutAdapter((JPanel) getWidget());
				layoutAdapter.setContainer((JPanel) getWidget());
			}
		}
		return layoutAdapter;
	}

	public void setLayoutAdapter(LayoutAdapter layoutAdapter) {
		this.layoutAdapter = layoutAdapter;
	}

	private LayoutAdapter layoutAdapter;

	@Override
	public Component getChild(int index) {
		JPanel jp = (JPanel) getWidget();
		return (Component) jp.getComponent(index);
	}

	@Override
	public int getChildCount() {
		JPanel jp = (JPanel) getWidget();
		return jp.getComponentCount();
	}

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
		boolean default_layout = LayoutAdapter.DEFAULT_LAYOUT
				.equals(layoutName);
		layoutName = layout == null ? "null" : getLayoutAdapter().getName(); //$NON-NLS-1$
		return default_layout ? "" : "(" + layoutName + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Override
	public int getIndexOfChild(Component child) {
		int size = getChildCount();
		JPanel jpanel = (JPanel) getWidget();
		for (int i = 0; i < size; i++) {
			Component comp = jpanel.getComponent(i);
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
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragOver(p);
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
	protected WidgetAdapter getDelegateAdapter() {
		return this.delegate;
	}

	@Override
	public boolean dragEnter(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragEnter(p);
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
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragExit(p);
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null) {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			return layoutAdapter.dragExit(p);
		} else
			return true;
	}

	@Override
	public boolean needGenBoundCode() {
		JPanel panel = (JPanel) getWidget();
		LayoutManager layout = panel.getLayout();
		return layout == null;
	}
	
	private void resize_widget(Point p) {
		int state = getState();
		Dimension min = new Dimension(10, 10);
		List<WidgetAdapter>dropWidgets=getDropWidget();
		assert !dropWidgets.isEmpty();
		Component beResized=dropWidgets.get(0).getComponent();
		Dimension size = beResized.getSize();
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
		beResized.setSize(w, h);
		beResized.doLayout();
	}

	@Override
	public boolean drop(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.drop(p);
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null) {
			int state = getState();
			clearAllSelected();
			for (WidgetAdapter adapter : getDropWidget()) {
				Component child = adapter.getComponent();
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
				adapter.requestNewName();
				adapter.setSelected(true);				
			}
			setDirty(true);
			doLayout();
			getWidget().validate();
			repaintDesigner();
			return true;
		} else {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			WidgetAdapter[]copy=new WidgetAdapter[getDropWidget().size()];
			getDropWidget().toArray(copy);
			if (layoutAdapter.drop(p)) {
				clearAllSelected();
				for (WidgetAdapter adapter : copy) {
					adapter.requestNewName();
					adapter.setSelected(true);
				}
				setDirty(true);				
				layoutAdapter.setContainer(jpanel);
				doLayout();
				getWidget().validate();
				repaintDesigner();
				return true;
			} else
				return false;
		}
	}

	@Override
	public void paintHovered(Graphics clipg) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null) {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			layoutAdapter.paintFocused(clipg);
		}
		clipg.setColor(Color.lightGray);
		clipg.drawRect(0, 0, jpanel.getWidth() -1 , jpanel.getHeight() - 1);
	}
	@Override
	public void paintGrid(Graphics clipg) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null) {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			layoutAdapter.paintGrid(clipg);
		}
	}
	@Override
	public void paintHint(Graphics g) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null) {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			layoutAdapter.paintHint(g);
		}
	}
	@Override
	public void paintAnchor(Graphics g) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null) {
			LayoutAdapter layoutAdapter = getLayoutAdapter();
			layoutAdapter.paintAnchor(g);
		}
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
	protected boolean isChildVisible(Component child) {
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
	public void fillContextAction(MenuManager menu) {
		super.fillContextAction(menu);
		fillSetLayoutAction(menu);
	}

	void fillSetLayoutAction(MenuManager menu) {
		MenuManager layoutMenu = new MenuManager(Messages.JPanelAdapter_Set_Layout, "#SET_LAYOUT"); //$NON-NLS-2$
		fillLayoutAction(layoutMenu);
		menu.add(layoutMenu);
	}

	@Override
	public void fillConstraintsAction(MenuManager menu, Component child) {
		JPanel jpanel = (JPanel) getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout != null)
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
			IConfigurationElement config = LayoutAdapter
					.getLayoutConfig(layoutClass);
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
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JPanel.class;
	}
}

