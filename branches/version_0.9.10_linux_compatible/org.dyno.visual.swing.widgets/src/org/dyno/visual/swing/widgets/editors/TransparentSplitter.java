package org.dyno.visual.swing.widgets.editors;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;

public class TransparentSplitter extends JComponent implements
		MouseInputListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private JSplitPane splitPane;
	private Timer timer;
	private ArrayList<ChangeListener> listeners;

	public TransparentSplitter(JSplitPane splitPane) {
		this.splitPane = splitPane;
		addMouseListener(this);
		addMouseMotionListener(this);
		timer = new Timer(500, this);
		listeners = new ArrayList<ChangeListener>();
	}
	public void addChangeListener(ChangeListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}
	private void fireStateChanged(ChangeEvent e) {
		for (ChangeListener l : listeners) {
			l.stateChanged(e);
		}
	}
	public void removeChangeListener(ChangeListener l) {
		if (listeners.contains(l))
			listeners.remove(l);
	}

	@Override
	public void addNotify() {
		super.addNotify();
		timer.start();
	}

	@Override
	public void removeNotify() {
		timer.stop();
		super.removeNotify();
	}

	public int getDividerLocation() {
		return splitPane.getDividerLocation();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		splitPane.dispatchEvent(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		splitPane.dispatchEvent(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		splitPane.dispatchEvent(e);
	}

	private int pressed_offset;
	private boolean dragging;

	@Override
	public void mousePressed(MouseEvent e) {
		int loc = splitPane.getDividerLocation();
		int size = splitPane.getDividerSize();
		if (splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
			int x = e.getX();
			if (x >= loc && x < loc + size) {
				dragging = true;
				pressed_offset = x - loc;				
			}
		} else if (splitPane.getOrientation() == JSplitPane.VERTICAL_SPLIT) {
			int y = e.getY();
			if (y >= loc && y < loc + size) {
				dragging = true;
				pressed_offset = y - loc;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!dragging) {
			ChangeEvent ce = new ChangeEvent(e.getSource());
			fireStateChanged(ce);
		} else {
			dragging = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(dragging){
			if (splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
				int x = e.getX();
				int loc = x-pressed_offset;
				splitPane.setDividerLocation(loc);
				splitPane.repaint();
			} else if (splitPane.getOrientation() == JSplitPane.VERTICAL_SPLIT) {
				int y = e.getY();
				int loc = y - pressed_offset;
				splitPane.setDividerLocation(loc);
				splitPane.repaint();
			}			
		}
	}

	private boolean shown;

	@Override
	protected void paintComponent(Graphics g) {
		if (shown&&!dragging) {
			int loc = splitPane.getDividerLocation();
			int size = splitPane.getDividerSize();
			int w = getWidth();
			int h = getHeight();
			if (splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
				g.setColor(Color.orange);
				g.fillRect(loc, 0, size, h - 1);
			} else {
				g.setColor(Color.orange);
				g.fillRect(0, loc, w - 1, size);
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int loc = splitPane.getDividerLocation();
		int size = splitPane.getDividerSize();
		if (splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
			int x = e.getX();
			int type = getCursor().getType();
			if (x >= loc && x < loc + size) {
				if (type != Cursor.E_RESIZE_CURSOR) {
					setCursor(Cursor
							.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				}
			} else {
				if (type != Cursor.DEFAULT_CURSOR) {
					setCursor(Cursor.getDefaultCursor());
				}
			}
		} else if (splitPane.getOrientation() == JSplitPane.VERTICAL_SPLIT) {
			int y = e.getY();
			int type = getCursor().getType();
			if (y >= loc && y < loc + size) {
				if (type != Cursor.N_RESIZE_CURSOR) {
					setCursor(Cursor
							.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
				}
			} else {
				if (type != Cursor.DEFAULT_CURSOR) {
					setCursor(Cursor.getDefaultCursor());
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		shown = !shown;
		repaint();
	}
}
