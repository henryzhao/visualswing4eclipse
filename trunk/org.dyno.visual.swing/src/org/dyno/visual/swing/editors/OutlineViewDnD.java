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

package org.dyno.visual.swing.editors;

import java.awt.Component;
import java.awt.Container;

import javax.swing.SwingUtilities;

import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TreeDropTargetEffect;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

class OutlineViewDnD extends DropTargetAdapter implements DragSourceListener {
	private TreeViewer treeView;
	private Tree tree;
	private TreeItem[] treeItems;
	private Display display;
	private VisualDesigner designer;

	public OutlineViewDnD(VisualDesigner designer) {
		this.designer = designer;
	}

	public void attach(TreeViewer treeView) {
		this.treeView = treeView;
		this.tree = treeView.getTree();
		display = tree.getDisplay();

		Transfer[] types = new Transfer[] { new JComponentTransfer() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
		final DragSource source = new DragSource(tree, operations);
		source.setTransfer(types);
		treeItems = new TreeItem[1];
		source.addDragListener(this);

		DropTarget target = new DropTarget(tree, operations);
		target.setDropTargetEffect(new MyDropTargetEffect(tree));
		target.setTransfer(types);
		target.addDropListener(this);
	}

	public void dragFinished(DragSourceEvent event) {
		if (event.detail == DND.DROP_MOVE) {
			for (TreeItem item : treeItems)
				item.dispose();
			treeView.refresh();
			designer.repaint();
			designer.publishSelection();
		}
		treeItems = null;
	}

	public void dragSetData(DragSourceEvent event) {
		Component[] components = new Component[treeItems.length];
		for (int i = 0; i < treeItems.length; i++) {
			components[i] = (Component) treeItems[i].getData();
		}
		event.data = components;
	}

	public void dragStart(DragSourceEvent event) {
		TreeItem[] selection = tree.getSelection();
		if (selection.length > 0) {
			Container parent = null;
			for (TreeItem item : selection) {
				Object object = item.getData();
				if (!(object instanceof Component)) {
					event.doit = false;
					return;
				} else {
					Component comp = (Component) object;
					WidgetAdapter adapter = WidgetAdapter
							.getWidgetAdapter(comp);
					if (adapter.isRoot()) {
						event.doit = false;
						return;
					} else {
						if (parent == null)
							parent = comp.getParent();
						else if (parent != comp.getParent()) {
							event.doit = false;
							return;
						}
					}
				}
			}
			treeItems = selection;
			event.doit = true;
		} else {
			event.doit = false;
		}
	}

	public void dragOver(DropTargetEvent event) {
		event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
		if (event.item != null) {
			TreeItem item = (TreeItem) event.item;
			Point pt = display.map(null, tree, event.x, event.y);
			Rectangle bounds = item.getBounds();
			if (pt.y < bounds.y + bounds.height / 4) {
				event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
			} else if (pt.y > bounds.y + 3 * bounds.height / 4) {
				event.feedback |= DND.FEEDBACK_INSERT_AFTER;
			} else {
				event.feedback |= DND.FEEDBACK_SELECT;
			}
		}
	}

	private void moveToSelectedNode(CompositeAdapter parent_adapter,
			Component target_comp, DropTargetEvent event, Component[] components) {
		if (parent_adapter.getWidget() == target_comp) {
			event.detail = DND.DROP_NONE;
		} else {
			WidgetAdapter target_adapter = WidgetAdapter
					.getWidgetAdapter(target_comp);
			CompositeAdapter target_parent = target_adapter.getParentAdapter();
			if (target_parent == parent_adapter) {
				if (!(target_adapter instanceof CompositeAdapter)) {
					event.detail = DND.DROP_NONE;
					return;
				}
			}
			for (Component component : components) {
				if (component == target_comp) {
					event.detail = DND.DROP_NONE;
					return;
				}
				if (SwingUtilities.isDescendingFrom(target_comp, component)) {
					event.detail = DND.DROP_NONE;
					return;
				}
			}
			for (Component component : components) {
				parent_adapter.removeChild(component);
				if (target_adapter instanceof CompositeAdapter)
					((CompositeAdapter) target_adapter).addChild(component);
			}
			target_adapter.getRootAdapter().getWidget().validate();
		}
	}

	private void moveBeforeSelectedNode(CompositeAdapter parent_adapter,
			Component target_comp, DropTargetEvent event, Component[] components) {
		if (parent_adapter.getWidget() == target_comp) {
			event.detail = DND.DROP_NONE;
		} else {
			WidgetAdapter target_adapter = WidgetAdapter
					.getWidgetAdapter(target_comp);
			CompositeAdapter target_parent = target_adapter.getParentAdapter();
			for (Component component : components) {
				if (component == target_comp) {
					event.detail = DND.DROP_NONE;
					return;
				}
				if (SwingUtilities.isDescendingFrom(target_comp, component)) {
					event.detail = DND.DROP_NONE;
					return;
				}
			}
			for (Component component : components) {
				parent_adapter.removeChild(component);
				target_parent.addBefore(target_comp, component);
			}
		}
	}

	private void moveAfterSelectedNode(CompositeAdapter parent_adapter,
			Component target_comp, DropTargetEvent event, Component[] components) {
		if (parent_adapter.getWidget() == target_comp) {
			event.detail = DND.DROP_NONE;
		} else {
			WidgetAdapter target_adapter = WidgetAdapter
					.getWidgetAdapter(target_comp);
			CompositeAdapter target_parent = target_adapter.getParentAdapter();
			for (Component component : components) {
				if (component == target_comp) {
					event.detail = DND.DROP_NONE;
					return;
				}
				if (SwingUtilities.isDescendingFrom(target_comp, component)) {
					event.detail = DND.DROP_NONE;
					return;
				}
			}
			for (Component component : components) {
				parent_adapter.removeChild(component);
				target_parent.addAfter(target_comp, component);
			}
		}
	}

	public void drop(DropTargetEvent event) {

		if (event.data == null) {
			event.detail = DND.DROP_NONE;
			return;
		} else if (event.item == null) {
			event.detail = DND.DROP_NONE;
		} else {
			TreeItem target_item = (TreeItem) event.item;
			Object data = target_item.getData();
			if (!(data instanceof Component)) {
				event.detail = DND.DROP_NONE;
			} else {
				Component[] components = (Component[]) event.data;
				if (components.length == 0) {
					event.detail = DND.DROP_NONE;
				} else {
					Component first = components[0];
					WidgetAdapter first_adapter = WidgetAdapter
							.getWidgetAdapter(first);
					CompositeAdapter parent_adapter = first_adapter
							.getParentAdapter();
					if ((feedback & DND.FEEDBACK_SELECT) != 0) {
						moveToSelectedNode(parent_adapter, target_comp, event,
								components);
					} else if ((feedback & DND.FEEDBACK_INSERT_BEFORE) != 0) {
						moveBeforeSelectedNode(parent_adapter, target_comp,
								event, components);
					} else if ((feedback & DND.FEEDBACK_INSERT_AFTER) != 0) {
						moveAfterSelectedNode(parent_adapter, target_comp,
								event, components);
					} else {
						event.detail = DND.DROP_NONE;
					}
				}
			}
		}
	}

	private int feedback;
	private Component target_comp;

	private class MyDropTargetEffect extends TreeDropTargetEffect {
		public MyDropTargetEffect(Tree tree) {
			super(tree);
		}

		@Override
		public void dragOver(DropTargetEvent event) {
			feedback = event.feedback;
			if (event.item.getData() instanceof Component)
				target_comp = (Component) event.item.getData();
			super.dragOver(event);
		}
	}
}

