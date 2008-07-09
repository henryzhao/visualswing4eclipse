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
import org.dyno.visual.swing.designer.WidgetSelection;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
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
		new OutlineViewDnD(designer).attach(treeView);
	}

	private void _showMenu(MenuDetectEvent e) {
		Tree tree = (Tree) getTreeViewer().getTree();
		if (tree != null) {
			TreeItem[] items = tree.getSelection();
			if (items != null && items.length > 0) {
				TreeItem item = items[0];
				Object object = item.getData();
				if (object instanceof JComponent) {
					WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter((JComponent) object);
					if (adapter != null) {
						WhiteBoard.sendEvent(new Event(this, Event.EVENT_SHOW_POPUP, new Object[] { new java.awt.Point(e.x, e.y), adapter.getWidget() }));
					}
				}
			}
		}
	}
	private Event createEvent(int id, Object param) {
		return new Event(this, id, param);
	}
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		super.selectionChanged(event);
		if (!isAdjusting) {
			TreeSelection selection = (TreeSelection) event.getSelection();
			TreePath[] paths = selection.getPaths();
			for (TreePath path : paths) {
				Object object = path.getLastSegment();
				if (object != null && object instanceof JComponent) {
					WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter((JComponent) object);
					if (adapter != null) {
						adapter.clearAllSelected();
						adapter.setSelected(true);
						isAdjusting = true;						
						WhiteBoard.sendEvent(createEvent(Event.EVENT_SELECTION, new WidgetSelection(designer.getRoot())));
						isAdjusting = false;
					}
				}
			}
			designer.repaint();
		}
	}

	private void asyncExec(Runnable run) {
		TreeViewer treeViewer = getTreeViewer();
		if (treeViewer == null)
			return;
		Control control = treeViewer.getControl();
		if (control == null)
			return;
		if (control.isDisposed())
			return;
		Display display = control.getDisplay();
		if (display == null)
			return;
		display.asyncExec(run);
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

	public void selectComponent(List<JComponent> components) {
		if (!isAdjusting) {
			final TreePath[] treePaths = getTreePath(components);
			asyncExec(new Runnable() {
				@Override
				public void run() {
					TreeViewer treeViewer = getTreeViewer();
					if (treeViewer != null) {
						for (TreePath treePath : treePaths) {
							if (!treeViewer.getExpandedState(treePath)) {
								treeViewer.setExpandedState(treePath, true);
							}
						}
						if (!treeViewer.getTree().isDisposed()) {
							treeViewer.refresh();
						}
					}
					TreeSelection selection = new TreeSelection(treePaths);
					isAdjusting = true;
					setSelection(selection);
					isAdjusting = false;
				}
			});
		}
	}

	private boolean isAdjusting;
}
