package org.dyno.visual.swing.widgets.editors;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

public class TableModelPanel extends JLayeredPane implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JTable table;
	private TableColumnHeaderGlass columnHeader;
	private TableRowHeaderGlass rowHeader;
	private JTextField textField;
	private Color designFieldBackground;
	private Color designFieldBorder;

	public TableModelPanel(JScrollPane jsp) {
		setOpaque(false);
		setLayout(new GlassLayout());
		setLayer(jsp, 0);
		add(jsp);
		jsp.setToolTipText("Adjust view");		
		jsp.getHorizontalScrollBar().setToolTipText("Drag it to adjust view");
		jsp.getVerticalScrollBar().setToolTipText("Drag it to adjust view");
		TableColumnHeaderGlass columnHeader = new TableColumnHeaderGlass();
		TableRowHeaderGlass rowHeader = new TableRowHeaderGlass();
		setLayer(columnHeader, 1);
		add(columnHeader);
		setLayer(rowHeader, 2);
		add(rowHeader);
		this.scrollPane = jsp;
		this.table = (JTable) jsp.getViewport().getView();
		this.table.setToolTipText("Click cell to edit");
		this.columnHeader = columnHeader;
		this.columnHeader.addActionListener(this);
		this.rowHeader = rowHeader;
		this.rowHeader.addActionListener(this);
		this.listeners = new ArrayList<TableModelListener>();
		this.textField = new JTextField();
		this.columnHeader.add(textField);
		this.textField.setVisible(false);
		designFieldBackground = Color.orange.brighter();
		designFieldBorder = Color.green.darker();
	}

	private Composite getEmbeddedParent() {
		Component parent = this;
		while (parent != null) {
			if (parent instanceof JComponent) {
				Object object = ((JComponent) parent).getClientProperty("embeded.composite");
				if (object != null && object instanceof Composite)
					return (Composite) object;
			}
			parent = parent.getParent();
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == columnHeader) {
			int action = columnHeader.icon;
			int column = columnHeader.column;
			switch (action) {
			case NO_OP:
				break;
			case COLUMN_LEFT_INSERT:
				doLeftInsert(column);
				break;
			case COLUMN_LEFT_MOVE:
				doMoveLeft(column);
				break;
			case COLUMN_RIGHT_MOVE:
				doMoveRight(column);
				break;
			case COLUMN_RIGHT_INSERT:
				doRightInsert(column);
				break;
			case COLUMN_DELETE:
				doDeleteColumn(column);
				break;
			case COLUMN_EDIT_TEXT:
				doEditText(column);
				break;
			case COLUMN_EDIT_TYPE:
				doEditType(column);
				break;
			}
		} else if (e.getSource() == rowHeader) {
			int action = rowHeader.icon;
			int row = rowHeader.row;
			switch (action) {
			case ROW_DELETE:
				doDeleteRow(row);
				break;
			case ROW_UP_INSERT:
				doUpInsert(row);
				break;
			case ROW_UP_MOVE:
				doUpMove(row);
				break;
			case ROW_DOWN_INSERT:
				doDownInsert(row);
				break;
			case ROW_DOWN_MOVE:
				doDownMove(row);
				break;
			}
		}
	}

	private void doDownMove(int row) {
		if (row < data.size() - 1) {
			int next = row + 1;
			List<Object> nextRow = data.get(next);
			data.set(next, data.get(row));
			data.set(row, nextRow);
			table.setModel(new InnerTableModel());
		}
	}

	private void doDownInsert(int row) {
		List<Object> rowList = new ArrayList<Object>();
		for (int i = 0; i < columnNames.size(); i++) {
			Object value = getDefaultClassValue(columnClass.get(i));
			if (value == null)
				value = "" + row + "x" + i;
			rowList.add(value);
		}
		data.add(row + 1, rowList);
		table.setModel(new InnerTableModel());
	}

	private Object getDefaultClassValue(Class<?> clazz) {
		TypeAction action = getTypeAction(clazz);
		if (action == null)
			return null;
		return action.defaultValue;
	}

	private void doUpInsert(int row) {
		List<Object> rowList = new ArrayList<Object>();
		for (int i = 0; i < columnNames.size(); i++) {
			Object value = getDefaultClassValue(columnClass.get(i));
			if (value == null)
				value = "" + row + "x" + i;
			rowList.add(value);
		}
		data.add(row, rowList);
		table.setModel(new InnerTableModel());
	}

	private void doUpMove(int row) {
		if (row > 0) {
			int prev = row - 1;
			List<Object> prevRow = data.get(prev);
			data.set(prev, data.get(row));
			data.set(row, prevRow);
			table.setModel(new InnerTableModel());
		}
	}

	private void doDeleteRow(int row) {
		data.remove(row);
		table.setModel(new InnerTableModel());
	}

	private void doMoveRight(int column) {
		int count = columnNames.size();
		if (column < count - 1) {
			int next = column + 1;
			swap(columnNames, column, next);
			swap(columnClass, column, next);
			for (List<Object> row : data) {
				swap(row, column, next);
			}
			table.setModel(new InnerTableModel());
		}
	}

	@SuppressWarnings("unchecked")
	private void swap(List list, int i, int j) {
		Object temp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, temp);
	}

	private void doMoveLeft(int column) {
		if (column > 0) {
			int prev = column - 1;
			swap(columnNames, column, prev);
			swap(columnClass, column, prev);
			for (List<Object> row : data) {
				swap(row, column, prev);
			}
			table.setModel(new InnerTableModel());
		}
	}

	private TypeAction[] actions = new TypeAction[] { new TypeAction(Object.class, "Object", null), new TypeAction(String.class, "String", null),
			new TypeAction(Boolean.class, "Boolean", new Boolean(false)), new TypeAction(Integer.class, "Integer", (int) 0),
			new TypeAction(Byte.class, "Byte", (byte) 0), new TypeAction(Short.class, "Short", (short) 0), new TypeAction(Long.class, "Long", 0l),
			new TypeAction(Float.class, "Float", 0f), new TypeAction(Double.class, "Double", 0d) };

	private TypeAction getTypeAction(Class<?> clazz) {
		for (TypeAction action : actions) {
			if (action.clazz == clazz)
				return action;
		}
		return null;
	}

	private void doEditType(final int column) {
		getEmbeddedParent().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				popupMenu(column);
			}
		});
	}

	private void popupMenu(int column) {
		MenuManager popup = new MenuManager("#TABLE_MODEL_TYPE_POPUP");
		for (TypeAction action : actions) {
			action.setColumn(column);
			popup.add(action);
		}
		Point p = columnHeader.getPopupLocation();
		Menu menu = popup.createContextMenu(getEmbeddedParent());
		Point loc = columnHeader.getLocationOnScreen();
		menu.setLocation(loc.x + p.x, loc.y + p.y);
		menu.setVisible(true);
	}

	class TypeAction extends Action {
		private Class<?> clazz;
		private int column;
		private Object defaultValue;

		public void setColumn(int column) {
			this.column = column;
			super.setChecked(columnClass.get(column) == clazz);
		}

		public TypeAction(Class<?> clazz, String name, Object defaultValue) {
			super(name, SWT.RADIO);
			this.clazz = clazz;
			this.defaultValue = defaultValue;
		}

		public Object getDefaultValue() {
			return defaultValue;
		}

		@Override
		public void run() {
			Class<?> prevClass = columnClass.get(column);
			if (prevClass != clazz) {
				columnClass.set(column, clazz);
				for (int r = 0; r < data.size(); r++) {
					List<Object> row = data.get(r);
					if (defaultValue == null) {
						row.set(column, "" + r + "x" + column);
					} else {
						row.set(column, defaultValue);
					}
				}
				table.setModel(new InnerTableModel());
			}
		}
	}

	private void doEditText(int column) {
		columnHeader.startEditing();
	}

	private void doDeleteColumn(int column) {
		columnClass.remove(column);
		columnNames.remove(column);
		for (List<Object> row : data) {
			row.remove(column);
		}
		table.setModel(new InnerTableModel());
	}

	private void doRightInsert(int column) {
		int i = column + 1;
		columnClass.add(i, String.class);
		columnNames.add(i, "Title " + i);
		for (int r = 0; r < data.size(); r++) {
			List<Object> row = data.get(r);
			row.add(i, "" + r + "x" + i);
		}
		table.setModel(new InnerTableModel());
	}

	private void doLeftInsert(int column) {
		columnClass.add(column, String.class);
		columnNames.add(column, "Title " + column);
		for (int r = 0; r < data.size(); r++) {
			List<Object> row = data.get(r);
			row.add(column, "" + r + "x" + column);
		}
		table.setModel(new InnerTableModel());
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
			columnHeader.setBounds(0, 0, rect.x + rect.width, rect.y);
			rowHeader.setBounds(0, rect.y, 64, rect.height);
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

	private static final int PRESSED = 0;
	private static final int RELEASED = 1;

	private static final int NO_OP = -1;
	private static final int COLUMN_LEFT_INSERT = 0;
	private static final int ROW_UP_INSERT = 0;
	private static final int COLUMN_LEFT_MOVE = 1;
	private static final int ROW_UP_MOVE = 1;
	private static final int COLUMN_DELETE = 2;
	private static final int ROW_DELETE = 2;
	private static final int COLUMN_EDIT_TEXT = 3;
	private static final int COLUMN_EDIT_TYPE = 4;
	private static final int COLUMN_RIGHT_MOVE = 5;
	private static final int ROW_DOWN_MOVE = 3;
	private static final int COLUMN_RIGHT_INSERT = 6;
	private static final int ROW_DOWN_INSERT = 4;

	private static Icon COLUMN_LEFT_INSERT_ICON;
	private static Icon ROW_UP_INSERT_ICON;
	private static Icon COLUMN_LEFT_MOVE_ICON;
	private static Icon ROW_UP_MOVE_ICON;
	private static Icon COLUMN_DELETE_ICON;
	private static Icon ROW_DELETE_ICON;
	private static Icon COLUMN_EDIT_TEXT_ICON;
	private static Icon COLUMN_EDIT_TYPE_ICON;
	private static Icon COLUMN_RIGHT_MOVE_ICON;
	private static Icon ROW_DOWN_MOVE_ICON;
	private static Icon COLUMN_RIGHT_INSERT_ICON;
	private static Icon ROW_DOWN_INSERT_ICON;
	static {
		COLUMN_LEFT_INSERT_ICON = new ImageIcon(TableModelPanel.class.getResource("/icons/insert.png"));
		ROW_UP_INSERT_ICON = new ImageIcon(TableModelPanel.class.getResource("/icons/insert.png"));
		COLUMN_LEFT_MOVE_ICON = new ImageIcon(TableModelPanel.class.getResource("/icons/left_move.png"));
		ROW_UP_MOVE_ICON = new ImageIcon(TableModelPanel.class.getResource("/icons/up_move.png"));
		COLUMN_RIGHT_INSERT_ICON = new ImageIcon(TableModelPanel.class.getResource("/icons/insert.png"));
		ROW_DOWN_INSERT_ICON = new ImageIcon(TableModelPanel.class.getResource("/icons/insert.png"));
		COLUMN_RIGHT_MOVE_ICON = new ImageIcon(TableModelPanel.class.getResource("/icons/right_move.png"));
		ROW_DOWN_MOVE_ICON = new ImageIcon(TableModelPanel.class.getResource("/icons/down_move.png"));
		COLUMN_DELETE_ICON = new ImageIcon(TableModelPanel.class.getResource("/icons/delete.png"));
		ROW_DELETE_ICON = new ImageIcon(TableModelPanel.class.getResource("/icons/delete.png"));
		COLUMN_EDIT_TEXT_ICON = new ImageIcon(TableModelPanel.class.getResource("/icons/edit_text.png"));
		COLUMN_EDIT_TYPE_ICON = new ImageIcon(TableModelPanel.class.getResource("/icons/edit_type.png"));
	}

	class ColumnNameAction implements ActionListener {
		private int column;

		public ColumnNameAction(int c) {
			column = c;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String title = textField.getText();
			columnNames.set(column, title);
			columnHeader.stopEditing();
			table.setModel(new InnerTableModel());
		}

	}

	class TableRowHeaderGlass extends JComponent implements MouseInputListener, MouseWheelListener {
		private static final long serialVersionUID = 1L;
		private int status;
		private int icon = -1;
		private int row = -1;
		private List<ActionListener> listeners;

		public TableRowHeaderGlass() {
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

		private int getXoffset() {
			switch (icon) {
			case ROW_UP_INSERT:
				return 0;
			case ROW_UP_MOVE:
				return 32;
			case ROW_DOWN_INSERT:
				return 0;
			case ROW_DOWN_MOVE:
				return 48;
			case ROW_DELETE:
				return 16;
			}
			return 0;
		}

		private int getYoffset() {
			switch (icon) {
			case ROW_UP_INSERT:
				return -8;
			case ROW_UP_MOVE:
				return 0;
			case ROW_DOWN_INSERT:
				return 8;
			case ROW_DOWN_MOVE:
				return 0;
			case ROW_DELETE:
				return 0;
			}
			return 0;
		}

		@Override
		protected void paintComponent(Graphics g) {
			paintBackground(g);
			if (row != -1) {
				Rectangle rect = table.getCellRect(row, 0, true);
				rect.width = 64;
				rect = SwingUtilities.convertRectangle(table, rect, TableModelPanel.this);
				rect = SwingUtilities.convertRectangle(TableModelPanel.this, rect, rowHeader);
				rect.x = 0;
				int x = rect.x;
				int y = rect.y;
				int w = 16;
				int h = 16;
				if (icon != NO_OP) {
					Icon iCon = getIcon();
					if (iCon != null) {
						x += getXoffset();
						y += getYoffset();
						w = iCon.getIconWidth();
						h = iCon.getIconHeight();
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
			g.setColor(designFieldBorder);
			int width = getWidth();
			int height = getHeight();
			g.drawLine(0, 0, 0, height - 1);
			g.drawLine(0, height - 1, width - 1, height - 1);
			g.drawLine(width-1, 0, width - 1, height - 1);
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

		private Icon getIcon() {
			switch (icon) {
			case NO_OP:
				return null;
			case ROW_UP_INSERT:
				return ROW_UP_INSERT_ICON;
			case ROW_UP_MOVE:
				return ROW_UP_MOVE_ICON;
			case ROW_DOWN_INSERT:
				return ROW_DOWN_INSERT_ICON;
			case ROW_DOWN_MOVE:
				return ROW_DOWN_MOVE_ICON;
			case ROW_DELETE:
				return ROW_DELETE_ICON;
			}
			return null;
		}

		private Rectangle getIconBounds() {
			switch (icon) {
			case NO_OP:
				return null;
			case ROW_UP_INSERT:
				return new Rectangle(0, -8, 16, 16);
			case ROW_UP_MOVE:
				return new Rectangle(32, 0, 16, 16);
			case ROW_DOWN_INSERT:
				return new Rectangle(0, 8, 16, 16);
			case ROW_DOWN_MOVE:
				return new Rectangle(48, 0, 16, 16);
			case ROW_DELETE:
				return new Rectangle(16, 0, 16, 16);
			}
			return null;
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
			Point p = e.getPoint();
			int count = table.getRowCount();
			for (int i = 0; i < count; i++) {
				Rectangle rect = table.getCellRect(i, 0, true);
				rect.width = 64;
				rect = SwingUtilities.convertRectangle(table, rect, TableModelPanel.this);
				rect = SwingUtilities.convertRectangle(TableModelPanel.this, rect, rowHeader);
				rect.x = 0;
				if (p.x >= rect.x && p.x < rect.x + rect.width) {
					icon = getIconPlace(p, rect);
					Icon iCon = getIcon();
					if (iCon != null) {
						Rectangle iconBounds = getIconBounds();
						iconBounds.x += rect.x;
						iconBounds.y += rect.y;
						if (p.x >= iconBounds.x && p.x < iconBounds.x + iconBounds.width && p.y >= iconBounds.y && p.y < iconBounds.y + iconBounds.height) {
							row = i;
							status = PRESSED;
							repaint();
							return;
						}
					}
				}
			}
			setToolTipText(null);
			row = -1;
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Point p = e.getPoint();
			int count = table.getRowCount();
			for (int i = 0; i < count; i++) {
				Rectangle rect = table.getCellRect(i, 0, true);
				rect.width = 64;
				rect = SwingUtilities.convertRectangle(table, rect, TableModelPanel.this);
				rect = SwingUtilities.convertRectangle(TableModelPanel.this, rect, rowHeader);
				rect.x = 0;
				if (p.x >= rect.x && p.x < rect.x + rect.width) {
					icon = getIconPlace(p, rect);
					Icon iCon = getIcon();
					if (iCon != null) {
						Rectangle iconBounds = getIconBounds();
						iconBounds.x += rect.x;
						iconBounds.y += rect.y;
						if (p.x >= iconBounds.x && p.x < iconBounds.x + iconBounds.width && p.y >= iconBounds.y && p.y < iconBounds.y + iconBounds.height) {
							row = i;
							status = RELEASED;
							repaint();
							fireActionPerformed();
							return;
						}
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

		private int getIconPlace(Point p, Rectangle rect) {
			if (p.x > rect.x && p.x < rect.x + 16 && p.y < rect.y + 8 && p.y > rect.y - 8)
				return ROW_UP_INSERT;
			else if (p.x > rect.x && p.x < rect.x + 16 && p.y > rect.y + 8 && p.y < rect.y + 24)
				return ROW_DOWN_INSERT;
			else if (p.x > rect.x + 16 && p.x < rect.x + 32 && p.y > rect.y && p.y < rect.y + 16) {
				return ROW_DELETE;
			} else if (p.x > rect.x + 32 && p.x < rect.x + 48 && p.y > rect.y && p.y < rect.y + 16)
				return ROW_UP_MOVE;
			else if (p.x > rect.x + 48 && p.x < rect.x + 64 && p.y > rect.y && p.y < rect.y + 16)
				return ROW_DOWN_MOVE;
			else
				return NO_OP;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Point p = e.getPoint();
			int count = table.getRowCount();
			for (int i = 0; i < count; i++) {
				Rectangle rect = table.getCellRect(i, 0, true);
				rect.width = 64;
				rect = SwingUtilities.convertRectangle(table, rect, TableModelPanel.this);
				rect = SwingUtilities.convertRectangle(TableModelPanel.this, rect, rowHeader);
				rect.x = 0;
				if (p.x >= rect.x && p.x < rect.x + rect.width) {
					int lastIcon = icon;
					icon = getIconPlace(p, rect);
					Icon iCon = getIcon();
					if (iCon != null) {
						Rectangle iconBounds = getIconBounds();
						iconBounds.x += rect.x;
						iconBounds.y += rect.y;
						if (p.x >= iconBounds.x && p.x < iconBounds.x + iconBounds.width && p.y >= iconBounds.y && p.y < iconBounds.y + iconBounds.height) {
							if (row != i || lastIcon != icon) {
								row = i;
								status = RELEASED;
								repaint();
								setToolTipText(getCurrentText());
							}
							return;
						}
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
			case ROW_UP_INSERT:
				return "Insert row";
			case ROW_UP_MOVE:
				return "Move upward";
			case ROW_DOWN_INSERT:
				return "Append row";
			case ROW_DOWN_MOVE:
				return "Move downward";
			case ROW_DELETE:
				return "Delete row";
			}
			return null;
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
		}

	}

	class TableColumnHeaderGlass extends JComponent implements MouseInputListener, MouseWheelListener {
		private static final long serialVersionUID = 1L;
		private int status;
		private int icon = -1;
		private int column = -1;
		private List<ActionListener> listeners;
		private ActionListener action;

		private Icon getIcon() {
			switch (icon) {
			case NO_OP:
				return null;
			case COLUMN_LEFT_INSERT:
				return COLUMN_LEFT_INSERT_ICON;
			case COLUMN_LEFT_MOVE:
				return COLUMN_LEFT_MOVE_ICON;
			case COLUMN_RIGHT_INSERT:
				return COLUMN_RIGHT_INSERT_ICON;
			case COLUMN_RIGHT_MOVE:
				return COLUMN_RIGHT_MOVE_ICON;
			case COLUMN_DELETE:
				return COLUMN_DELETE_ICON;
			case COLUMN_EDIT_TEXT:
				return COLUMN_EDIT_TEXT_ICON;
			case COLUMN_EDIT_TYPE:
				return COLUMN_EDIT_TYPE_ICON;
			}
			return null;
		}

		void startEditing() {
			if (column == -1)
				return;
			Rectangle rect = table.getTableHeader().getHeaderRect(column);
			action = new ColumnNameAction(column);
			textField.addActionListener(action);
			textField.setVisible(true);
			textField.setBounds(rect);
			textField.setText(columnNames.get(column));
			textField.selectAll();
			textField.requestFocus();
		}

		Point getPopupLocation() {
			Rectangle rect = table.getTableHeader().getHeaderRect(column);
			rect = SwingUtilities.convertRectangle(table, rect, TableModelPanel.this);
			int x = rect.x;
			int y = rect.y;
			if (icon != NO_OP) {
				Icon iCon = getIcon();
				if (iCon != null) {
					x += icon * rect.width / 6 - iCon.getIconWidth() / 2;
				}
			}
			return new Point(x, y);
		}

		void stopEditing() {
			textField.removeActionListener(action);
			textField.setVisible(false);
		}

		public TableColumnHeaderGlass() {
			setLayout(null);
			setOpaque(false);			
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
			listeners = new ArrayList<ActionListener>();
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
		protected void paintComponent(Graphics g) {
			paintBackground(g);
			if (column != -1) {
				Rectangle rect = table.getTableHeader().getHeaderRect(column);
				rect = SwingUtilities.convertRectangle(table, rect, TableModelPanel.this);
				int x = rect.x;
				int y = 0;
				int w = 16;
				int h = 16;
				if (icon != NO_OP) {
					Icon iCon = getIcon();
					if (iCon != null) {
						x += icon * rect.width / 6 - iCon.getIconWidth() / 2;
						y += (rect.height - iCon.getIconHeight()) / 2;
						w = iCon.getIconWidth();
						h = iCon.getIconHeight();
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
			g.setColor(designFieldBorder);
			int width = getWidth();
			int height = getHeight();
			g.drawLine(0, 0, 0, height - 1);
			g.drawLine(0, 0, width-1, 0);
			g.drawLine(width - 1, 0, width-1, height-1);
			g.drawLine(63, height-1, width-1, height-1);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
			column = -1;
			repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			Point p = SwingUtilities.convertPoint(TableModelPanel.this, e.getPoint(), table);
			JTableHeader header = table.getTableHeader();
			int count = table.getColumnCount();
			for (int i = 0; i < count; i++) {
				Rectangle rect = header.getHeaderRect(i);
				icon = (p.x - rect.x + 8) / (rect.width / 6);
				Icon iCon = getIcon();
				if (iCon != null) {
					int x = rect.x + icon * rect.width / 6 - iCon.getIconWidth() / 2;
					if (p.x >= x && p.x < x + iCon.getIconWidth()) {
						column = i;
						status = PRESSED;
						repaint();
						return;
					}
				}
			}
			column = -1;
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Point p = SwingUtilities.convertPoint(TableModelPanel.this, e.getPoint(), table);
			JTableHeader header = table.getTableHeader();
			int count = table.getColumnCount();
			for (int i = 0; i < count; i++) {
				Rectangle rect = header.getHeaderRect(i);
				if (p.x >= rect.x && p.x < rect.x + rect.width) {
					icon = (p.x - rect.x) / (rect.width / 6);
					Icon iCon = getIcon();
					if (iCon != null) {
						int x = rect.x + icon * rect.width / 6 - iCon.getIconWidth() / 2;
						if (p.x >= x && p.x < x + iCon.getIconWidth()) {
							column = i;
							status = RELEASED;
							repaint();
							fireActionPerformed();
							return;
						}
					}
				}
			}
			column = -1;
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Point p = SwingUtilities.convertPoint(TableModelPanel.this, e.getPoint(), table);
			JTableHeader header = table.getTableHeader();
			int count = table.getColumnCount();
			for (int i = 0; i < count; i++) {
				Rectangle rect = header.getHeaderRect(i);
				if (p.x >= rect.x && p.x < rect.x + rect.width) {
					int lastIcon = icon;
					icon = (p.x - rect.x) / (rect.width / 6);
					Icon iCon = getIcon();
					if (iCon != null) {
						int x = rect.x + icon * rect.width / 6 - iCon.getIconWidth() / 2;
						if (p.x >= x && p.x < x + iCon.getIconWidth()) {
							if (column != i || lastIcon != icon) {
								column = i;
								status = RELEASED;
								repaint();
								setToolTipText(getCurrentText());
							}
							return;
						}
					}
				}
			}
			setToolTipText(null);
			column = -1;
			repaint();
		}

		private String getCurrentText() {
			switch (icon) {
			case NO_OP:
				return null;
			case COLUMN_LEFT_INSERT:
				return "Insert column";
			case COLUMN_LEFT_MOVE:
				return "Move leftward";
			case COLUMN_RIGHT_INSERT:
				return "Append column";
			case COLUMN_RIGHT_MOVE:
				return "Move rightward";
			case COLUMN_DELETE:
				return "Delete column";
			case COLUMN_EDIT_TEXT:
				return "Edit column title";
			case COLUMN_EDIT_TYPE:
				return "Edit column type";
			}
			return null;
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
		}
	}

	public TableModel getTableModel() {
		Object[][] d = new Object[data.size()][];
		Object[] n = new Object[columnNames.size()];
		Class<?>[] c = new Class[columnClass.size()];
		for (int i = 0; i < columnNames.size(); i++)
			n[i] = columnNames.get(i);
		for (int i = 0; i < columnClass.size(); i++)
			c[i] = columnClass.get(i);
		for (int row = 0; row < data.size(); row++) {
			List<Object> rowList = data.get(row);
			d[row] = new Object[rowList.size()];
			for (int col = 0; col < rowList.size(); col++) {
				d[row][col] = rowList.get(col);
			}
		}
		return new TypedTableModel(d, n, c);
	}

	public void setFocus() {
		table.requestFocus();
	}

	public void setTableModel(TableModel v) {
		columnClass = new ArrayList<Class<?>>();
		columnNames = new ArrayList<String>();
		for (int i = 0; i < v.getColumnCount(); i++) {
			Class<?> type = v.getColumnClass(i);
			if (type == null)
				columnClass.add(String.class);
			else
				columnClass.add(type);
			columnNames.add(v.getColumnName(i));
		}
		data = new ArrayList<List<Object>>();
		for (int r = 0; r < v.getRowCount(); r++) {
			List<Object> row = new ArrayList<Object>();
			for (int c = 0; c < v.getColumnCount(); c++) {
				row.add(v.getValueAt(r, c));
			}
			data.add(row);
		}
		table.setModel(new InnerTableModel());
	}

	@Override
	public void setFont(Font font) {
		table.setFont(font);
		super.setFont(font);
	}

	private List<TableModelListener> listeners;
	private List<Class<?>> columnClass;
	private List<String> columnNames;
	private List<List<Object>> data;

	class InnerTableModel implements TableModel {
		@Override
		public void addTableModelListener(TableModelListener l) {
			if (!listeners.contains(l))
				listeners.add(l);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return columnClass.get(columnIndex);
		}

		@Override
		public int getColumnCount() {
			return columnNames.size();
		}

		@Override
		public String getColumnName(int columnIndex) {
			return columnNames.get(columnIndex);
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data.get(rowIndex).get(columnIndex);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
			if (listeners.contains(l))
				listeners.remove(l);
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			data.get(rowIndex).set(columnIndex, value);
		}
	}

	public Color getDesignFieldBackground() {
		return designFieldBackground;
	}

	public void setDesignFieldBackground(Color designFieldBackground) {
		this.designFieldBackground = designFieldBackground;
	}
}
