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
import java.awt.Dimension;
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
import org.dyno.visual.swing.base.Azimuth;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IDesignOperation;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.IEditorAdapter;
import org.dyno.visual.swing.plugin.spi.IWidgetListener;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetEvent;
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
public class GlassTarget extends DropTarget implements MouseInputListener, MouseWheelListener, Azimuth, ChangeListener, FocusListener {
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

	CompositeAdapter getHoveredAdapter() {
		if (hoveredAdapter == null)
			return null;
		if (hoveredAdapter instanceof CompositeAdapter)
			return (CompositeAdapter) hoveredAdapter;
		else
			return hoveredAdapter.getParentAdapter();
	}

	private void dragOver(Point p) {
		if (state == STATE_BEAN_HOVER) {
			Component hovered = designer.componentAt(p, 0);
			if (hovered != null) {
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(hovered);
				if (adapter != null) {
					if (!(adapter instanceof CompositeAdapter)) {
						adapter = adapter.getParentAdapter();
					}
					CompositeAdapter compositeAdapter = (CompositeAdapter) adapter;
					if (hoveredAdapter != compositeAdapter) {
						if (hoveredAdapter != null) {
							IDesignOperation operation = (IDesignOperation) hoveredAdapter.getAdapter(IDesignOperation.class);
							if (operation != null)
								operation.dragExit(hoveredAdapter.convertToLocal(p));
						}
						hoveredAdapter = compositeAdapter;
						IDesignOperation operation = (IDesignOperation) hoveredAdapter.getAdapter(IDesignOperation.class);
						if (operation != null)
							operation.dragEnter(hoveredAdapter.convertToLocal(p));
					} else if (compositeAdapter != null) {
						hoveredAdapter = compositeAdapter;
						IDesignOperation operation = (IDesignOperation) hoveredAdapter.getAdapter(IDesignOperation.class);
						if (operation != null) {
							operation.dragOver(hoveredAdapter.convertToLocal(p));
						}
					}
				}
			} else {
				if (hoveredAdapter != null) {
					IDesignOperation operation = (IDesignOperation) hoveredAdapter.getAdapter(IDesignOperation.class);
					if (operation != null) {
						operation.dragExit(hoveredAdapter.convertToLocal(p));
					}
					hoveredAdapter = null;
				}
				glassPlane.setHotspotPoint(p);
			}
		} else if (currentAdapters != null) {
			hoveredAdapter = currentAdapters.get(0);
			IDesignOperation operation = (IDesignOperation) hoveredAdapter.getAdapter(IDesignOperation.class);
			if (operation != null) {
				operation.dragOver(hoveredAdapter.convertToLocal(p));
			}
		}
		glassPlane.repaint();
	}

	@Override
	public synchronized void drop(DropTargetDropEvent dtde) {
		drop(dtde.getLocation(), false);
	}

	private void drop(Point p, boolean shift) {
		List<IWidgetListener> widgetListeners = org.dyno.visual.swing.base.ExtensionRegistry.getWidgetListeners();
		if (state == STATE_BEAN_HOVER) {
			Component hovered = designer.componentAt(p, 0);
			if (hovered != null) {
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(hovered);
				if (!(adapter instanceof CompositeAdapter)) {
					adapter = adapter.getParentAdapter();
				}
				CompositeAdapter compositeAdapter = (CompositeAdapter) adapter;
				hoveredAdapter = compositeAdapter;
				IDesignOperation design = (IDesignOperation) hoveredAdapter.getAdapter(IDesignOperation.class);
				if (design != null && design.drop(compositeAdapter.convertToLocal(p))) {
					if (lastParent != null) {
						IOperationHistory operationHistory = PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
						List<Component> children = new ArrayList<Component>();
						List<Object> new_constraints = new ArrayList<Object>();
						for (WidgetAdapter wa : designer.getSelectedWidget()) {
							Component child = wa.getParentContainer();
							children.add(child);
							new_constraints.add(compositeAdapter.getChildConstraints(child));
						}
						IUndoableOperation operation = new MoveResizeOperation(lastParent, compositeAdapter, children, lastConstraints, new_constraints);
						operation.addContext(designer.getUndoContext());
						try {
							operationHistory.execute(operation, null, null);
						} catch (ExecutionException e) {
							VisualSwingPlugin.getLogger().error(e);
						}
						for (WidgetAdapter wa : designer.getSelectedWidget()) {
							WidgetEvent we = new WidgetEvent(lastParent, compositeAdapter, wa);
							for (IWidgetListener listener : widgetListeners) {
								listener.widgetMoved(we);
							}
						}
					} else {
						for (WidgetAdapter wa : designer.getSelectedWidget()) {
							WidgetEvent we = new WidgetEvent(compositeAdapter, wa);
							for (IWidgetListener listener : widgetListeners) {
								listener.widgetAdded(we);
							}
						}
					}
					designer.fireDirty();
					adapter.addNotify();
				} else {
					if (lastParent != null) {
						List<WidgetAdapter> selectedWidget = designer.getSelectedWidget();
						for (int i = 0; i < selectedWidget.size(); i++) {
							WidgetAdapter wa = selectedWidget.get(i);
							Object constraints = lastConstraints.get(i);
							lastParent.addChildByConstraints(wa.getWidget(), constraints);
						}
					}
				}
			} else {
				glassPlane.setHotspotPoint(null);
				if (lastParent != null) {
					IOperationHistory operationHistory = PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
					List<Component> children = new ArrayList<Component>();
					for (WidgetAdapter wa : designer.getSelectedWidget()) {
						children.add(wa.getParentContainer());
					}
					IUndoableOperation operation = new DragDropOperation(lastParent, children, lastConstraints);
					operation.addContext(designer.getUndoContext());
					try {
						operationHistory.execute(operation, null, null);
					} catch (ExecutionException e) {
						VisualSwingPlugin.getLogger().error(e);
					}
					lastParent.setDirty(true);
					designer.fireDirty();
					for (WidgetAdapter wa : designer.getSelectedWidget()) {
						WidgetEvent we = new WidgetEvent(lastParent, wa);
						for (IWidgetListener listener : widgetListeners) {
							listener.widgetRemoved(we);
						}
					}
				}
			}
		} else if (currentAdapters != null) {
			WidgetAdapter adapter = currentAdapters.get(0);
			hoveredAdapter = adapter;
			IDesignOperation design = (IDesignOperation) hoveredAdapter.getAdapter(IDesignOperation.class);
			if (design != null && design.drop(adapter.convertToLocal(p))) {
				if (lastParent != null) {
					IOperationHistory operationHistory = PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
					List<Component> children = new ArrayList<Component>();
					List<Object> new_constraints = new ArrayList<Object>();
					for (WidgetAdapter wa : designer.getSelectedWidget()) {
						Component child = wa.getParentContainer();
						children.add(child);
						new_constraints.add(((CompositeAdapter) adapter).getChildConstraints(child));
					}
					IUndoableOperation operation = new MoveResizeOperation(lastParent, ((CompositeAdapter) adapter), children, lastConstraints, new_constraints);
					operation.addContext(designer.getUndoContext());
					try {
						operationHistory.execute(operation, null, null);
					} catch (ExecutionException e) {
						VisualSwingPlugin.getLogger().error(e);
					}
					for (WidgetAdapter wa : designer.getSelectedWidget()) {
						WidgetEvent we = new WidgetEvent(lastParent, (CompositeAdapter) adapter, wa);
						for (IWidgetListener listener : widgetListeners) {
							listener.widgetResized(we);
						}
					}
				}
				adapter.changeNotify();
				adapter.setDirty(true);
			} else {
				if (lastParent != null) {
					List<WidgetAdapter> selectedWidget = designer.getSelectedWidget();
					for (int i = 0; i < selectedWidget.size(); i++) {
						WidgetAdapter wa = selectedWidget.get(i);
						Object constraints = lastConstraints.get(i);
						lastParent.addChildByConstraints(wa.getWidget(), constraints);
					}
				}
			}
		}
		if (!shift) {
			currentAdapters = null;
			state = STATE_MOUSE_HOVER;
			designer.setSelectedWidget(null);
			designer.clearToolSelection();
		} else {
			List<WidgetAdapter> adapters = designer.getSelectedWidget();
			List<WidgetAdapter> clones = new ArrayList<WidgetAdapter>();
			for (WidgetAdapter adapter : adapters) {
				WidgetAdapter clone = (WidgetAdapter) adapter.clone();
				clones.add(clone);
			}
			designer.setSelectedWidget(clones);
		}
		glassPlane.repaint();
	}

	private int getCursorLocation(WidgetAdapter adapter, Point p) {
		Component widget = adapter.getRootPane();
		int w = widget.getWidth();
		int h = widget.getHeight();
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

	private void mouse_pressed(MouseEvent e) {
		Point point = e.getPoint();
		Component hovered = designer.componentAt(point, WidgetAdapter.ADHERE_PAD);
		if (hovered != null) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(hovered);
			Point hotspot = SwingUtilities.convertPoint(designer, point, hovered);
			if (adapter.isRoot()) {
				process_root_pressed(e);
			} else if (adapter.isSelected()) {
				int loc = getCursorLocation(adapter, hotspot);
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
		currentAdapters = designer.getSelectedWidgetList();
		Point point = e.getPoint();
		for (WidgetAdapter hoveredAdapter : currentAdapters) {
			Component hovered = hoveredAdapter.getWidget();
			Point relp = SwingUtilities.convertPoint(designer, point, hovered);
			hoveredAdapter.setHotspotPoint(relp);
		}
		dragging_event = e;
		state = STATE_BEAN_TOBE_HOVER;
	}

	private void process_bean_resize(MouseEvent e) {
		if (designer.getSelectedComponents().size() != 1)
			return;
		Point point = e.getPoint();
		Component hovered = designer.componentAt(point, WidgetAdapter.ADHERE_PAD);
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(hovered);
		dragging_event = e;
		currentAdapters = new ArrayList<WidgetAdapter>();
		currentAdapters.add(adapter);
		Point rel = SwingUtilities.convertPoint(designer, point, hovered);
		int loc = getCursorLocation(adapter, rel);
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
		Component hovered = designer.componentAt(point, WidgetAdapter.ADHERE_PAD);
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(hovered);
		Point rel = SwingUtilities.convertPoint(designer, point, hovered);
		int loc = getCursorLocation(adapter, rel);
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
		if (e.isConsumed() || e.getButton() != MouseEvent.BUTTON1)
			return;
		if (!isAddingState() && (currentEditor == null || stopEditing())) {
			mouse_pressed(e);
		} else if (currentEditor != null) {
			stopEditing();
		}
		designer.repaint();
	}

	public void mouseClicked(MouseEvent e) {
		if (e.isConsumed() || e.getButton() != MouseEvent.BUTTON1)
			return;
		if (e.getClickCount() > 1 && !isAddingState() && stopEditing()) {
			Point point = e.getPoint();
			Component hovered = designer.componentAt(point, WidgetAdapter.ADHERE_PAD);
			if (hovered != null) {
				Point loc = SwingUtilities.convertPoint(designer, point, hovered);
				startEditComponent(hovered, loc);
			} else if (designer.getRoot() != null) {
				Point loc = SwingUtilities.convertPoint(designer, point, designer.getRoot());
				startEditComponent(designer.getRoot(), loc);
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
		IEditorAdapter editorAdapter = (IEditorAdapter) adapter.getAdapter(IEditorAdapter.class);
		IEditor iEditor = null;
		if (editorAdapter != null) {
			editorAdapter.setHotspot(loc);
			iEditor = editorAdapter.getEditorAt();
		}
		if (iEditor != null) {
			iEditor.setFont(adapter.getWidget().getFont());
			iEditor.setValue(editorAdapter.getWidgetValue());
			iEditor.addChangeListener(this);
			Rectangle bounds = editorAdapter.getEditorBounds();
			bounds = SwingUtilities.convertRectangle(hovered, bounds, designer);
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

	private boolean isValueChanged(Object old_value, Object new_value) {
		if (old_value == null) {
			if (new_value == null) {
				return false;
			} else {
				return true;
			}
		} else {
			if (new_value == null) {
				return true;
			} else {
				return !old_value.equals(new_value);
			}
		}
	}

	private boolean _stopEditing(boolean silence) {
		if (currentEditor != null) {
			stoppingEditing = true;
			WidgetAdapter adapter = currentEditor.getAdapter();
			IEditor iEditor = currentEditor.getEditor();
			try {
				iEditor.validateValue();
				Object newValue = iEditor.getValue();
				Object oldValue = iEditor.getOldValue();
				if (isValueChanged(oldValue, newValue)) {
					IUndoableOperation operation = new SetWidgetValueOperation(adapter, oldValue, newValue);
					IOperationHistory operationHistory = PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
					operation.addContext(adapter.getUndoContext());
					operationHistory.execute(operation, null, null);
					adapter.setDirty(true);
					CompositeAdapter container = (CompositeAdapter) adapter.getParentAdapter();
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
				designer.repaint();
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
					final String message = e.getMessage();
					final Shell shell = JavaUtil.getEclipseShell();
					shell.getDisplay().syncExec(new Runnable() {
						@Override
						public void run() {
							MessageDialog.openError(shell, Messages.GLASS_TARGET_VALIDATION_ERROR, message);
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
		if (e.isConsumed() || e.getButton() != MouseEvent.BUTTON1)
			return;
		if (isAddingState()) {
			drop(e.getPoint(), e.isShiftDown());
		} else if (state == STATE_SELECTION) {
			Rectangle rect = getSelBounds(dragging_event, e);
			designer.selectWidgets(rect);
			glassPlane.setSelectionRegion(null);
			dragging_event = null;
			currentAdapters = null;
			state = STATE_MOUSE_HOVER;
			glassPlane.setCursorType(Cursor.DEFAULT_CURSOR);
		} else if (state == STATE_BEAN_TOBE_HOVER) {
			Point point = e.getPoint();
			Component hovered = designer.componentAt(point, WidgetAdapter.ADHERE_PAD);
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
		if (e.isConsumed())
			return;
		if (isAddingState()) {
			dragOver(e.getPoint());
		} else if (state == STATE_ROOT_RESIZE_RIGHT) {
			Component root = glassPlane.getDesigner().getRootWidget();
			Rectangle bounds = root.getBounds();
			Dimension min = root.getMinimumSize();
			boolean changed = false;
			int w = e.getX() - bounds.x;
			if (w > 0) {
				if (w < min.width)
					w = min.width;
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
			Dimension min = root.getMinimumSize();
			boolean changed = false;
			int w = e.getX() - bounds.x;
			if (w > 0) {
				if (w < min.width)
					w = min.width;
				changed = true;
				bounds.width = w;
			}
			int h = e.getY() - bounds.y;
			if (h > 0) {
				if (h < min.height)
					h = min.height;
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
			Dimension min = root.getMinimumSize();
			boolean changed = false;
			int h = e.getY() - bounds.y;
			if (h > 0) {
				if (h < min.height)
					h = min.height;
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
			glassPlane.setCursorType(Cursor.CROSSHAIR_CURSOR);
		} else if (isSameParent()) {
			if (isTobeDnd() && isDndReady(e)) {
				TransferHandler handler = glassPlane.getTransferHandler();
				handler.exportAsDrag(glassPlane, dragging_event, TransferHandler.COPY);
				designer.setSelectedWidget(currentAdapters);
				if (state == STATE_BEAN_TOBE_HOVER) {
					setMascotLocation(e.getPoint());
					state = STATE_BEAN_HOVER;
				} else {
					Point zerop = new Point(0, 0);
					Point locp = SwingUtilities.convertPoint(currentAdapters.get(0).getWidget(), zerop, designer);
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
				if (!currentAdapters.isEmpty()) {
					CompositeAdapter parentAdapter = (CompositeAdapter) currentAdapters.get(0).getParentAdapter();
					if (parentAdapter.isViewContainer()) {
						currentAdapters = new ArrayList<WidgetAdapter>();
						currentAdapters.add(parentAdapter);
						parentAdapter = (CompositeAdapter) parentAdapter.getParentAdapter();
					}
					lastConstraints = new ArrayList<Object>();
					for (WidgetAdapter a : currentAdapters) {
						lastConstraints.add(parentAdapter.getChildConstraints(a.getWidget()));
						parentAdapter.removeChild(a.getWidget());
					}
					currentAdapters = new ArrayList<WidgetAdapter>();
					currentAdapters.add(parentAdapter);
					lastParent = parentAdapter;
				}
				dragging_event = null;
			}
		} else {
			state = STATE_BEAN_HOVER;
		}
	}

	private CompositeAdapter lastParent;
	private List<Object> lastConstraints;

	private boolean isDndReady(MouseEvent e) {
		return dragging_event != null && e.getPoint().distance(dragging_event.getPoint()) > DND_THRESHOLD;
	}

	private boolean isSameParent() {
		if (currentAdapters != null && !currentAdapters.isEmpty()) {
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
		if (e.isConsumed())
			return;
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
		Component hovered = designer.componentAt(point, WidgetAdapter.ADHERE_PAD);
		if (hovered != null) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(hovered);
			if (adapter != null) {
				if (adapter.isRoot()) {
					Point rel = SwingUtilities.convertPoint(designer, point, hovered);
					int loc = getCursorLocation(adapter, rel);
					switch (loc) {
					case WidgetAdapter.RIGHT:
					case WidgetAdapter.RIGHT_BOTTOM:
					case WidgetAdapter.BOTTOM:
						int type = getCursorType(loc);
						glassPlane.setCursorType(type);
						break;
					case WidgetAdapter.LEFT_TOP:
					case WidgetAdapter.LEFT:
					case WidgetAdapter.TOP:
					case WidgetAdapter.LEFT_BOTTOM:
					case WidgetAdapter.RIGHT_TOP:
					case WidgetAdapter.INNER:
					case WidgetAdapter.OUTER:
						glassPlane.setCursorType(Cursor.DEFAULT_CURSOR);
						break;
					}
				} else if (adapter.isSelected() && designer.getSelectedComponents().size() == 1) {
					Point rel = SwingUtilities.convertPoint(designer, point, hovered);
					int loc = getCursorLocation(adapter, rel);
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
							glassPlane.setCursorType(type);
						} else {
							glassPlane.setCursorType(Cursor.DEFAULT_CURSOR);
						}
						break;
					case WidgetAdapter.INNER:
						if (adapter.isMoveable())
							glassPlane.setCursorType(Cursor.HAND_CURSOR);
						else
							glassPlane.setCursorType(Cursor.DEFAULT_CURSOR);
						break;
					case WidgetAdapter.OUTER:
						glassPlane.setCursorType(Cursor.DEFAULT_CURSOR);
						break;
					}
				} else {
					glassPlane.setCursorType(Cursor.DEFAULT_CURSOR);
				}
			} else {
				glassPlane.setCursorType(Cursor.DEFAULT_CURSOR);
			}
		} else {
			glassPlane.setCursorType(Cursor.DEFAULT_CURSOR);
		}
	}

	public void mouseEntered(MouseEvent e) {
		if (e.isConsumed())
			return;
		if (isAddingState()) {
			state = STATE_BEAN_HOVER;
		} else {
			state = STATE_MOUSE_HOVER;
		}
	}

	public void mouseExited(MouseEvent e) {
		if (e.isConsumed())
			return;
		glassPlane.setHotspotPoint(null);
		glassPlane.repaint();
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.isConsumed())
			return;
		e.setSource(designer);
		designer.dispatchEvent(e);
	}

	private boolean isAddingState() {
		return designer.getSelectedWidget() != null;
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
