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

package org.dyno.visual.swing.designer;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.base.Azimuth;
import org.dyno.visual.swing.base.ShellAdaptable;
import org.dyno.visual.swing.editors.PaletteView;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.undo.SetWidgetValueOperation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * GlassTarget
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class GlassTarget extends DropTarget implements MouseInputListener,
		MouseWheelListener, Azimuth, ChangeListener, FocusListener {
	private static final long serialVersionUID = 5331246522463111004L;
	private int state;
	private GlassPlane glassPlane;
	private VisualDesigner designer;
	private MouseEvent dragging_event;
	private List<WidgetAdapter> currentAdapters;

	public GlassTarget(GlassPlane gp) {
		glassPlane = gp;
		designer = gp.getDesigner();
	}

	public int getState() {
		return state;
	}

	@Override
	public synchronized void dragEnter(DropTargetDragEvent dtde) {
		state = STATE_BEAN_HOVER;
	}

	@Override
	public synchronized void dragExit(DropTargetEvent dte) {
		state = STATE_MOUSE_HOVER;
		glassPlane.setHotspotPoint(null);
		glassPlane.repaint();
	}

	@Override
	public synchronized void dragOver(DropTargetDragEvent dtde) {
		dragOver(dtde.getLocation());
	}

	private WidgetAdapter hoveredAdapter;

	WidgetAdapter getFocusedAdapter() {
		return hoveredAdapter;
	}

	private void dragOver(Point p) {
		boolean update = false;
		if (state == STATE_BEAN_HOVER) {
			Component hovered = designer.componentAt(p, 0);
			if (hovered != null) {
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(hovered);
				if (!(adapter instanceof CompositeAdapter)) {
					adapter = adapter.getParentAdapter();
				}
				CompositeAdapter compositeAdapter = (CompositeAdapter) adapter;
				if (hoveredAdapter != compositeAdapter) {
					if (hoveredAdapter != null
							&& hoveredAdapter.dragExit(hoveredAdapter
									.convertToLocal(p)))
						update = true;
					hoveredAdapter = compositeAdapter;
					if (hoveredAdapter.dragEnter(hoveredAdapter
							.convertToLocal(p)))
						update = true;
				} else {
					if (compositeAdapter.dragOver(hoveredAdapter
							.convertToLocal(p)))
						update = true;
				}
			} else {
				if (hoveredAdapter != null) {
					hoveredAdapter.dragExit(hoveredAdapter.convertToLocal(p));
					hoveredAdapter = null;
				}
				glassPlane.setHotspotPoint(p);
				update = true;
			}
		} else if (currentAdapters != null) {
			hoveredAdapter = currentAdapters.get(0);
			if (((CompositeAdapter) hoveredAdapter).dragOver(hoveredAdapter
					.convertToLocal(p)))
				update = true;
		}
		if (update)
			glassPlane.repaint();
	}

	@Override
	public synchronized void drop(DropTargetDropEvent dtde) {
		drop(dtde.getLocation());
	}

	private void drop(Point p) {
		if (state == STATE_BEAN_HOVER) {
			Component hovered = designer.componentAt(p, 0);
			if (hovered != null) {
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(hovered);
				if (!(adapter instanceof CompositeAdapter)) {
					adapter = adapter.getParentAdapter();
				}
				CompositeAdapter compositeAdapter = (CompositeAdapter) adapter;
				if (compositeAdapter.drop(compositeAdapter.convertToLocal(p))) {
					if (lastParent != null) {
						IOperationHistory operationHistory = PlatformUI
								.getWorkbench().getOperationSupport()
								.getOperationHistory();
						List<Component> children = new ArrayList<Component>();
						List<Object> new_constraints = new ArrayList<Object>();
						for (WidgetAdapter wa : WhiteBoard.getSelectedWidget()) {
							Component child = wa.getComponent();
							children.add(child);
							new_constraints.add(compositeAdapter
									.getChildConstraints(child));
						}
						IUndoableOperation operation = new MoveResizeOperation(
								lastParent, compositeAdapter, children,
								lastConstraints, new_constraints);
						operation.addContext(designer.getUndoContext());
						try {
							operationHistory.execute(operation, null, null);
						} catch (ExecutionException e) {
							VisualSwingPlugin.getLogger().error(e);
						}
					}
					adapter.addNotify();
					adapter.setDirty(true);
				}
			} else {
				glassPlane.setHotspotPoint(null);
				if (lastParent != null) {
					IOperationHistory operationHistory = PlatformUI
							.getWorkbench().getOperationSupport()
							.getOperationHistory();
					List<Component> children = new ArrayList<Component>();
					for (WidgetAdapter wa : WhiteBoard.getSelectedWidget()) {
						children.add(wa.getComponent());
					}
					IUndoableOperation operation = new DragDropOperation(
							lastParent, children, lastConstraints);
					operation.addContext(designer.getUndoContext());
					try {
						operationHistory.execute(operation, null, null);
					} catch (ExecutionException e) {
						VisualSwingPlugin.getLogger().error(e);
					}
				}
			}
			hoveredAdapter = null;
			WhiteBoard.setSelectedWidget(null);
			PaletteView.clearToolSelection();
		} else if (currentAdapters != null) {
			WidgetAdapter adapter = currentAdapters.get(0);
			if (((CompositeAdapter) adapter).drop(adapter.convertToLocal(p))) {
				if (lastParent != null) {
					IOperationHistory operationHistory = PlatformUI
							.getWorkbench().getOperationSupport()
							.getOperationHistory();
					List<Component> children = new ArrayList<Component>();
					List<Object> new_constraints = new ArrayList<Object>();
					for (WidgetAdapter wa : WhiteBoard.getSelectedWidget()) {
						Component child = wa.getComponent();
						children.add(child);
						new_constraints.add(((CompositeAdapter) adapter)
								.getChildConstraints(child));
					}
					IUndoableOperation operation = new MoveResizeOperation(
							lastParent, ((CompositeAdapter) adapter), children,
							lastConstraints, new_constraints);
					operation.addContext(designer.getUndoContext());
					try {
						operationHistory.execute(operation, null, null);
					} catch (ExecutionException e) {
						VisualSwingPlugin.getLogger().error(e);
					}
				}
				adapter.changeNotify();
				adapter.setDirty(true);
			}
		}
		currentAdapters = null;
		state = STATE_MOUSE_HOVER;
		WhiteBoard.setSelectedWidget(null);
		glassPlane.repaint();
	}

	private void mouse_pressed(MouseEvent e) {
		Point point = e.getPoint();
		Component hovered = designer.componentAt(point,
				WidgetAdapter.ADHERE_PAD);
		if (hovered != null) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(hovered);
			Point hotspot = SwingUtilities.convertPoint(designer, point,
					hovered);
			MouseEvent mEvent = SwingUtilities.convertMouseEvent(designer, e,
					hovered);
			boolean should_continue = adapter.widgetPressed(mEvent);
			if (!should_continue) {
				designer.repaint();
				return;
			}
			if (adapter.isRoot()) {
				process_root_pressed(e);
			} else if (adapter.isSelected()) {
				int loc = adapter.getCursorLocation(hotspot);
				if (loc != WidgetAdapter.INNER && adapter.isResizable()) {
					process_bean_resize(e);
				} else if (loc == WidgetAdapter.INNER && adapter.isMoveable()) {
					process_bean_move(e);
				} else {
					dragging_event = null;
					currentAdapters = null;
					state = STATE_MOUSE_DRAGGING;
					designer.repaint();
				}
			} else {
				if (!e.isControlDown())
					designer.clearSelection();
				adapter.setSelected(true);
				adapter.changeNotify();
				dragging_event = null;
				currentAdapters = null;
				state = STATE_MOUSE_DRAGGING;
				designer.repaint();
				glassPlane.dispatchEvent(e);
			}
		} else {
			if (!e.isControlDown()) {
				designer.clearSelection();
				designer.publishSelection();
			}
			dragging_event = null;
			currentAdapters = null;
			state = STATE_MOUSE_DRAGGING;
			designer.repaint();
		}
	}

	private void process_bean_move(MouseEvent e) {
		currentAdapters = designer.getSelectedWidgets();
		Point point = e.getPoint();
		for (WidgetAdapter hoveredAdapter : currentAdapters) {
			Component hovered = hoveredAdapter.getWidget();
			Point relp = SwingUtilities.convertPoint(designer, point, hovered);
			hoveredAdapter.setHotspotPoint(relp);
			dragging_event = e;
		}
		state = STATE_BEAN_TOBE_HOVER;
	}

	private void process_bean_resize(MouseEvent e) {
		if (designer.getSelectedComponents().size() != 1)
			return;
		Point point = e.getPoint();
		Component hovered = designer.componentAt(point,
				WidgetAdapter.ADHERE_PAD);
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(hovered);
		dragging_event = e;
		currentAdapters = new ArrayList<WidgetAdapter>();
		currentAdapters.add(adapter);
		Point rel = SwingUtilities.convertPoint(designer, point, hovered);
		int loc = adapter.getCursorLocation(rel);
		switch (loc) {
		case WidgetAdapter.RIGHT:
			state = STATE_BEAN_TOBE_RESIZED_RIGHT;
			break;
		case WidgetAdapter.RIGHT_BOTTOM:
			state = STATE_BEAN_TOBE_RESIZED_RIGHT_BOTTOM;
			break;
		case WidgetAdapter.BOTTOM:
			state = STATE_BEAN_TOBE_RESIZED_BOTTOM;
			break;
		case WidgetAdapter.LEFT_TOP:
			state = STATE_BEAN_TOBE_RESIZED_LEFT_TOP;
			break;
		case WidgetAdapter.LEFT:
			state = STATE_BEAN_TOBE_RESIZED_LEFT;
			break;
		case WidgetAdapter.TOP:
			state = STATE_BEAN_TOBE_RESIZED_TOP;
			break;
		case WidgetAdapter.LEFT_BOTTOM:
			state = STATE_BEAN_TOBE_RESIZED_LEFT_BOTTOM;
			break;
		case WidgetAdapter.RIGHT_TOP:
			state = STATE_BEAN_TOBE_RESIZED_RIGHT_TOP;
			break;
		}
	}

	private void process_root_pressed(MouseEvent e) {
		Point point = e.getPoint();
		Component hovered = designer.componentAt(point,
				WidgetAdapter.ADHERE_PAD);
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(hovered);
		Point rel = SwingUtilities.convertPoint(designer, point, hovered);
		int loc = adapter.getCursorLocation(rel);
		dragging_event = e;
		currentAdapters = new ArrayList<WidgetAdapter>();
		currentAdapters.add(adapter);
		switch (loc) {
		case WidgetAdapter.RIGHT:
			state = STATE_ROOT_RESIZE_RIGHT;
			break;
		case WidgetAdapter.RIGHT_BOTTOM:
			state = STATE_ROOT_RESIZE_RIGHT_BOTTOM;
			break;
		case WidgetAdapter.BOTTOM:
			state = STATE_ROOT_RESIZE_BOTTOM;
			break;
		case WidgetAdapter.INNER:
			if (!e.isControlDown())
				glassPlane.getDesigner().clearSelection();
			adapter.setSelected(true);
			adapter.changeNotify();
			state = STATE_SELECTION;
			break;
		default:
			if (!e.isControlDown())
				glassPlane.getDesigner().clearSelection();
			state = STATE_SELECTION;
			break;
		}
	}

	public void mousePressed(MouseEvent e) {
		if (!isAddingState() && (currentEditor == null || stopEditing())) {
			mouse_pressed(e);
		} else if (currentEditor != null) {
			stopEditing();
		}
		designer.repaint();
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() > 1 && !isAddingState() && stopEditing()) {
			Point point = e.getPoint();
			Component hovered = designer.componentAt(point,
					WidgetAdapter.ADHERE_PAD);
			if (hovered != null) {
				Point loc = SwingUtilities.convertPoint(designer, point,
						hovered);
				startEditComponent(hovered, loc);
			}
		}
	}

	boolean editComponent(Component hovered) {
		if (stopEditing()) {
			Point p = new Point(hovered.getWidth() / 2, hovered.getHeight() / 2);
			startEditComponent(hovered, p);
			return true;
		} else
			return false;
	}

	private void startEditComponent(Component hovered, Point loc) {
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(hovered);
		IEditor iEditor = adapter.getEditorAt(loc.x, loc.y);
		if (iEditor != null) {
			iEditor.setFont(adapter.getWidget().getFont());
			iEditor.setValue(adapter.getWidgetValue());
			iEditor.addChangeListener(this);
			Rectangle bounds = adapter.getEditorBounds(loc.x, loc.y);
			if (adapter.isRoot())
				bounds = SwingUtilities.convertRectangle(hovered, bounds,
						designer);
			else if (((CompositeAdapter) adapter.getParentAdapter())
					.isEnclosingContainer())
				bounds = SwingUtilities.convertRectangle(
						((CompositeAdapter) adapter.getParentAdapter())
								.getWidget(), bounds, designer);
			else
				bounds = SwingUtilities.convertRectangle(hovered, bounds,
						designer);
			Component comp = iEditor.getComponent();
			Font f = adapter.getWidget().getFont();
			if (f != null) {
				comp.setFont(f);
			}
			comp.setBounds(bounds);
			comp.doLayout();
			glassPlane.add(comp);
			comp.addFocusListener(this);
			glassPlane.validate();
			glassPlane.repaint();
			comp.repaint();
			iEditor.setFocus();
			currentEditor = new EditorAdapter(adapter, iEditor);
		}
	}

	class EditorAdapter {
		private IEditor iEditor;
		private WidgetAdapter adapter;

		public EditorAdapter(WidgetAdapter adapter, IEditor iEditor) {
			this.adapter = adapter;
			this.iEditor = iEditor;
		}

		public IEditor getEditor() {
			return iEditor;
		}

		public WidgetAdapter getAdapter() {
			return adapter;
		}
	}

	private EditorAdapter currentEditor;
	private boolean stoppingEditing;

	private boolean _stopEditing(boolean silence) {
		if (currentEditor != null) {
			stoppingEditing = true;
			WidgetAdapter adapter = currentEditor.getAdapter();
			IEditor iEditor = currentEditor.getEditor();
			try {
				iEditor.validateValue();
				Object newValue = iEditor.getValue();
				if (adapter.isWidgetValueChanged(newValue)) {
					IUndoableOperation operation = new SetWidgetValueOperation(
							adapter, newValue);
					Shell shell = designer.getShell();
					IOperationHistory operationHistory = PlatformUI
							.getWorkbench().getOperationSupport()
							.getOperationHistory();
					operation.addContext(adapter.getUndoContext());
					operationHistory.execute(operation, null,
							new ShellAdaptable(shell));
					adapter.setDirty(true);
					CompositeAdapter container = (CompositeAdapter) adapter
							.getParentAdapter();
					if (container != null)
						container.adjustLayout(adapter.getWidget());
					if (adapter.isSelected()) {
						designer.publishSelection();
					}
				}
				iEditor.removeChangeListener(this);
				iEditor.getComponent().removeFocusListener(this);
				iEditor.getComponent().transferFocus();
				glassPlane.remove(iEditor.getComponent());
				glassPlane.validate();
				currentEditor = null;
				return true;
			} catch (Exception e) {
				if (silence) {
					iEditor.removeChangeListener(this);
					iEditor.getComponent().removeFocusListener(this);
					glassPlane.remove(iEditor.getComponent());
					glassPlane.validate();
					currentEditor = null;
				} else {
					final Shell shell = designer.getShell();
					final String message = e.getMessage();
					shell.getDisplay().syncExec(new Runnable() {
						@Override
						public void run() {
							MessageDialog.openError(shell, Messages.GLASS_TARGET_VALIDATION_ERROR,
									message);
						}
					});
					iEditor.getComponent().requestFocus();
				}
				return false;
			} finally {
				stoppingEditing = false;
			}
		} else
			return true;
	}

	public boolean stopEditing() {
		return _stopEditing(false);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				stopEditing();
			}
		});
	}

	public void mouseReleased(MouseEvent e) {
		if (isAddingState()) {
			drop(e.getPoint());
		} else if (state == STATE_SELECTION) {
			Rectangle rect = getSelBounds(dragging_event, e);
			designer.selectWidgets(rect);
			glassPlane.setSelectionRegion(null);
			dragging_event = null;
			currentAdapters = null;
			state = STATE_MOUSE_HOVER;
		} else if (state == STATE_BEAN_TOBE_HOVER) {
			Point point = e.getPoint();
			Component hovered = designer.componentAt(point,
					WidgetAdapter.ADHERE_PAD);
			assert hovered != null;
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(hovered);
			if (!e.isControlDown() && !e.isPopupTrigger()) {
				designer.clearSelection();
			}
			adapter.setSelected(true);
			adapter.changeNotify();
			dragging_event = null;
			currentAdapters = null;
			state = STATE_MOUSE_HOVER;
		} else {
			dragging_event = null;
			currentAdapters = null;
			state = STATE_MOUSE_HOVER;
		}
		hoveredAdapter = null;
		lastParent = null;
		lastConstraints = null;
		designer.repaint();
	}

	public void mouseDragged(MouseEvent e) {
		if (isAddingState()) {
			dragOver(e.getPoint());
		} else if (state == STATE_ROOT_RESIZE_RIGHT) {
			Component root = glassPlane.getDesigner().getRootWidget();
			Rectangle bounds = root.getBounds();
			boolean changed = false;
			int w = e.getX() - bounds.x;
			if (w > 0) {
				bounds.width = w;
				changed = true;
			}
			if (changed) {
				root.setBounds(bounds);
				WidgetAdapter.getWidgetAdapter(root).setDirty(true);
				designer.validateContent();
			}
		} else if (state == STATE_ROOT_RESIZE_RIGHT_BOTTOM) {
			Component root = glassPlane.getDesigner().getRootWidget();
			Rectangle bounds = root.getBounds();
			boolean changed = false;
			int w = e.getX() - bounds.x;
			if (w > 0) {
				changed = true;
				bounds.width = w;
			}
			int h = e.getY() - bounds.y;
			if (h > 0) {
				changed = true;
				bounds.height = h;
			}
			if (changed) {
				root.setBounds(bounds);
				WidgetAdapter.getWidgetAdapter(root).setDirty(true);
				designer.validateContent();
			}
		} else if (state == STATE_ROOT_RESIZE_BOTTOM) {
			Component root = glassPlane.getDesigner().getRootWidget();
			Rectangle bounds = root.getBounds();
			boolean changed = false;
			int h = e.getY() - bounds.y;
			if (h > 0) {
				changed = true;
				bounds.height = h;
			}
			if (changed) {
				root.setBounds(bounds);
				WidgetAdapter.getWidgetAdapter(root).setDirty(true);
				designer.validateContent();
			}
		} else if (state == STATE_SELECTION) {
			Rectangle rect = getSelBounds(dragging_event, e);
			glassPlane.setSelectionRegion(rect);
		} else if (isSameParent()) {
			if (isTobeDnd() && isDndReady(e)) {
				TransferHandler handler = glassPlane.getTransferHandler();
				handler.exportAsDrag(glassPlane, dragging_event,
						TransferHandler.COPY);
				WhiteBoard.setSelectedWidget(currentAdapters);
				if (state == STATE_BEAN_TOBE_HOVER) {
					setMascotLocation(e.getPoint());
					state = STATE_BEAN_HOVER;
				} else {
					Point zerop = new Point(0, 0);
					Point locp = SwingUtilities.convertPoint(currentAdapters
							.get(0).getWidget(), zerop, designer);
					for (WidgetAdapter a : currentAdapters) {
						a.setHotspotPoint(zerop);
					}
					setMascotLocation(locp);
					if (state == STATE_BEAN_TOBE_RESIZED_RIGHT_BOTTOM) {
						state = STATE_BEAN_RESIZE_RIGHT_BOTTOM;
					} else if (state == STATE_BEAN_TOBE_RESIZED_BOTTOM) {
						state = STATE_BEAN_RESIZE_BOTTOM;
					} else if (state == STATE_BEAN_TOBE_RESIZED_LEFT_BOTTOM) {
						state = STATE_BEAN_RESIZE_LEFT_BOTTOM;
					} else if (state == STATE_BEAN_TOBE_RESIZED_LEFT) {
						state = STATE_BEAN_RESIZE_LEFT;
					} else if (state == STATE_BEAN_TOBE_RESIZED_LEFT_TOP) {
						state = STATE_BEAN_RESIZE_LEFT_TOP;
					} else if (state == STATE_BEAN_TOBE_RESIZED_TOP) {
						state = STATE_BEAN_RESIZE_TOP;
					} else if (state == STATE_BEAN_TOBE_RESIZED_RIGHT_TOP) {
						state = STATE_BEAN_RESIZE_RIGHT_TOP;
					} else if (state == STATE_BEAN_TOBE_RESIZED_RIGHT) {
						state = STATE_BEAN_RESIZE_RIGHT;
					}
				}
				CompositeAdapter parentAdapter = (CompositeAdapter) currentAdapters
						.get(0).getParentAdapter();
				if (parentAdapter.isViewContainer()) {
					currentAdapters = new ArrayList<WidgetAdapter>();
					currentAdapters.add(parentAdapter);
					parentAdapter = (CompositeAdapter) parentAdapter
							.getParentAdapter();
				}
				lastConstraints = new ArrayList<Object>();
				for (WidgetAdapter a : currentAdapters) {
					lastConstraints.add(parentAdapter.getChildConstraints(a
							.getWidget()));
					parentAdapter.removeChild(a.getWidget());
				}
				currentAdapters = new ArrayList<WidgetAdapter>();
				currentAdapters.add(parentAdapter);
				lastParent = parentAdapter;
				dragging_event = null;
			}
		} else {
			state = STATE_BEAN_HOVER;
		}
	}

	private CompositeAdapter lastParent;
	private List<Object> lastConstraints;

	private boolean isDndReady(MouseEvent e) {
		return e.getPoint().distance(dragging_event.getPoint()) > DND_THRESHOLD;
	}

	private boolean isSameParent() {
		if (currentAdapters != null) {
			WidgetAdapter parent = null;
			for (WidgetAdapter adapter : currentAdapters) {
				if (adapter.isRoot()) {
					return false;
				}
				if (parent == null) {
					parent = adapter.getParentAdapter();
				} else if (parent != adapter.getParentAdapter()) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isTobeDnd() {
		if (state == STATE_BEAN_TOBE_HOVER) {
			return true;
		} else if (state == STATE_BEAN_TOBE_RESIZED_RIGHT_BOTTOM) {
			return true;
		} else if (state == STATE_BEAN_TOBE_RESIZED_BOTTOM) {
			return true;
		} else if (state == STATE_BEAN_TOBE_RESIZED_LEFT_BOTTOM) {
			return true;
		} else if (state == STATE_BEAN_TOBE_RESIZED_LEFT) {
			return true;
		} else if (state == STATE_BEAN_TOBE_RESIZED_LEFT_TOP) {
			return true;
		} else if (state == STATE_BEAN_TOBE_RESIZED_TOP) {
			return true;
		} else if (state == STATE_BEAN_TOBE_RESIZED_RIGHT_TOP) {
			return true;
		} else if (state == STATE_BEAN_TOBE_RESIZED_RIGHT) {
			return true;
		}
		return false;
	}

	private Rectangle getSelBounds(MouseEvent e1, MouseEvent e2) {
		return getSelBounds(e1.getPoint(), e2.getPoint());
	}

	private Rectangle getSelBounds(Point p1, Point p2) {
		int minx = Math.min(p1.x, p2.x);
		int miny = Math.min(p1.y, p2.y);
		int maxx = Math.max(p1.x, p2.x);
		int maxy = Math.max(p1.y, p2.y);
		int w = maxx - minx;
		int h = maxy - miny;
		return new Rectangle(minx, miny, w, h);
	}

	public void mouseMoved(MouseEvent e) {
		if (isAddingState()) {
			dragOver(e.getPoint());
		} else {
			moveOver(e.getPoint());
		}
	}

	private static int getCursorType(int loc) {
		switch (loc) {
		case WidgetAdapter.OUTER:
			return Cursor.DEFAULT_CURSOR;
		case WidgetAdapter.INNER:
			return Cursor.DEFAULT_CURSOR;
		case WidgetAdapter.LEFT_TOP:
			return Cursor.NW_RESIZE_CURSOR;
		case WidgetAdapter.TOP:
			return Cursor.N_RESIZE_CURSOR;
		case WidgetAdapter.RIGHT_TOP:
			return Cursor.NE_RESIZE_CURSOR;
		case WidgetAdapter.RIGHT:
			return Cursor.E_RESIZE_CURSOR;
		case WidgetAdapter.RIGHT_BOTTOM:
			return Cursor.SE_RESIZE_CURSOR;
		case WidgetAdapter.BOTTOM:
			return Cursor.S_RESIZE_CURSOR;
		case WidgetAdapter.LEFT_BOTTOM:
			return Cursor.SW_RESIZE_CURSOR;
		case WidgetAdapter.LEFT:
			return Cursor.W_RESIZE_CURSOR;
		}
		return Cursor.DEFAULT_CURSOR;
	}

	private void moveOver(Point point) {
		Component hovered = designer.componentAt(point,
				WidgetAdapter.ADHERE_PAD);
		if (hovered != null) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(hovered);
			if (adapter != null) {
				if (adapter.isRoot()) {
					Point rel = SwingUtilities.convertPoint(designer, point,
							hovered);
					int loc = adapter.getCursorLocation(rel);
					switch (loc) {
					case WidgetAdapter.RIGHT:
					case WidgetAdapter.RIGHT_BOTTOM:
					case WidgetAdapter.BOTTOM:
						int type = getCursorType(loc);
						glassPlane.setGestureCursor(type);
						break;
					case WidgetAdapter.LEFT_TOP:
					case WidgetAdapter.LEFT:
					case WidgetAdapter.TOP:
					case WidgetAdapter.LEFT_BOTTOM:
					case WidgetAdapter.RIGHT_TOP:
					case WidgetAdapter.INNER:
					case WidgetAdapter.OUTER:
						glassPlane.setGestureCursor(Cursor.DEFAULT_CURSOR);
						break;
					}
				} else if (adapter.isSelected()
						&& designer.getSelectedComponents().size() == 1) {
					Point rel = SwingUtilities.convertPoint(designer, point,
							hovered);
					int loc = adapter.getCursorLocation(rel);
					switch (loc) {
					case WidgetAdapter.RIGHT:
					case WidgetAdapter.RIGHT_BOTTOM:
					case WidgetAdapter.BOTTOM:
					case WidgetAdapter.LEFT_TOP:
					case WidgetAdapter.LEFT:
					case WidgetAdapter.TOP:
					case WidgetAdapter.LEFT_BOTTOM:
					case WidgetAdapter.RIGHT_TOP:
						if (adapter.isResizable()) {
							int type = getCursorType(loc);
							glassPlane.setGestureCursor(type);
						} else {
							glassPlane.setGestureCursor(Cursor.DEFAULT_CURSOR);
						}
						break;
					case WidgetAdapter.INNER:
						if (adapter.isMoveable())
							glassPlane.setGestureCursor(Cursor.HAND_CURSOR);
						else
							glassPlane.setGestureCursor(Cursor.DEFAULT_CURSOR);
						break;
					case WidgetAdapter.OUTER:
						glassPlane.setGestureCursor(Cursor.DEFAULT_CURSOR);
						break;
					}
				} else {
					glassPlane.setGestureCursor(Cursor.DEFAULT_CURSOR);
				}
			} else {
				glassPlane.setGestureCursor(Cursor.DEFAULT_CURSOR);
			}
		} else {
			glassPlane.setGestureCursor(Cursor.DEFAULT_CURSOR);
		}
	}

	public void mouseEntered(MouseEvent e) {
		if (isAddingState()) {
			state = STATE_BEAN_HOVER;
		} else {
			state = STATE_MOUSE_HOVER;
		}
	}

	public void mouseExited(MouseEvent e) {
		glassPlane.setHotspotPoint(null);
		glassPlane.repaint();
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		e.setSource(designer);
		designer.dispatchEvent(e);
	}

	private boolean isAddingState() {
		return WhiteBoard.getSelectedWidget() != null;
	}

	private void setMascotLocation(Point p) {
		glassPlane.setHotspotPoint(p);
		glassPlane.repaint();
	}

	boolean isWidgetEditing() {
		return currentEditor != null;
	}

	@Override
	public void focusGained(FocusEvent e) {
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (!stoppingEditing) {
			if (e.getOppositeComponent() == null) {
				_stopEditing(true);
			} else {
				_stopEditing(false);
			}
		}
	}
}
