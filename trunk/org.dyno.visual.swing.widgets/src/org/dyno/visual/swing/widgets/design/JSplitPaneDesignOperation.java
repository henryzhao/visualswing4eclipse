package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JSplitPaneDesignOperation extends CompositeDesignOperation {
	private boolean existsAndDesigning(JComponent comp) {
		return comp != null && WidgetAdapter.getWidgetAdapter(comp) != null;
	}
	private Rectangle getRightBounds() {
		JSplitPane jsp = (JSplitPane) adaptable.getWidget();
		int width = jsp.getWidth();
		int height = jsp.getHeight();
		int divideLocation = jsp.getDividerLocation();
		int dividerSize = jsp.getDividerSize();
		Insets insets = jsp.getInsets();
		int x = 0, y = 0, w = 0, h = 0;
		x = divideLocation + dividerSize;
		y = insets.top;
		w = width - x - insets.right;
		if (w < 10) {
			w = 10;
			x = width - insets.right - w;
		}
		h = height - insets.top - insets.bottom;
		return new Rectangle(x, y, w, h);
	}

	private String position;

	private Rectangle getLeftBounds() {
		JSplitPane jsp = (JSplitPane) adaptable.getWidget();
		int height = jsp.getHeight();
		int divideLocation = jsp.getDividerLocation();
		Insets insets = jsp.getInsets();
		int x = 0, y = 0, w = 0, h = 0;
		x = insets.left;
		y = insets.top;
		w = divideLocation - x;
		if(w<10){
			w = 10;
			x = divideLocation-w;
		}
		h = height - insets.top - insets.bottom;
		return new Rectangle(x, y, w, h);
	}
	private Rectangle getTopBounds() {
		JSplitPane jsp = (JSplitPane) adaptable.getWidget();
		int width = jsp.getWidth();
		int divideLocation = jsp.getDividerLocation();
		Insets insets = jsp.getInsets();
		int x = 0, y = 0, w = 0, h = 0;
		x = insets.left;
		y = insets.top;
		h = divideLocation - y;
		if(h<10){
			h=10;
			y = divideLocation - h;
		}		
		w = width - insets.left - insets.right;
		return new Rectangle(x, y, w, h);
	}
	private Rectangle getBottomBounds() {
		JSplitPane jsp = (JSplitPane) adaptable.getWidget();
		int width = jsp.getWidth();
		int height = jsp.getHeight();
		int divideLocation = jsp.getDividerLocation();
		int dividerSize = jsp.getDividerSize();
		Insets insets = jsp.getInsets();
		int x = 0, y = 0, w = 0, h = 0;
		y = insets.top + divideLocation + dividerSize;
		x = insets.left;
		h = height - y;
		if (h < 10) {
			h = 10;
			y = height - h;
		}
		w = width - insets.left - insets.right;
		return new Rectangle(x, y, w, h);
	}

	private boolean forbid;

	public boolean isForbid() {
		return forbid;
	}
	public Rectangle getHotspotBounds() {
		if (position == null)
			return null;
		if (position.equals("left")) //$NON-NLS-1$
			return getLeftBounds();
		if (position.equals("top")) //$NON-NLS-1$
			return getTopBounds();
		if (position.equals("right")) //$NON-NLS-1$
			return getRightBounds();
		if (position.equals("bottom")) //$NON-NLS-1$
			return getBottomBounds();
		return null;
	}


	private void updatePosition(Point p) {
		List<WidgetAdapter>dropping=adaptable.getDropWidget();
		if(dropping==null||dropping.size()!=1){
			forbid=true;
			position=null;
			return;
		}
		JSplitPane jsp = (JSplitPane) adaptable.getWidget();
		int orientation = jsp.getOrientation();
		if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
			if (getLeftBounds().contains(p)) {
				JComponent left = (JComponent) jsp.getLeftComponent();
				position = "left"; //$NON-NLS-1$
				forbid = existsAndDesigning(left);
			} else if (getRightBounds().contains(p)) {
				JComponent right = (JComponent) jsp.getRightComponent();
				position = "right"; //$NON-NLS-1$
				forbid = existsAndDesigning(right);
			} else {
				position = null;
				forbid = true;
			}
		} else {
			if (getTopBounds().contains(p)) {
				JComponent top = (JComponent) jsp.getTopComponent();
				position = "top"; //$NON-NLS-1$
				forbid = existsAndDesigning(top);
			} else if (getBottomBounds().contains(p)) {
				JComponent bottom = (JComponent) jsp.getBottomComponent();
				position = "bottom"; //$NON-NLS-1$
				forbid = existsAndDesigning(bottom);
			} else {
				position = null;
				forbid = true;
			}
		}
	}

	@Override
	public boolean drop(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.drop(p);
		if (position != null) {
			if (forbid) {
				Toolkit.getDefaultToolkit().beep();
				forbid = false;
				position = null;
				return false;
			} else {
				WidgetAdapter adapter = adaptable.getDropWidget().get(0);
				Component child = adapter.getParentContainer();
				JSplitPane jtp = (JSplitPane) adaptable.getWidget();
				if (position.equals("left")) //$NON-NLS-1$
					jtp.setLeftComponent(child);
				else if (position.equals("right")) //$NON-NLS-1$
					jtp.setRightComponent(child);
				else if (position.equals("top")) //$NON-NLS-1$
					jtp.setTopComponent(child);
				else if (position.equals("bottom")) //$NON-NLS-1$
					jtp.setBottomComponent(child);
				adaptable.clearAllSelected();
				adapter.requestNewName();
				adapter.setSelected(true);
			}
			position = null;
		}
		return true;
	}

	@Override
	public boolean dragEnter(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragEnter(p);
		updatePosition(p);
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragExit(p);
		updatePosition(p);
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragOver(p);
		updatePosition(p);
		adaptable.setMascotLocation(p);
		return true;
	}
}
