/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.editors;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.MouseInputListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class TreeModelPanel extends JLayeredPane implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JTree tree;
	private TreeGlass glass;
	private Color designFieldBackground;
	private Color designFieldBorder;

	public TreeModelPanel(JScrollPane jsp) {
		setOpaque(false);
		setLayout(new GlassLayout());
		setLayer(jsp, 0);
		add(jsp);
		jsp.setToolTipText("Adjust view");
		jsp.getHorizontalScrollBar().setToolTipText("Drag it to adjust view");
		jsp.getVerticalScrollBar().setToolTipText("Drag it to adjust view");
		TreeGlass glass = new TreeGlass();
		setLayer(glass, 1);
		add(glass);
		this.scrollPane = jsp;
		this.tree = (JTree) jsp.getViewport().getView();
		this.tree.setToolTipText("Click cell to edit");
		this.glass = glass;
		this.glass.addActionListener(this);
		designFieldBackground = Color.orange.brighter();
		designFieldBorder = Color.green.darker();
	}

	private static final int PRESSED = 0;
	private static final int RELEASED = 1;

	private static final int NO_OP = -1;
	private static final int DELETE_NODE = 0;
	private static final int EDIT_NODE = 1;
	private static final int ADD_NODE = 2;

	private static Icon DELETE_NODE_ICON;
	private static Icon EDIT_NODE_ICON;
	private static Icon ADD_NODE_ICON;
	static {
		ADD_NODE_ICON = new ImageIcon(TableModelPanel.class.getResource("/icons/insert.png"));
		DELETE_NODE_ICON = new ImageIcon(TableModelPanel.class.getResource("/icons/delete.png"));
		EDIT_NODE_ICON = new ImageIcon(TableModelPanel.class.getResource("/icons/edit_text.png"));
	}

	class TreeGlass extends JComponent implements MouseInputListener, MouseWheelListener {
		private static final long serialVersionUID = 1L;
		private int status;
		private int icon = -1;
		private int row = -1;
		private List<ActionListener> listeners;

		public TreeGlass() {
			listeners = new ArrayList<ActionListener>();
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
		}

		public void addActionListener(ActionListener l) {
			if (!listeners.contains(l))
				listeners.add(l);
		}

		public void removeActionListener(ActionListener l) {
			if (listeners.contains(l))
				listeners.remove(l);
		}

		private void fireActionPerformed() {
			ActionEvent event = new ActionEvent(this, 0, "action");
			for (ActionListener l : listeners)
				l.actionPerformed(event);
		}

		private Icon getIcon() {
			switch (icon) {
			case NO_OP:
				return null;
			case ADD_NODE:
				return ADD_NODE_ICON;
			case EDIT_NODE:
				return EDIT_NODE_ICON;
			case DELETE_NODE:
				return DELETE_NODE_ICON;
			}
			return null;
		}

		private int getXoffset() {
			switch (icon) {
			case ADD_NODE:
				return 32;
			case EDIT_NODE:
				return 16;
			case DELETE_NODE:
				return 0;
			}
			return 0;
		}

		@Override
		protected void paintComponent(Graphics g) {
			paintBackground(g);
			if (row != -1) {
				TreePath path = tree.getPathForRow(row);
				Rectangle rect = tree.getPathBounds(path);
				if (rect != null) {
					rect = SwingUtilities.convertRectangle(tree, rect, TreeModelPanel.this);
					int x = rect.x;
					int y = rect.y;
					int w = 16;
					int h = 16;
					if (icon != NO_OP) {
						Icon iCon = getIcon();
						if (iCon != null) {
							x += getXoffset();
							w = iCon.getIconWidth();
							h = iCon.getIconHeight();
							y += (rect.height - h) / 2;
							iCon.paintIcon(this, g, x, y);
						}
					}
					g.setColor(Color.LIGHT_GRAY);
					if (status == PRESSED) {
						g.draw3DRect(x, y, w, h, false);
					} else if (status == RELEASED) {
						g.draw3DRect(x, y, w, h, true);
					}
				}
			}
			g.setColor(designFieldBorder);
			int width = getWidth();
			int height = getHeight();
			g.drawLine(0, 0, 0, height - 1);
			g.drawLine(0, 0, width - 1, 0);
			g.drawLine(0, height - 1, width - 1, height - 1);
			g.drawLine(width - 1, 0, width - 1, height - 1);
		}

		private void paintBackground(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			java.awt.Composite old = g2d.getComposite();
			AlphaComposite pha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f);
			g2d.setComposite(pha);
			g2d.setColor(designFieldBackground);
			int width = getWidth();
			int height = getHeight();
			g2d.fillRect(0, 0, width, height);
			g2d.setComposite(old);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
			row = -1;
			repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			Point p = SwingUtilities.convertPoint(TreeModelPanel.this, e.getPoint(), tree);
			TreePath path = tree.getClosestPathForLocation(p.x, p.y);
			int r = tree.getRowForLocation(p.x, p.y);
			Rectangle rect = tree.getPathBounds(path);
			if (p.x >= rect.x && p.x < rect.x + 48) {
				icon = (p.x - rect.x) / 16;
				Icon iCon = getIcon();
				if (iCon != null) {
					int x = rect.x + icon * 16;
					if (p.x >= x && p.x < x + iCon.getIconWidth()) {
						row = r;
						status = PRESSED;
						repaint();
						setToolTipText(getCurrentText());
						return;
					}
				}
			}
			setToolTipText(null);
			row = -1;
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Point p = SwingUtilities.convertPoint(TreeModelPanel.this, e.getPoint(), tree);
			TreePath path = tree.getClosestPathForLocation(p.x, p.y);
			int r = tree.getRowForLocation(p.x, p.y);
			Rectangle rect = tree.getPathBounds(path);
			if (p.x >= rect.x && p.x < rect.x + 48) {
				icon = (p.x - rect.x) / 16;
				Icon iCon = getIcon();
				if (iCon != null) {
					int x = rect.x + icon * 16;
					if (p.x >= x && p.x < x + iCon.getIconWidth()) {
						row = r;
						status = RELEASED;
						repaint();
						setToolTipText(getCurrentText());
						fireActionPerformed();
						return;
					}
				}
			}
			setToolTipText(null);
			row = -1;
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Point p = SwingUtilities.convertPoint(TreeModelPanel.this, e.getPoint(), tree);
			TreePath path = tree.getClosestPathForLocation(p.x, p.y);
			int r = tree.getRowForLocation(p.x, p.y);
			Rectangle rect = tree.getPathBounds(path);
			if (p.x >= rect.x && p.x < rect.x + 48) {
				int lastIcon = icon;
				icon = (p.x - rect.x) / 16;
				Icon iCon = getIcon();
				if (iCon != null) {
					int x = rect.x + icon * 16;
					if (p.x >= x && p.x < x + iCon.getIconWidth()) {
						if (row != r || lastIcon != icon) {
							row = r;
							status = RELEASED;
							repaint();
							setToolTipText(getCurrentText());
						}
						return;
					}
				}
			}
			setToolTipText(null);
			row = -1;
			repaint();
		}

		private String getCurrentText() {
			switch (icon) {
			case NO_OP:
				return null;
			case ADD_NODE:
				return "Add child node";
			case EDIT_NODE:
				return "Edit node";
			case DELETE_NODE:
				return "Delete node";
			}
			return null;
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
		}
	}

	class GlassLayout implements LayoutManager {
		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void layoutContainer(Container parent) {
			int w = parent.getWidth();
			int h = parent.getHeight();
			scrollPane.setBounds(0, 0, w, h);
			scrollPane.doLayout();
			Rectangle rect = scrollPane.getViewportBorderBounds();
			glass.setBounds(0, 0, rect.width, rect.height);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return scrollPane.getMinimumSize();
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return scrollPane.getPreferredSize();
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

	}

	public void setFocus() {
		tree.requestFocus();
	}

	public void setTreeModel(TreeModel v) {
		tree.setModel(v);
		expandTreePath(v, tree.getPathForRow(0));
	}

	private void expandTreePath(TreeModel model, TreePath parent) {
		tree.expandPath(parent);
		Object parentElement = parent.getLastPathComponent();
		int count = model.getChildCount(parentElement);
		if (count > 0) {
			Object[] parent_path = parent.getPath();
			for (int i = 0; i < count; i++) {
				Object child = model.getChild(parentElement, i);
				Object[] childpath = new Object[parent_path.length + 1];
				System.arraycopy(parent_path, 0, childpath, 0, parent_path.length);
				childpath[parent_path.length] = child;
				TreePath childPath = new TreePath(childpath);
				expandTreePath(model, childPath);
			}
		}
	}

	public TreeModel getTreeModel() {
		TreeNode node = (TreeNode) tree.getModel().getRoot();
		return new DefaultTreeModel(node);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int action = glass.icon;
		int row = glass.row;
		switch (action) {
		case NO_OP:
			break;
		case ADD_NODE:
			doAddNode(row);
			break;
		case DELETE_NODE:
			doDeleteNode(row);
			break;
		case EDIT_NODE:
			doEditNode(row);
			break;
		}
	}

	class EditingListener implements CellEditorListener {
		@Override
		public void editingCanceled(ChangeEvent e) {
			setLayer(glass, 1);
			tree.getCellEditor().removeCellEditorListener(this);
			TreeModelPanel.this.doLayout();
		}

		@Override
		public void editingStopped(ChangeEvent e) {
			setLayer(glass, 1);
			tree.getCellEditor().removeCellEditorListener(this);
			TreeModelPanel.this.doLayout();
		}
	}

	private CellEditorListener listener = new EditingListener();

	private void doEditNode(int row) {
		tree.setEditable(true);
		TreePath path = tree.getPathForRow(row);
		tree.getCellEditor().addCellEditorListener(listener);
		tree.startEditingAtPath(path);
		setLayer(glass, -1);
	}

	private void doDeleteNode(int row) {
		if (row > 0) {
			TreePath path = tree.getPathForRow(row);
			Object object = path.getLastPathComponent();
			TreePath parent = path.getParentPath();
			if (object instanceof MutableTreeNode) {
				MutableTreeNode node = (MutableTreeNode) object;
				node.removeFromParent();
			}
			if (parent != null) {
				tree.expandPath(parent);
				tree.updateUI();
			}
			doLayout();
		}
	}

	private void doAddNode(int row) {
		TreePath path = tree.getPathForRow(row);
		Object object = path.getLastPathComponent();
		if (object instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
			DefaultMutableTreeNode newnode = new DefaultMutableTreeNode("New Node");
			node.add(newnode);
			tree.updateUI();
			tree.expandPath(path);
			doLayout();
		}
	}

	public Color getDesignFieldBackground() {
		return designFieldBackground;
	}

	public void setDesignFieldBackground(Color designFieldBackground) {
		this.designFieldBackground = designFieldBackground;
	}

	public Color getDesignFieldBorder() {
		return designFieldBorder;
	}

	public void setDesignFieldBorder(Color designFieldBorder) {
		this.designFieldBorder = designFieldBorder;
	}

}
