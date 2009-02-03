package org.dyno.visual.swing.widgets.editoradapter;

import javax.swing.JFrame;

public class JFrameEdtiorAdapter extends JWindowEditorAdapter {
	@Override
	public Object getWidgetValue() {
		JFrame jif = (JFrame) adaptable.getWidget();
		return jif.getTitle();
	}

	@Override
	public void setWidgetValue(Object value) {
		JFrame jif = (JFrame) adaptable.getWidget();
		jif.setTitle((String) value);
	}
}
