package org.dyno.visual.swing.types.renderer;

import javax.swing.ListModel;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ListItemLabelProvider extends LabelProvider {

	public ListItemLabelProvider() {
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element == null)
			return "null";
		if (element instanceof ListModel) {
			StringBuilder builder = new StringBuilder();
			ListModel model = (ListModel) element;
			int size = model.getSize();
			for (int i = 0; i < size; i++) {
				Object object = model.getElementAt(i);
				if (i != 0) {
					builder.append(", ");
				}
				if (object == null) {
					builder.append("null");
				} else {
					builder.append(object.toString());
				}
			}
			return builder.toString();
		} else {
			return element.toString();
		}
	}
}
