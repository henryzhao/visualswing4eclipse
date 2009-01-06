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

package org.dyno.visual.swing.plugin.spi;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.action.MenuManager;

/**
 * 
 * CompositeAdapter
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public abstract class CompositeAdapter extends WidgetAdapter {
	public CompositeAdapter() {
	}
	public LayoutAdapter getLayoutAdapter() {
		return null;
	}
	@Override
	public boolean includeName(String another) {
		int count = getChildCount();
		for(int i=0;i<count;i++){
			Component child = getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			if(childAdapter.includeName(another))
				return true;
		}
		return super.includeName(another);
	}
	
	@Override
	public void requestNewName() {
		super.requestNewName();
		int count = getChildCount();
		for(int i=0;i<count;i++){
			Component child = getChild(i);
			WidgetAdapter childAdapter=WidgetAdapter.getWidgetAdapter(child);
			childAdapter.requestNewName();
		}
	}
	public CompositeAdapter(String name) {
		super(name);
	}

	public void selectChildren() {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			Component child = getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			childAdapter.setSelected(true);
			if (childAdapter instanceof CompositeAdapter) {
				((CompositeAdapter) childAdapter).selectChildren();
			}
		}
	}

	@Override
	public boolean isRenamed() {
		if(super.isRenamed())
			return true;
		int count = getChildCount();
		for(int i=0;i<count;i++){
			Component child = getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			if(childAdapter.isRenamed())
				return true;
		}
		return false;
	}
	public boolean interceptPoint(Point p, int ad) {
		return false;
	}

	@Override
	public boolean isDirty() {
		if (dirty)
			return true;
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			Component child = getChild(i);
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);			
			if (adapter!=null&&adapter.isDirty())
				return true;
		}
		return false;
	}

	@Override
	public void clearDirty() {
		dirty = false;
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			Component child = getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			childAdapter.clearDirty();
		}
	}

	@Override
	protected Component createWidget() {
		return null;
	}

	public boolean allowChildResize() {
		return false;
	}

	public Component getChild(int index) {
		if (getWidget() instanceof Container)
			return ((Container) getWidget()).getComponent(index);
		else
			return null;
	}

	public boolean isIntermediate() {
		return false;
	}

	public int getChildCount() {
		if (getWidget() instanceof Container)
			return ((Container) getWidget()).getComponentCount();
		else
			return 0;
	}

	public int getIndexOfChild(Component child) {
		if (getWidget() instanceof Container) {
			int count = getChildCount();
			for (int i = 0; i < count; i++) {
				if (((Container) getWidget()).getComponent(i) == child)
					return i;
			}
		}
		return -1;
	}

	protected boolean isChildVisible(Component child) {
		return child.isVisible();
	}

	public abstract Object getChildConstraints(Component child);

	public abstract void addChildByConstraints(Component child, Object constraints);

	@Override
	public void clearSelection() {
		super.clearSelection();
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			Component child = getChild(i);
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			if (adapter != null)
				adapter.clearSelection();
		}
	}

	public boolean isChildMoveable() {
		return true;
	}

	public boolean removeChild(Component child) {
		if (getWidget() instanceof Container)
			((Container) getWidget()).remove(child);
		getWidget().validate();
		return true;
	}

	public void removeAllChild() {
		if (getWidget() instanceof Container)
			((Container) getWidget()).removeAll();
		doLayout();
		getWidget().validate();
	}

	public boolean isEnclosingContainer() {
		return false;
	}

	public void showChild(Component widget) {
		widget.setVisible(true);
	}

	public void addBefore(Component hovering, Component dragged) {
	}

	public void addAfter(Component hovering, Component dragged) {
	}

	public void addChild(Component widget) {
	}

	public boolean doAlignment(String id) {
		return false;
	}

	public IUndoableOperation doKeyPressed(KeyEvent e) {
		return null;
	}

	public void adjustLayout(Component widget) {
	}

	public boolean isSelectionAlignResize(String id) {
		return false;
	}

	public boolean needGenBoundCode() {
		return false;
	}

	public IWidgetPropertyDescriptor[] getConstraintsProperties(Component widget) {
		return null;
	}

	public Rectangle getVisibleRect(Component comp) {
		return null;
	}

	public boolean isViewContainer() {
		return false;
	}

	public void fillConstraintsAction(MenuManager menu, Component widget) {
	}
	
	protected boolean isDroppingMenuItem(){
		List<WidgetAdapter> target = getDropWidget();
		if(target==null)
			return false;
		if(target.size()!=1)
			return false;
		Component drop = target.get(0).getWidget();
		return drop instanceof JMenuItem;
	}
	protected boolean isDroppingMenuBar(){
		List<WidgetAdapter> target = getDropWidget();
		if(target==null)
			return false;
		if(target.size()!=1)
			return false;
		Component drop =  target.get(0).getWidget();
		return drop instanceof JMenuBar;
	}
	
	@Override
	public boolean dragEnter(Point p) {
		setMascotLocation(p);
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		setMascotLocation(p);
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		setMascotLocation(p);
		return true;
	}

	@Override
	public boolean drop(Point p) {
		setMascotLocation(p);
		return true;
	}
	public Class<?> getDefaultLayout() {
		return null;
	}
	public void paintGrid(Graphics clipg) {
	}	

	public void paintHovered(Graphics clipg) {
	}

	public void paintHint(Graphics g) {
	}
	public void paintAnchor(Graphics g) {
	}	
}

