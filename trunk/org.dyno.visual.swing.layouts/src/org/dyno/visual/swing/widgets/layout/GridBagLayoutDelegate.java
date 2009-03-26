package org.dyno.visual.swing.widgets.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.event.MouseInputAdapter;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class GridBagLayoutDelegate extends MouseInputAdapter implements
		IAdaptableContext {
	private GridBagLayoutAdapter adapter;

	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adapter = (GridBagLayoutAdapter) adaptable;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseClicked(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Container container = adapter.getContainer();
		WidgetAdapter widgetAdapter = WidgetAdapter.getWidgetAdapter(container);
		if (widgetAdapter.isFocused() && dragging) {
			GridBagLayout layout = (GridBagLayout) container.getLayout();
			int[][] row_cols = layout.getLayoutDimensions();
			if (row_cols == null)
				return;
			if (draggingX) {
				int[] widths = layout.columnWidths;
				if (widths == null)
					widths = row_cols[0];
				if(index == -1){
					widths[0] += prev - e.getX();
					widths[0] += prev - e.getX();
				} else if (index == widths.length - 1) {
					widths[index] += e.getX() - prev;
					widths[index] += e.getX() - prev;
				} else {
					widths[index] += e.getX()-prev;
					widths[index + 1] -= e.getX() - prev;
				}
				layout.columnWidths = widths;
				layout.layoutContainer(container);
				prev = e.getX();
				widgetAdapter.repaintDesigner();
				e.consume();
			} else {
				int[] heights = layout.rowHeights;
				if (heights == null)
					heights = row_cols[1];
				if(index == -1){
					heights[0] += prev - e.getY();
					heights[0] += prev - e.getY();
				} else if (index == heights.length - 1) {
					heights[index] += e.getY() - prev;
					heights[index] += e.getY() - prev;
				} else {
					heights[index] += e.getY()-prev;
					heights[index + 1] -= e.getY() - prev;
				}
				layout.rowHeights = heights;
				layout.layoutContainer(container);
				prev = e.getY();
				widgetAdapter.repaintDesigner();
				e.consume();				
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseExited(e);
	}

	private boolean isInDropDown(Point p) {
		Container container = adapter.getContainer();
		CompositeAdapter parentAdapter = (CompositeAdapter) WidgetAdapter
				.getWidgetAdapter(container);
		int count = parentAdapter.getChildCount();
		for (int i = 0; i < count; i++) {
			Component child = parentAdapter.getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			if (childAdapter.isSelected()) {
				Rectangle rect = child.getBounds();
				Rectangle thumbRect = new Rectangle(rect.x + rect.width - 18,
						rect.y - 4, 10, 8);
				if (thumbRect.contains(p)) {
					return true;
				}
			}
		}
		return false;
	}

	private static final int DISTANCE_THRESHOLD = 4;

	private int detectCursorType(MouseEvent e) {
		Container container = adapter.getContainer();
		WidgetAdapter widgetAdapter = WidgetAdapter.getWidgetAdapter(container);
		if (widgetAdapter.isFocused()) {
			GridBagLayout layout = (GridBagLayout) container.getLayout();
			int[][] row_cols = layout.getLayoutDimensions();
			if (row_cols == null)
				return Cursor.DEFAULT_CURSOR;
			Point origin = layout.getLayoutOrigin();
			int[] widths = layout.columnWidths;
			if (widths == null)
				widths = row_cols[0];
			if (widths != null) {
				int x = origin != null ? origin.x : 0;
				if (e.getX() > x - DISTANCE_THRESHOLD
						&& e.getX() < x + DISTANCE_THRESHOLD)
					return Cursor.E_RESIZE_CURSOR;
				for (int i = 0; i < widths.length; i++) {					
					if (e.getX() > x + widths[i]- DISTANCE_THRESHOLD
							&& e.getX() < x+ widths[i] + DISTANCE_THRESHOLD) {
						return Cursor.E_RESIZE_CURSOR;
					}
					x += widths[i];
				}
				if (e.getX() > x - DISTANCE_THRESHOLD
						&& e.getX() < x + DISTANCE_THRESHOLD) {
					return Cursor.E_RESIZE_CURSOR;
				}
			}
			int[] heights = layout.rowHeights;
			if (heights == null)
				heights = row_cols[1];
			if (heights != null) {
				int y = origin != null ? origin.y : 0;
				if (e.getY() > y - DISTANCE_THRESHOLD
						&& e.getY() < y + DISTANCE_THRESHOLD) {
					return Cursor.N_RESIZE_CURSOR;
				}
				for (int i = 0; i < heights.length; i++) {
					if (e.getY() > y +heights[i]- DISTANCE_THRESHOLD
							&& e.getY() < y +heights[i]+ DISTANCE_THRESHOLD) {
						return Cursor.N_RESIZE_CURSOR;
					}
					y += heights[i];
				}
				if (e.getY() > y - DISTANCE_THRESHOLD
						&& e.getY() < y + DISTANCE_THRESHOLD) {
					return Cursor.N_RESIZE_CURSOR;
				}
			}
		}
		return Cursor.DEFAULT_CURSOR;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Container container = adapter.getContainer();
		WidgetAdapter widgetAdapter = WidgetAdapter.getWidgetAdapter(container);
		int type = detectCursorType(e);
		if (type != Cursor.DEFAULT_CURSOR && isInDropDown(e.getPoint()))
			type = Cursor.DEFAULT_CURSOR;
		widgetAdapter.setCursorType(type);
		if (type != Cursor.DEFAULT_CURSOR)
			e.consume();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Container container = adapter.getContainer();
		WidgetAdapter widgetAdapter = WidgetAdapter.getWidgetAdapter(container);
		if (widgetAdapter.isFocused()) {
			GridBagLayout layout = (GridBagLayout) container.getLayout();
			int[][] row_cols = layout.getLayoutDimensions();
			if (row_cols == null)
				return;
			Point origin = layout.getLayoutOrigin();
			int[] widths = layout.columnWidths;
			if (widths == null)
				widths = row_cols[0];
			if (widths != null) {
				int x = origin != null ? origin.x : 0;
				if (e.getX() > x - DISTANCE_THRESHOLD
						&& e.getX() < x + DISTANCE_THRESHOLD
						&& !isInDropDown(e.getPoint())) {
					dragging = true;
					draggingX = true;
					index = -1;
					prev = x;
					e.consume();
					return;
				}
				for (int i = 0; i < widths.length; i++) {
					if (e.getX() > x +widths[i]- DISTANCE_THRESHOLD
							&& e.getX() < x + widths[i]+DISTANCE_THRESHOLD
							&& !isInDropDown(e.getPoint())) {
						widgetAdapter.setCursorType(Cursor.E_RESIZE_CURSOR);
						dragging = true;
						draggingX = true;
						index = i;
						prev = x+widths[i];
						e.consume();
						return;
					}
					x += widths[i];
				}
			}
			int[] heights = layout.rowHeights;
			if (heights == null)
				heights = row_cols[1];
			if (heights != null) {
				int y = origin != null ? origin.y : 0;
				if (e.getY() > y - DISTANCE_THRESHOLD
						&& e.getY() < y + DISTANCE_THRESHOLD
						&& !isInDropDown(e.getPoint())) {
					dragging = true;
					draggingX = false;
					index = -1;
					prev = y;
					e.consume();
					return;
				}
				for (int i = 0; i < heights.length; i++) {
					if (e.getY() > y +heights[i]- DISTANCE_THRESHOLD
							&& e.getY() < y +heights[i]+ DISTANCE_THRESHOLD
							&& !isInDropDown(e.getPoint())) {
						widgetAdapter.setCursorType(Cursor.N_RESIZE_CURSOR);
						dragging = true;
						draggingX = false;
						index = i;
						prev = y+heights[i];
						e.consume();
						return;
					}
					y += heights[i];
				}
			}
			widgetAdapter.setCursorType(Cursor.DEFAULT_CURSOR);
		}
	}

	private boolean dragging;
	private boolean draggingX;
	private int index;
	private int prev;

	@Override
	public void mouseReleased(MouseEvent e) {
		dragging = false;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		super.mouseWheelMoved(e);
	}

}
