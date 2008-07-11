/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.editors;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.designer.Event;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * 
 * VisualSwingOutline
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class VisualSwingOutline extends ContentOutlinePage {
	private ComponentTreeInput input;
	private VisualDesigner designer;

	public VisualSwingOutline(VisualDesigner designer) {
		assert designer != null;
		this.designer = designer;
		this.input = new ComponentTreeInput(designer);
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer treeView = getTreeViewer();
		treeView.setContentProvider(new ComponentTreeContentProvider());
		treeView.setLabelProvider(new ComponentTreeLabelProvider());
		treeView.setInput(input);
		treeView.expandToLevel(2);
		treeView.addSelectionChangedListener(this);
		Tree tree = (Tree) treeView.getTree();
		tree.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(MenuDetectEvent e) {
				_showMenu(e);
			}
		});
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				_mouseDoubleClicked(e);
			}
		});
		new OutlineViewDnD(designer).attach(treeView);
	}

	private void _mouseDoubleClicked(MouseEvent e) {
		Tree tree = (Tree) getTreeViewer().getTree();
		TreeItem item = tree.getItem(new Point(e.x, e.y));
		if (item != null) {
			if(item.getExpanded()){
				getTreeViewer().collapseToLevel(item.getData(), 1);
			}else{
				getTreeViewer().expandToLevel(item.getData(), 1);
			}
			if (item.getData() instanceof EventMethod) {
				EventMethod eMethod = (EventMethod) item.getData();
				eMethod.editCode();
			}
		}
	}

	private void _showMenu(MenuDetectEvent e) {
		Tree tree = (Tree) getTreeViewer().getTree();
		if (tree != null) {
			TreeItem[] items = tree.getSelection();
			if (items != null && items.length > 0) {
				TreeItem item = items[0];
				Object object = item.getData();
				if (object instanceof JComponent) {
					WidgetAdapter adapter = WidgetAdapter
							.getWidgetAdapter((JComponent) object);
					if (adapter != null) {
						WhiteBoard.sendEvent(new Event(this,
								Event.EVENT_SHOW_POPUP, new Object[] {
										new java.awt.Point(e.x, e.y),
										adapter.getWidget() }));
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (event.getSource() == getTreeViewer()) {
			IStructuredSelection selection = (IStructuredSelection) event
					.getSelection();
			designer.clearSelection();
			for (Object object : selection.toArray()) {
				if (object != null && object instanceof JComponent) {
					WidgetAdapter adapter = WidgetAdapter
							.getWidgetAdapter((JComponent) object);
					if (adapter != null) {
						adapter.setSelected(true);
					}
				}
			}
			designer.repaint();
			super.selectionChanged(event);
		} else {
			getTreeViewer().refresh();
			TreePath[] paths = getTreePath((List<JComponent>) event
					.getSelection());
			TreeSelection sel = new TreeSelection(paths);
			setSelection(sel);
		}
	}

	private TreePath[] getTreePath(List<JComponent> components) {
		List<TreePath> paths = new ArrayList<TreePath>();
		for (JComponent component : components) {
			paths.add(buildTreePath(component));
		}
		TreePath[] array = new TreePath[paths.size()];
		return paths.toArray(array);
	}

	private TreePath buildTreePath(JComponent component) {
		List<Object> objects = new ArrayList<Object>();
		objects.add(input);
		objects.add(designer);
		addPathObject(objects, component);
		Object[] array = new Object[objects.size()];
		objects.toArray(array);
		TreePath treePath = new TreePath(array);
		return treePath;
	}

	private void addPathObject(List<Object> objects, JComponent component) {
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(component);
		if (adapter.isRoot()) {
			objects.add(component);
		} else {
			WidgetAdapter parent = adapter.getParentAdapter();
			addPathObject(objects, parent.getWidget());
			objects.add(component);
		}
	}
}
