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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.base.EditorAction;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.MenuSelectionManager;
import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.editors.VisualSwingEditor;
import org.dyno.visual.swing.editors.actions.SetLnfAction;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IContextCustomizer;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.swt_awt.GTKAWTBridgePopupFix;
import org.dyno.visual.swing.swt_awt.Platform;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.operations.RedoActionHandler;
import org.eclipse.ui.operations.UndoActionHandler;

/**
 * 
 * VisualDesigner
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
@SuppressWarnings("unchecked")
public class VisualDesigner extends JComponent implements KeyListener {
	private static final long serialVersionUID = -8003291919574427325L;
	private Rectangle rootBounds;
	private GlassPlane glass;
	private JComponent container;
	private JComponent popupLayer;
	private Border designBorder;
	private Component root;
	private Banner banner;
	private List<WidgetAdapter> clipboard;

	private boolean lnfChanged;
	private ICompilationUnit unit;

	private VisualSwingEditor editor;
	private Composite parent;
	private IUndoContext undoContext;

	private List<WidgetAdapter> selected;

	private boolean locked;

	private NamespaceManager namespace;

	public NamespaceManager getNamespace() {
		return namespace;
	}

	public boolean isLocked() {
		return locked;
	}

	public void lock() {
		locked = true;
	}

	public void unlock() {
		locked = false;
	}

	public VisualDesigner(VisualSwingEditor editor, Composite parent) {
		this.editor = editor;
		this.parent = parent;

		this.clipboard = new ArrayList<WidgetAdapter>();
		this.selected = new ArrayList<WidgetAdapter>();

		setFocusable(true);
		setLayout(new DesignerLayout());

		glass = new GlassPlane(this);
		add(glass);
		popupLayer = new JLayeredPane();
		add(popupLayer);
		glass.addKeyListener(this);

		container = new ContainerPane();

		add(container);
		banner = new Banner();
		add(banner);
		setFocusCycleRoot(true);
		setFocusTraversalPolicy(new DesignerFocusTraversalPolicy());
		putClientProperty("popup.layer", popupLayer); //$NON-NLS-1$
	}

	private class ContainerPane extends JComponent {
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (captured != null && root != null && rootBounds != null && designBorder != null) {
				Insets insets = designBorder.getBorderInsets(this);
				Graphics clipg = g.create(insets.left, insets.top, root.getWidth(), root.getHeight());
				clipg.drawImage(captured, 0, 0, this);
				clipg.dispose();
			}
		}
	}

	public IUndoContext getUndoContext() {
		return undoContext;
	}

	public VisualSwingEditor getEditor() {
		return editor;
	}

	public String getLnfClassname() {
		return editor.getLnfClassname();
	}

	public GlassPlane getGlass() {
		return glass;
	}

	public boolean editComponent(Component hovered) {
		return glass.editComponent(hovered);
	}

	public Component getRoot() {
		return root;
	}

	public void setLnfClassname(String lnfClassname) {
		editor.setLnfClassname(lnfClassname);
		setLnfChanged(true);
	}

	public void selectWidgets(Rectangle selectionRegion) {
		if (root != null) {
			CompositeAdapter rootAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(root);
			if (_selectWidget(selectionRegion, rootAdapter)) {
				if (rootAdapter != null) {
					rootAdapter.setSelected(false);
				}
			}
			publishSelection();
			repaint();
		}
	}

	private boolean isAdapterVisible(WidgetAdapter root) {
		boolean visible = root.getRootPane().isVisible();
		if (visible) {
			if (root.isRoot())
				return true;
			else {
				WidgetAdapter adapter = root.getParentAdapter();
				if (isAdapterVisible(adapter)) {
					return ((CompositeAdapter) adapter).isChildVisible(root.getWidget());
				} else
					return false;
			}
		} else
			return false;
	}

	private boolean _selectWidget(Rectangle sel, WidgetAdapter adapter) {
		boolean selected = false;
		Component current = adapter.getWidget();
		Rectangle localBounds = SwingUtilities.getLocalBounds(current);
		Rectangle globalBounds = SwingUtilities.convertRectangle(current, localBounds, this);
		if (sel.contains(globalBounds) && isAdapterVisible(adapter)) {
			adapter.setSelected(true);
			selected = true;
		}
		if (adapter instanceof CompositeAdapter) {
			CompositeAdapter compositeAdapter = (CompositeAdapter) adapter;
			int size = compositeAdapter.getChildCount();
			for (int i = 0; i < size; i++) {
				Component child = compositeAdapter.getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
				if (childAdapter != null && _selectWidget(sel, childAdapter)) {
					selected = true;
				}
			}
		}
		return selected;
	}

	void trigPopup(final Point p, final List<Component> selected) {
		editor.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				Point dp = new Point(p);
				SwingUtilities.convertPointToScreen(dp, glass);
				showPopup(dp, selected);
			}
		});
	}

	private void fillLnfAction(MenuManager lnfMenu) {
		Collection<ILookAndFeelAdapter> lnfAdapters = ExtensionRegistry.getLnfAdapters();
		for (ILookAndFeelAdapter lnfAdapter : lnfAdapters) {
			String lnfName = lnfAdapter.getName();
			String lnfClassname = lnfAdapter.getClassname();
			IAction lnfAction = new SetLnfAction(this, lnfName, lnfClassname);
			lnfMenu.add(lnfAction);
		}
	}

	private void showPopup(Point dp, List<Component> selected) {
		MenuManager manager = new MenuManager("#EDIT"); //$NON-NLS-1$
		MenuManager lnfMenu = new MenuManager(Messages.VisualDesigner_SetLaf, "#LNF"); //$NON-NLS-2$
		fillLnfAction(lnfMenu);
		manager.add(lnfMenu);
		List<IContextCustomizer> contexts = ExtensionRegistry.getContextCustomizers();
		if (!contexts.isEmpty()) {
			manager.add(new Separator());
			WidgetAdapter rootAdapter = WidgetAdapter.getWidgetAdapter(root);
			for (IContextCustomizer customizer : contexts) {
				customizer.fillContextMenu(manager, rootAdapter, selected);
			}
		}
		manager.add(new Separator());
		IEditorSite site = editor.getEditorSite();
		manager.add(undoAction);
		manager.add(redoAction);
		manager.add(new Separator());

		IWorkbenchPage page = site.getPage();
		IWorkbenchWindow window = site.getWorkbenchWindow();
		page.getActiveEditor();
		for (EditorAction action : editor.getActions()) {
			if (action == null) {
				manager.add(new Separator());
			} else if (action.isRetargetable()) {
				IWorkbenchAction workbenchAction = action.getActionFactory().create(window);
				if (workbenchAction instanceof RetargetAction) {
					RetargetAction retargetAction = (RetargetAction) workbenchAction;
					page.addPartListener(retargetAction);
					retargetAction.partActivated(editor);
				}
				manager.add(workbenchAction);
			} else {
				manager.add(action);
			}
		}
		final Menu menu = manager.createContextMenu(parent);
		menu.setLocation(dp.x, dp.y);
		if (Platform.isGtk()) {
			Runnable r = new Runnable() {
				public void run() {
					GTKAWTBridgePopupFix.showMenu(menu);
				}
			};
			parent.getShell().getDisplay().asyncExec(r);
		} else {
			menu.setVisible(true);
		}
	}

	public void clearSelection() {
		if (root != null) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(root);
			if (adapter != null) {
				adapter.clearSelection();
			}
		}
		selected = new ArrayList<WidgetAdapter>();
	}

	public void addSelectedWidget(WidgetAdapter adapter) {
		if (!selected.contains(adapter))
			selected.add(adapter);
	}

	public void removeSelectedWidget(WidgetAdapter adapter) {
		selected.remove(adapter);
	}

	public List<WidgetAdapter> getSelectedWidgets() {
		return selected;
	}
	public List<WidgetAdapter> getSelectedWidgetList(){
		List<WidgetAdapter>adapters=new ArrayList<WidgetAdapter>();
		List<Component>children = getSelectedComponents();
		for(Component child:children){
			WidgetAdapter adapter=WidgetAdapter.getWidgetAdapter(child);
			adapters.add(adapter);
		}
		return adapters;
	}
	public List<Component> getSelectedComponents() {
		return new WidgetSelection(root);
	}

	public Component componentAt(Point p, int offset) {
		if (root != null) {
			MenuElement[] menu_selection = MenuSelectionManager.defaultManager().getSelectedPath();
			if (menu_selection != null && menu_selection.length > 0) {
				for (int i = menu_selection.length - 1; i >= 0; i--) {
					if (menu_selection[i] instanceof JPopupMenu) {
						JPopupMenu jpm = (JPopupMenu) menu_selection[i];
						if (!jpm.isShowing())
							continue;
						Rectangle b = jpm.getBounds();
						Point jpml = jpm.getLocationOnScreen();
						Point vdl = getLocationOnScreen();
						b.x = jpml.x - vdl.x;
						b.y = jpml.y - vdl.y;
						if (b.contains(p)) {
							MenuElement[] sub = jpm.getSubElements();
							for (MenuElement submenu : sub) {
								if (submenu instanceof JMenuItem) {
									JMenuItem submenuItem = (JMenuItem) submenu;
									Rectangle sb = submenuItem.getBounds();
									sb.x += b.x;
									sb.y += b.y;
									if (sb.contains(p)) {
										return submenuItem;
									}
								}
							}
						}
					}
				}
			}
			Point mp = SwingUtilities.convertPoint(this, p, root);
			if (isPointInRoot(mp, offset))
				return root;
			return _getComponentAt(root, mp, offset);
		} else
			return null;
	}

	private boolean isPointInRoot(Point p, int offset) {
		int w = root.getWidth();
		int h = root.getHeight();
		if (p.x < -offset || p.y < -offset || p.x >= w + offset || p.y >= h + offset)
			return false;
		if (p.x >= offset && p.y >= offset && p.x < w - offset && p.y < h - offset)
			return false;
		return true;
	}

	private Component _getComponentAt(Component comp, Point p, int ad) {
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(comp);
		if (adapter == null)
			return null;
		if (adapter instanceof CompositeAdapter) {
			CompositeAdapter compAdapter = (CompositeAdapter) adapter;
			if (compAdapter.isEnclosingContainer()) {
				if (compAdapter.interceptPoint(p, ad))
					return comp;
			}
			int count = compAdapter.getChildCount();
			for (int i = 0; i < count; i++) {
				Component child = compAdapter.getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
				if (child.isVisible() && childAdapter != null && childAdapter.isSelected()) {
					Point location = SwingUtilities.convertPoint(comp, p, child);
					Component jcomp = _getComponentAt(child, location, ad);
					if (jcomp != null)
						return jcomp;
				}
			}

			for (int i = 0; i < count; i++) {
				Component child = compAdapter.getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
				if (child.isVisible() && childAdapter != null && !childAdapter.isSelected()) {
					Point location = SwingUtilities.convertPoint(comp, p, child);
					Component jcomp = _getComponentAt(child, location, ad);
					if (jcomp != null)
						return jcomp;
				}
			}
		}
		CompositeAdapter compParent = adapter.getParentAdapter();
		if (compParent != null && compParent.isEnclosingContainer()) {
			Rectangle bounds = compParent.getVisibleRect(comp);
			if (bounds != null) {
				if (p.x >= bounds.x - ad && p.x <= bounds.width + ad && p.y >= bounds.y - ad && p.y <= bounds.height + ad)
					return comp;
			}
		}
		if (p.x >= -ad && p.y >= -ad && p.x < comp.getWidth() + ad && p.y < comp.getHeight() + ad)
			return comp;
		else
			return null;
	}

	private Event createEvent(int id, Object param) {
		return new Event(this, id, param);
	}

	Component getRootWidget() {
		return root;
	}

	public void resetRoot() {
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(root);
		rootBounds = adapter.getDesignBounds();
		root.setSize(rootBounds.width, rootBounds.height);
		designBorder = adapter.getDesignBorder();
		container.setBorder(designBorder);
		validateContent();
		setLnfChanged(false);
		setFocus();
	}

	public void initRootWidget(WidgetAdapter adapter) {
		if (root != null)
			remove(root);
		if (adapter != null) {
			root = adapter.getRootPane();
			Container parent = root.getParent();
			if (parent != null)
				parent.remove(root);
			undoContext = new ObjectUndoContext(root);
			IEditorSite site = editor.getEditorSite();
			undoAction = new UndoActionHandler(site, getUndoContext());
			redoAction = new RedoActionHandler(site, getUndoContext());
			IActionBars actionBars = site.getActionBars();
			actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);
			actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);
			rootBounds = adapter.getDesignBounds();
			root.setSize(rootBounds.width, rootBounds.height);
			add(root);
			root.setVisible(true);
			designBorder = adapter.getDesignBorder();
			container.setBorder(designBorder);
		} else {
			root = null;
			designBorder = null;
			rootBounds = null;
			container.setBorder(null);
		}
		validateContent();
		setLnfChanged(false);
		setFocus();
	}

	private Image captured;

	public void capture() {
		if (root != null) {
			captured = null;
			System.gc();
			captured = root.createVolatileImage(root.getWidth(), root.getHeight());
			Graphics offg = captured.getGraphics();
			root.paint(offg);
			repaint();
		}
	}

	public void clearCapture() {
		captured = null;
		repaint();
	}

	private UndoActionHandler undoAction;
	private RedoActionHandler redoAction;

	public void publishSelection() {
		WhiteBoard.sendEvent(createEvent(Event.EVENT_SELECTION, new StructuredSelection(new Object[] { new WidgetSelection(root) })));
	}

	public void showPopup(Event event) {
		Object[] param = (Object[]) event.getParameter();
		Point p = (Point) param[0];
		List<Component> hovered = (List<Component>) param[1];
		showPopup(p, hovered);
	}

	public void validateContent() {
		editor.validateContent();
	}

	public void refreshDesigner() {
		validate();
		clearCapture();
		repaint();
	}

	public void changeFocused() {
		CompositeAdapter focused = getFocused();
		glass.setFocusedComposite(focused);
		repaint();
	}

	public CompositeAdapter getFocused() {
		if (root != null) {
			WidgetSelection selected = new WidgetSelection(root);
			WidgetAdapter parent = null;
			for (int i = 0; i < selected.size(); i++) {
				Component comp = selected.get(i);
				WidgetAdapter child = WidgetAdapter.getWidgetAdapter(comp);
				WidgetAdapter par = child.getParentAdapter();
				if (parent == null) {
					parent = par;
				} else if (parent != par)
					return null;
			}
			if (parent == null)
				return null;
			else
				return (CompositeAdapter) parent;
		} else
			return null;
	}

	public boolean isWidgetEditing() {
		return glass.isWidgetEditing();
	}

	public List<WidgetAdapter> getClipboard() {
		return clipboard;
	}

	class DesignerLayout implements LayoutManager {
		public void addLayoutComponent(String name, Component comp) {
		}

		public void layoutContainer(Container parent) {
			if (root != null && rootBounds != null && designBorder != null) {
				banner.setBounds(0, 0, 0, 0);
				int w = root == null ? 0 : root.getWidth();
				w = w <= 0 ? rootBounds.width : w;
				int h = root == null ? 0 : root.getHeight();
				h = h <= 0 ? rootBounds.height : h;
				Insets insets = designBorder.getBorderInsets(parent);
				container.setBounds(rootBounds.x - insets.left, rootBounds.y - insets.top, w + insets.left + insets.right, h + insets.top + insets.bottom);
				if (root != null) {
					root.setBounds(rootBounds.x, rootBounds.y, w, h);
				}
			} else {
				int w = parent.getWidth();
				int h = parent.getHeight();
				banner.setBounds(0, 0, w, h);
			}
			int w = parent.getWidth();
			int h = parent.getHeight();
			popupLayer.setBounds(0, 0, w, h);
			glass.setBounds(0, 0, w, h);
		}

		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(rootBounds == null ? 24 : 2 * rootBounds.x, rootBounds == null ? 24 : 2 * rootBounds.y);
		}

		public Dimension preferredLayoutSize(Container parent) {
			int w = root == null ? (rootBounds == null ? 320 : rootBounds.width) : root.getWidth();
			int h = root == null ? (rootBounds == null ? 240 : rootBounds.height) : root.getHeight();
			w += rootBounds == null ? 24 : 2 * rootBounds.x;
			h += rootBounds == null ? 24 : 2 * rootBounds.y;
			Dimension prefSize = new Dimension(0, 0);
			int count = popupLayer.getComponentCount();
			for (int i = 0; i < count; i++) {
				Component comp = popupLayer.getComponent(i);
				if (comp.isVisible()) {
					Rectangle bounds = comp.getBounds();
					if (bounds.x + bounds.width > prefSize.width)
						prefSize.width = bounds.x + bounds.width;
					if (bounds.y + bounds.height > prefSize.height)
						prefSize.height = bounds.y + bounds.height;
				}
			}
			return new Dimension(prefSize.width > w ? prefSize.width : w, prefSize.height > h ? prefSize.height : h);
		}

		public void removeLayoutComponent(Component comp) {
		}
	}

	private boolean isAlignResize(int count, String id) {
		WidgetSelection selection = new WidgetSelection(root);
		if (selection.size() > count) {
			WidgetAdapter parentAdapter = null;
			for (Component selected : selection) {
				WidgetAdapter selectedAdapter = WidgetAdapter.getWidgetAdapter(selected);
				WidgetAdapter selectedParent = selectedAdapter.getParentAdapter();
				if (parentAdapter == null) {
					parentAdapter = selectedParent;
				} else if (parentAdapter != selectedParent) {
					return false;
				}
			}
			if (parentAdapter == null) {
				return false;
			} else {
				for (Component selected : selection) {
					WidgetAdapter selectedAdapter = WidgetAdapter.getWidgetAdapter(selected);
					if (!selectedAdapter.isResizable()) {
						return false;
					}
				}
				return ((CompositeAdapter) parentAdapter).isSelectionAlignResize(id);
			}
		} else {
			return false;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (isAlignResize(0, null)) {
			boolean isMoved = false;
			if (e.getKeyCode() == KeyEvent.VK_LEFT)
				isMoved = true;
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				isMoved = true;
			else if (e.getKeyCode() == KeyEvent.VK_UP)
				isMoved = true;
			else if (e.getKeyCode() == KeyEvent.VK_DOWN)
				isMoved = true;
			else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP)
				isMoved = true;
			else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
				isMoved = true;
			if (isMoved) {
				WidgetSelection selection = new WidgetSelection(root);
				Component child = selection.get(0);
				WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
				CompositeAdapter parentAdapter = (CompositeAdapter) childAdapter.getParentAdapter();
				IUndoableOperation operation = parentAdapter.doKeyPressed(e);
				if (operation != null) {
					IOperationHistory operationHistory = PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
					operation.addContext(getUndoContext());
					try {
						operationHistory.execute(operation, null, null);
					} catch (ExecutionException ex) {
						VisualSwingPlugin.getLogger().error(ex);
					}
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public boolean isDirty() {
		if (root == null)
			return false;
		WidgetAdapter rootAdapter = WidgetAdapter.getWidgetAdapter(root);
		return rootAdapter.isDirty() || rootAdapter.isRenamed();
	}

	public void fireDirty() {
		editor.fireDirty();
	}

	public void clearDirty() {
		if (root != null) {
			WidgetAdapter rootAdapter = WidgetAdapter.getWidgetAdapter(root);
			rootAdapter.clearDirty();
			fireDirty();
		}
	}

	public void setLnfChanged(boolean b) {
		lnfChanged = b;
		if (lnfChanged)
			fireDirty();
	}

	public boolean isLnfChanged() {
		return lnfChanged;
	}

	public void setFocus() {
		glass.setFocus();
	}

	public CompositeAdapter getFocusedContainer() {
		if (root == null)
			return null;
		List<Component> selected = new WidgetSelection(root);
		if (selected.isEmpty())
			return null;
		if (selected.size() == 1) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(selected.get(0));
			if (adapter.isRoot())
				return (CompositeAdapter) adapter;
			else if (adapter instanceof CompositeAdapter)
				return (CompositeAdapter) adapter;
			else
				return adapter.getParentAdapter();
		}
		WidgetAdapter parent = null;
		for (Component comp : selected) {
			WidgetAdapter compAdapter = WidgetAdapter.getWidgetAdapter(comp);
			if (parent == null) {
				if (compAdapter.isRoot())
					return null;
				parent = compAdapter.getParentAdapter();
			} else if (parent != compAdapter.getParentAdapter()) {
				return null;
			}
		}
		if (parent == null)
			return null;
		else if (parent instanceof CompositeAdapter)
			return (CompositeAdapter) parent;
		else
			return parent.getParentAdapter();
	}

	public CompositeAdapter getSelectedContainer() {
		if (root == null)
			return null;
		List<Component> selected = new WidgetSelection(root);
		if (selected.isEmpty())
			return null;
		if (selected.size() == 1) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(selected.get(0));
			if (adapter.getParentAdapter() != null)
				return adapter.getParentAdapter();
			else
				return (CompositeAdapter) adapter;
		}
		WidgetAdapter parent = null;
		for (Component comp : selected) {
			WidgetAdapter compAdapter = WidgetAdapter.getWidgetAdapter(comp);
			if (parent == null) {
				if (compAdapter.isRoot())
					return null;
				parent = compAdapter.getParentAdapter();
			} else if (parent != compAdapter.getParentAdapter()) {
				return null;
			}
		}
		if (parent == null)
			return null;
		else if (parent instanceof CompositeAdapter)
			return (CompositeAdapter) parent;
		else
			return parent.getParentAdapter();
	}

	public List<InvisibleAdapter> getInvisibles() {
		if (root == null)
			return null;
		else {
			return WidgetAdapter.getWidgetAdapter(root).getInvisibles();
		}
	}

	public ICompilationUnit getCompilationUnit() {
		return unit;
	}

	public void setCompilationUnit(ICompilationUnit unit) {
		this.unit = unit;
	}

	public void initNamespaceWithUnit(ICompilationUnit unit) {
		setCompilationUnit(unit);
		WidgetAdapter rootAdapter = WidgetAdapter.getWidgetAdapter(root);
		VSNamespaceManager vsname = new VSNamespaceManager(rootAdapter);
		try {
			IType[] types = unit.getAllTypes();
			for (IType type : types) {
				IField[] fields = type.getFields();
				for (IField field : fields) {
					vsname.addName(field.getElementName());
				}
			}
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		this.namespace = vsname;
	}

	public void setCursorType(int type) {
		glass.setCursorType(type);
	}

	public void setError(Exception e) {
		banner.setIcon(null);
		banner.setError(true);
		banner.setText(e.getMessage());
	}
	private List<WidgetAdapter> selectedWidget;

	public void setSelectedWidget(List<WidgetAdapter> adapters) {
		if (adapters == null) {
			selectedWidget = null;
		} else {
			selectedWidget = new ArrayList<WidgetAdapter>();
			for (WidgetAdapter adapter : adapters) {
				selectedWidget.add(adapter);
			}
		}
	}

	public List<WidgetAdapter> getSelectedWidget() {
		return selectedWidget;
	}

	public void clearToolSelection() {
		editor.clearToolSelection();
	}
}
