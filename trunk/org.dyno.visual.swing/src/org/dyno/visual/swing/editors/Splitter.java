package org.dyno.visual.swing.editors;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Sash;

class Splitter extends Composite implements SelectionListener, DragDetectListener, MouseListener, PaintListener {
	private static final String SPLITTER_DOWN = "/icons/splitter_down.png";
	private static final String SPLITTER_DOWN_RESTORE = "/icons/splitter_down_restore.png";
	private static final String SPLITTER_UP = "/icons/splitter_up.png";
	private static final String SPLITTER_UP_RESTORE = "/icons/splitter_up_restore.png";
	private static final String SPLITTER_LEFT = "/icons/splitter_left.png";
	private static final String SPLITTER_LEFT_RESTORE = "/icons/splitter_left_restore.png";
	private static final String SPLITTER_RIGHT = "/icons/splitter_right.png";
	private static final String SPLITTER_RIGHT_RESTORE = "/icons/splitter_right_restore.png";

	enum State {
		DEFAULT, MINIMIZED, MAXIMIZED
	};

	private int percent;
	private Sash sash;
	private Control first;
	private Control second;
	private int dividerWidth;
	private boolean vertical;
	private boolean smooth;
	private boolean dragging;
	private State state;

	public Splitter(Composite parent, int style) {
		super(parent, 0);
		state = State.DEFAULT;
		vertical = (style & SWT.VERTICAL) != 0;
		smooth = (style & SWT.SMOOTH) != 0;
		setLayout(new FormLayout());
		sash = new Sash(this, vertical?SWT.HORIZONTAL:SWT.VERTICAL);
		setDivider(50, 6);
		sash.addSelectionListener(this);
		sash.addDragDetectListener(this);
		sash.addMouseListener(this);
		sash.addPaintListener(this);
	}

	public void setFirstControl(Control control) {
		first = control;
		FormData data = new FormData();
		if (vertical) {
			data.left = new FormAttachment(0, 0);
			data.right = new FormAttachment(100, 0);
			data.top = new FormAttachment(0, 0);
			data.bottom = new FormAttachment(sash, 0);
		} else {
			data.top = new FormAttachment(0, 0);
			data.bottom = new FormAttachment(100, 0);
			data.left = new FormAttachment(0, 0);
			data.right = new FormAttachment(sash, 0);
		}
		first.setLayoutData(data);
	}

	public void setSecondControl(Control control) {
		second = control;
		FormData data = new FormData();
		if (vertical) {
			data.left = new FormAttachment(0, 0);
			data.right = new FormAttachment(100, 0);
			data.top = new FormAttachment(sash, 0);
			data.bottom = new FormAttachment(100, 0);
		} else {
			data.top = new FormAttachment(0, 0);
			data.bottom = new FormAttachment(100, 0);
			data.left = new FormAttachment(sash, 0);
			data.right = new FormAttachment(100, 0);
		}
		second.setLayoutData(data);
	}

	public void setDivider(int pc, int width) {
		dividerWidth = width;
		percent = pc;
		FormData data = new FormData();
		if (vertical) {
			data.left = new FormAttachment(0, 0);
			data.right = new FormAttachment(100, 0);
			data.top = new FormAttachment(percent, -dividerWidth / 2);
			data.bottom = new FormAttachment(percent, dividerWidth / 2);
		} else {
			data.top = new FormAttachment(0, 0);
			data.bottom = new FormAttachment(100, 0);
			data.left = new FormAttachment(percent, -dividerWidth / 2);
			data.right = new FormAttachment(percent, dividerWidth / 2);
		}
		sash.setLayoutData(data);
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void widgetSelected(SelectionEvent e) {
		if (smooth) {
			if (vertical) {
				((FormData) sash.getLayoutData()).top = new FormAttachment(0, e.y - dividerWidth / 2);
				((FormData) sash.getLayoutData()).bottom = new FormAttachment(0, e.y + dividerWidth / 2);
			} else {
				((FormData) sash.getLayoutData()).left = new FormAttachment(0, e.x - dividerWidth / 2);
				((FormData) sash.getLayoutData()).right = new FormAttachment(0, e.x + dividerWidth / 2);
			}
			layout();
		}
	}

	public void dragDetected(DragDetectEvent e) {
		dragging = true;
	}

	public void mouseDoubleClick(MouseEvent e) {
		maximize();
	}

	private void maximize() {
		state = State.MAXIMIZED;
		if (vertical) {
			((FormData) sash.getLayoutData()).top = new FormAttachment(100, -dividerWidth);
			((FormData) sash.getLayoutData()).bottom = new FormAttachment(100, 0);
		} else {
			((FormData) sash.getLayoutData()).left = new FormAttachment(100, -dividerWidth);
			((FormData) sash.getLayoutData()).right = new FormAttachment(100, 0);
		}
		layout();
		sash.redraw();
	}

	private void minimize() {
		state = State.MINIMIZED;
		if (vertical) {
			((FormData) sash.getLayoutData()).top = new FormAttachment(0, 0);
			((FormData) sash.getLayoutData()).bottom = new FormAttachment(0, dividerWidth);
		} else {
			((FormData) sash.getLayoutData()).left = new FormAttachment(0, 0);
			((FormData) sash.getLayoutData()).right = new FormAttachment(0, dividerWidth);
		}
		layout();
		sash.redraw();
	}

	private void restore() {
		state = State.DEFAULT;
		if (vertical) {
			((FormData) sash.getLayoutData()).top = new FormAttachment(percent, -dividerWidth / 2);
			((FormData) sash.getLayoutData()).bottom = new FormAttachment(percent, dividerWidth / 2);
		} else {
			((FormData) sash.getLayoutData()).left = new FormAttachment(percent, -dividerWidth / 2);
			((FormData) sash.getLayoutData()).right = new FormAttachment(percent, dividerWidth / 2);
		}
		layout();
		sash.redraw();
	}

	public void mouseDown(MouseEvent e) {

	}

	public void mouseUp(MouseEvent e) {
		if (dragging) {
			dragging = false;
			if (!smooth) {
				Rectangle bounds = sash.getBounds();
				if (vertical) {
					((FormData) sash.getLayoutData()).top = new FormAttachment(0, bounds.y + e.y - dividerWidth / 2);
					((FormData) sash.getLayoutData()).bottom = new FormAttachment(0, bounds.y + e.y + dividerWidth / 2);
				} else {
					((FormData) sash.getLayoutData()).left = new FormAttachment(0, bounds.x + e.x - dividerWidth / 2);
					((FormData) sash.getLayoutData()).right = new FormAttachment(0, bounds.x + e.x + dividerWidth / 2);
				}
				layout();
				state = State.DEFAULT;
			}
		} else {
			boolean first = false;
			boolean second = false;
			if (vertical) {
				int width = sash.getBounds().width;
				int x = width / 2 - 8;
				first = e.x > x && e.x < x + 11;
				second = e.x > x + 11 && e.x < x + 18;
			} else {
				int height = sash.getBounds().height;
				int y = height / 2 - 8;
				first = e.y > y && e.y < y + 11;
				second = e.y > y + 11 && e.y < y + 18;
			}
			switch (state) {
			case DEFAULT:
				if (first) {
					maximize();
				} else if (second) {
					minimize();
				}
				break;
			case MAXIMIZED:
				if (first) {
					restore();
				} else if (second) {
					minimize();
				}
				break;
			case MINIMIZED:
				if (first) {
					restore();
				} else {
					maximize();
				}
				break;
			}
		}
		sash.redraw();
	}

	public void paintControl(PaintEvent e) {
		if (vertical) {
			int width = e.width;
			String left, right;
			switch (state) {
			case DEFAULT:
				left = SPLITTER_DOWN;
				right = SPLITTER_UP;
				break;
			case MINIMIZED:
				left = SPLITTER_DOWN;
				right = SPLITTER_DOWN_RESTORE;
				break;
			case MAXIMIZED:
				left = SPLITTER_UP;
				right = SPLITTER_UP_RESTORE;
				break;
			default:
				left = SPLITTER_DOWN;
				right = SPLITTER_UP;
				break;
			}
			Image leftImg = VisualSwingPlugin.getSharedImage(left);
			Image rightImg = VisualSwingPlugin.getSharedImage(right);
			int y = 1;
			int x = width / 2 - 8;
			e.gc.drawImage(leftImg, x, y);
			e.gc.drawImage(rightImg, x + 11, y);
		} else {
			int height = e.height;
			String top, bottom;
			switch (state) {
			case DEFAULT:
				top = SPLITTER_LEFT;
				bottom = SPLITTER_RIGHT;
				break;
			case MINIMIZED:
				top = SPLITTER_LEFT;
				bottom = SPLITTER_LEFT_RESTORE;
				break;
			case MAXIMIZED:
				top = SPLITTER_RIGHT;
				bottom = SPLITTER_RIGHT_RESTORE;
				break;
			default:
				top = SPLITTER_LEFT;
				bottom = SPLITTER_RIGHT;
				break;
			}
			Image topImg = VisualSwingPlugin.getSharedImage(top);
			Image bottomImg = VisualSwingPlugin.getSharedImage(bottom);
			int x = 1;
			int y = height / 2 - 8;
			e.gc.drawImage(topImg, x, y);
			e.gc.drawImage(bottomImg, x, y + 11);
		}
	}
}
