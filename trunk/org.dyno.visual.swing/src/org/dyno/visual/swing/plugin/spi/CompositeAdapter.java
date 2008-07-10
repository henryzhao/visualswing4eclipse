/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.plugin.spi;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
/**
 * 
 * CompositeAdapter
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public abstract class CompositeAdapter extends WidgetAdapter {
	public CompositeAdapter() {
		super();
	}

	public CompositeAdapter(String name) {
		super(name);
	}

	public void selectChildren() {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			JComponent child = getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			childAdapter.setSelected(true);
			if (childAdapter instanceof CompositeAdapter) {
				((CompositeAdapter) childAdapter).selectChildren();
			}
		}
	}
	public boolean interceptPoint(Point p, int ad){
		return false;
	}
	@Override
	public boolean isDirty() {
		if (dirty)
			return true;
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			JComponent child = getChild(i);
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			if (adapter.isDirty())
				return true;
		}
		return false;
	}

	@Override
	public void clearDirty() {
		dirty = false;
		int count = getChildCount();
		for(int i=0;i<count;i++){
			JComponent child = getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			childAdapter.clearDirty();
		}
	}

	@Override
	protected JComponent createWidget() {
		return null;
	}

	public boolean allowChildResize() {
		return false;
	}

	public JComponent getChild(int index) {
		return (JComponent) getWidget().getComponent(index);
	}

	public boolean isIntermediate() {
		return false;
	}

	public int getChildCount() {
		return getWidget().getComponentCount();
	}

	public int getIndexOfChild(JComponent child) {
		int count = getWidget().getComponentCount();
		for (int i = 0; i < count; i++) {
			if (getWidget().getComponent(i) == child)
				return i;
		}
		return -1;
	}

	protected boolean isChildVisible(JComponent child) {
		return child.isVisible();
	}

	@Override
	public void clearSelection() {
		super.clearSelection();
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			JComponent child = getChild(i);
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			if (adapter != null)
				adapter.clearSelection();
		}
	}

	public boolean isChildMoveable() {
		return true;
	}

	public boolean removeChild(JComponent child) {
		getWidget().remove(child);
		getWidget().validate();
		return true;
	}

	public void removeAllChild() {
		getWidget().removeAll();
		doLayout();
		getWidget().validate();
	}

	public boolean isEnclosingContainer() {
		return false;
	}

	public void showChild(JComponent widget) {
		widget.setVisible(true);
	}
	public void addBefore(JComponent hovering, Component dragged) {
	}

	public void addAfter(JComponent hovering, Component dragged) {
	}

	public void addChild(JComponent widget) {
	}

	public boolean doAlignment(String id) {
		return false;
	}

	public IUndoableOperation doKeyPressed(KeyEvent e) {
		return null;
	}
	public void adjustLayout(JComponent widget) {
	}

	public boolean isSelectionAlignResize(String id) {
		return false;
	}

	public boolean needGenBoundCode() {
		return false;
	}
	@Override
	public boolean genCode(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			JComponent child = getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			if (!childAdapter.genCode(type, imports, monitor))
				return false;
		}
		if (!dirty)
			return true;
		return super.genCode(type, imports, monitor);
	}

	public IWidgetPropertyDescriptor[] getConstraintsProperties(JComponent widget) {
		return null;
	}

	public Rectangle getVisibleRect(JComponent comp) {
		return null;
	}

	public boolean isViewContainer() {
		return false;
	}	
}
