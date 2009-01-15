package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.editors.TreeModelEditor;

public class JTreeEditorAdapter extends ComplexWidgetEditorAdapter {

	@Override
	public IEditor getEditorAt(int x, int y) {
		CompositeAdapter parent = adaptable.getParentAdapter();
		if (parent != null && parent.getWidget() instanceof JScrollPane)
			return new TreeModelEditor((JScrollPane) parent.cloneWidget());
		else
			return null;
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		CompositeAdapter parent = adaptable.getParentAdapter();
		if (parent != null && parent.getWidget() instanceof JScrollPane) {
			Rectangle bounds = parent.getWidget().getBounds();
			bounds.x = 0;
			bounds.y = 0;
			return bounds;
		}
		Rectangle bounds = adaptable.getWidget().getBounds();
		bounds.x = 0;
		bounds.y = 0;
		return bounds;
	}

	@Override
	public Object getWidgetValue(int x, int y) {
		JTree tree = (JTree) adaptable.getWidget();
		return tree.getModel();
	}

	@Override
	public void setWidgetValue(Object value) {
		JTree tree = (JTree) adaptable.getWidget();
		tree.setModel((TreeModel) value);
	}

}
