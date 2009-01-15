package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;
import java.util.StringTokenizer;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.IEditor;

public class JComboBoxEditorAdapter extends WidgetEditorAdapter {

	private LabelEditor editor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		if (editor == null) {
			editor = new LabelEditor();
		}
		return editor;
	}

	@Override
	public Object getWidgetValue(int x, int y) {
		ComboBoxModel model = ((JComboBox) adaptable.getWidget()).getModel();
		int size = model.getSize();
		String items = ""; //$NON-NLS-1$
		for (int i = 0; i < size; i++) {
			if (i == 0)
				items += model.getElementAt(i);
			else
				items += ", " + model.getElementAt(i); //$NON-NLS-1$
		}
		return items;
	}

	@Override
	public void setWidgetValue(Object value) {
		if (value == null)
			((JComboBox) adaptable.getWidget()).setModel(new DefaultComboBoxModel());
		else {
			String items = (String) value;
			if (items.trim().length() == 0)
				((JComboBox) adaptable.getWidget()).setModel(new DefaultComboBoxModel());
			else {
				StringTokenizer tokenizer = new StringTokenizer(items, ","); //$NON-NLS-1$
				DefaultComboBoxModel model = new DefaultComboBoxModel();
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					model.addElement(token.trim());
				}
				((JComboBox) adaptable.getWidget()).setModel(model);
			}
		}
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = adaptable.getWidget().getWidth();
		int h = adaptable.getWidget().getHeight();
		return new Rectangle(0, 0, w - 1, h - 1);
	}


}
