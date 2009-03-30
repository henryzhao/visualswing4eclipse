package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.editors.TableModelEditor;

public class JTableEditorAdapter extends ComplexWidgetEditorAdapter {
	@Override
	public IEditor getEditorAt() {
		CompositeAdapter parent = adaptable.getParentAdapter();
		if (parent != null && parent.getWidget() instanceof JScrollPane)
			return new TableModelEditor(adaptable, (JScrollPane) parent.cloneWidget());
		else
			return null;
	}

	@Override
	public Rectangle getEditorBounds() {
		CompositeAdapter parent = adaptable.getParentAdapter();
		if (parent != null && parent.getWidget() instanceof JScrollPane) {
			Rectangle bounds = parent.getWidget().getBounds();
			bounds.x = 0;
			bounds.y = 0;
			bounds=SwingUtilities.convertRectangle(parent.getWidget(), bounds, adaptable.getWidget());
			return bounds;
		}
		Rectangle bounds = adaptable.getWidget().getBounds();
		bounds.x = 0;
		bounds.y = 0;
		return bounds;
	}

	@Override
	public Object getWidgetValue() {
		JTable table = (JTable) adaptable.getWidget();
		return table.getModel();
	}

	@Override
	public void setWidgetValue(Object value) {
		JTable table = (JTable) adaptable.getWidget();
		table.setModel((TableModel) value);
	}

}
