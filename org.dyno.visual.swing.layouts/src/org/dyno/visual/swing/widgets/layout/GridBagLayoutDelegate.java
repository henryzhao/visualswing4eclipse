package org.dyno.visual.swing.widgets.layout;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.event.MouseInputAdapter;

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
		if (widgetAdapter.isHovered() && dragging) {
			GridBagLayout layout = (GridBagLayout) container.getLayout();
			int[][] row_cols = layout.getLayoutDimensions();
			if (row_cols == null)
				return;
			if (draggingX) {
				int[] widths = layout.columnWidths;
				if (widths == null)
					widths = row_cols[0];
				widths[index] += e.getX() - prev;
				layout.columnWidths = widths;
				layout.layoutContainer(container);
				prev = e.getX();
				widgetAdapter.repaintDesigner();
				e.consume();
			} else {
				int[] heights = layout.rowHeights;
				if (heights == null)
					heights = row_cols[1];
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

	@Override
	public void mouseMoved(MouseEvent e) {
		Container container = adapter.getContainer();
		WidgetAdapter widgetAdapter = WidgetAdapter.getWidgetAdapter(container);
		if (widgetAdapter.isHovered()) {
			GridBagLayout layout = (GridBagLayout) container.getLayout();
			int[][] row_cols = layout.getLayoutDimensions();
			if (row_cols == null)
				return;
			Point origin = layout.getLayoutOrigin();
			int[] widths = layout.columnWidths;
			if (widths == null)
				widths = row_cols[0];
			if (widths != null) {
				int x = origin!=null?origin.x:0;
				for (int i = 0; i < widths.length; i++) {
					if(e.getX()>x-2&&e.getX()<x+2){
						widgetAdapter.setCursorType(Cursor.E_RESIZE_CURSOR);
						e.consume();
						return;
					}
					x += widths[i];
				}
				if(e.getX()>x-2&&e.getX()<x+2){
					widgetAdapter.setCursorType(Cursor.E_RESIZE_CURSOR);
					e.consume();
					return;
				}
			}
			int[] heights = layout.rowHeights;
			if (heights == null)
				heights = row_cols[1];
			if (heights != null) {
				int y = origin!=null?origin.y:0;
				for (int i = 0; i < heights.length; i++) {
					if (e.getY() > y - 2 && e.getY() < y + 2) {
						widgetAdapter.setCursorType(Cursor.N_RESIZE_CURSOR);
						e.consume();
						return;
					}
					y += heights[i];
				}
				if (e.getY() > y - 2 && e.getY() < y + 2) {
					widgetAdapter.setCursorType(Cursor.N_RESIZE_CURSOR);
					e.consume();
					return;
				}
			}
			widgetAdapter.setCursorType(Cursor.DEFAULT_CURSOR);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Container container = adapter.getContainer();
		WidgetAdapter widgetAdapter = WidgetAdapter.getWidgetAdapter(container);
		if (widgetAdapter.isHovered()) {
			GridBagLayout layout = (GridBagLayout) container.getLayout();
			int[][] row_cols = layout.getLayoutDimensions();
			if (row_cols == null)
				return;
			Point origin = layout.getLayoutOrigin();
			int[] widths = layout.columnWidths;
			if (widths == null)
				widths = row_cols[0];
			if (widths != null) {
				int x = origin!=null?origin.x:0;
				for (int i = 0; i < widths.length; i++) {
					if(e.getX()>x-2&&e.getX()<x+2){
						widgetAdapter.setCursorType(Cursor.E_RESIZE_CURSOR);
						dragging=true;
						draggingX=true;
						index=i;
						prev=x;
						e.consume();
						return;
					}
					x += widths[i];
				}
				if(e.getX()>x-2&&e.getX()<x+2){
					widgetAdapter.setCursorType(Cursor.E_RESIZE_CURSOR);
					dragging=true;
					draggingX=true;
					index=widths.length-1;
					prev=x;
					e.consume();
					return;
				}
			}
			int[] heights = layout.rowHeights;
			if (heights == null)
				heights = row_cols[1];
			if (heights != null) {
				int y = origin!=null?origin.y:0;
				for (int i = 0; i < heights.length; i++) {
					if (e.getY() > y - 2 && e.getY() < y + 2) {
						widgetAdapter.setCursorType(Cursor.N_RESIZE_CURSOR);
						e.consume();
						return;
					}
					y += heights[i];
				}
				if (e.getY() > y - 2 && e.getY() < y + 2) {
					widgetAdapter.setCursorType(Cursor.N_RESIZE_CURSOR);
					e.consume();
					return;
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
		// TODO Auto-generated method stub
		super.mouseReleased(e);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		super.mouseWheelMoved(e);
	}

}
