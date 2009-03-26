package org.dyno.visual.swing.widgets.editoradapter;

import javax.swing.JDialog;

public class JDialogEdtiorAdapter extends JWindowEditorAdapter {
	@Override
	public Object getWidgetValue() {
		JDialog jif = (JDialog) adaptable.getWidget();
		return jif.getTitle();
	}

	@Override
	public void setWidgetValue(Object value) {
		JDialog jif = (JDialog) adaptable.getWidget();
		jif.setTitle((String) value);
	}
}
