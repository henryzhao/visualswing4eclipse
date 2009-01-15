package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.TextAreaEditor;
import org.dyno.visual.swing.widgets.WidgetPlugin;

public class JListEditorAdapter extends ComplexWidgetEditorAdapter {

	private IEditor iEditor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		if (iEditor == null) {
			iEditor = new TextAreaEditor();
		}
		return iEditor;
	}

	@Override
	public Object getWidgetValue(int x, int y) {
		ListModel model = ((JList) adaptable.getWidget()).getModel();
		int size = model.getSize();
		String items = ""; //$NON-NLS-1$
		for (int i = 0; i < size; i++) {
			if (i == 0)
				items += model.getElementAt(i);
			else
				items += "\n " + model.getElementAt(i); //$NON-NLS-1$
		}
		return items;
	}

	@Override
	public void setWidgetValue(Object value) {
		if (value == null)
			((JList) adaptable.getWidget()).setModel(new DefaultListModel());
		else {
			String items = (String) value;
			if (items.trim().length() == 0)
				((JList) adaptable.getWidget()).setModel(new DefaultListModel());
			else {
				BufferedReader br = new BufferedReader(new StringReader(items));
				DefaultListModel model = new DefaultListModel();
				String token;
				try {
					while ((token = br.readLine()) != null) {
						model.addElement(token.trim());
					}
					br.close();
				} catch (IOException e) {
					WidgetPlugin.getLogger().error(e);
				}
				((JList) adaptable.getWidget()).setModel(model);
			}
		}
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = adaptable.getWidget().getWidth();
		int h = adaptable.getWidget().getHeight();
		return new Rectangle(0, 0, w, h);
	}

}
