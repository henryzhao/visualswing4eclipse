package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.border.Border;

import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.IEditor;

public class JPanelEditorAdapter extends CompositeEdtiorAdapter {

	@Override
	public Object getWidgetValue(int x, int y) {
		JPanel panel = (JPanel) adaptable.getWidget();
		Border border = panel.getBorder();
		if (border != null) {
			BorderAdapter borderAdapter = BorderAdapter.getBorderAdapter(border.getClass());
			if(borderAdapter!=null){
				return borderAdapter.getWidgetValue(panel);
			}
		} 
		return null;
	}

	@Override
	public void setWidgetValue(Object value) {
		JPanel panel = (JPanel) adaptable.getWidget();
		Border border = panel.getBorder();
		if (border != null) {
			BorderAdapter borderAdapter = BorderAdapter.getBorderAdapter(border.getClass());
			if(borderAdapter!=null){
				borderAdapter.setWidgetValue(panel, value);
			}
		} 
	}

	@Override
	public IEditor getEditorAt(int x, int y) {
		JPanel panel = (JPanel) adaptable.getWidget();
		Border border = panel.getBorder();
		if (border != null) {
			BorderAdapter borderAdapter = BorderAdapter.getBorderAdapter(border.getClass());
			if(borderAdapter!=null){
				return borderAdapter.getEditorAt(panel, x, y);
			}
		} 
		return null;
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		JPanel panel = (JPanel) adaptable.getWidget();
		Border border = panel.getBorder();
		if (border != null) {
			BorderAdapter borderAdapter = BorderAdapter.getBorderAdapter(border.getClass());
			if(borderAdapter!=null){
				return borderAdapter.getEditorBounds(panel, x, y);
			}
		} 
		return null;
	}
}
