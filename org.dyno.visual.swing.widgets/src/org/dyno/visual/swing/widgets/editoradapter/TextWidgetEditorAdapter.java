package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.WidgetPlugin;

public class TextWidgetEditorAdapter extends WidgetEditorAdapter {
	private static final int HOR_TEXT_PAD = 20;
	private static final int VER_TEXT_PAD = 4;
	private LabelEditor editor;

	@Override
	public IEditor getEditorAt() {
		if (editor == null) {
			editor = new LabelEditor();
		}
		return editor;
	}

	@Override
	public Object getWidgetValue() {
		return getText(adaptable.getWidget());
	}
	private PropertyDescriptor getTextProperty() {
		try {
			return new PropertyDescriptor("text", adaptable.getWidgetClass()); //$NON-NLS-1$
		} catch (IntrospectionException e) {
			WidgetPlugin.getLogger().error(e);
			return null;
		}
	}
	@Override
	public void setWidgetValue(Object value) {
		setText(adaptable.getWidget(), value == null ? "" : value.toString()); //$NON-NLS-1$
	}
	private String getText(Component jc) {
		PropertyDescriptor textProperty = getTextProperty();
		try {
			return (String) textProperty.getReadMethod().invoke(jc);
		} catch (Exception e) {
			WidgetPlugin.getLogger().error(e);
			return null;
		}
	}

	private void setText(Component jc, String text) {
		PropertyDescriptor textProperty = getTextProperty();
		try {
			textProperty.getWriteMethod().invoke(jc, text);
		} catch (Exception e) {
			WidgetPlugin.getLogger().error(e);
		}
	}
	@Override
	public Rectangle getEditorBounds() {
		int w = adaptable.getWidget().getWidth();
		int h = adaptable.getWidget().getHeight();
		Component widget = adaptable.getWidget();
		FontMetrics fm = widget.getFontMetrics(widget.getFont());
		int fh = fm.getHeight() + VER_TEXT_PAD;
		int fw = fm.stringWidth(getText(adaptable.getWidget())) + HOR_TEXT_PAD;
		int fx = (w - fw) / 2;
		int fy = (h - fh) / 2;
		return new Rectangle(fx, fy, fw, fh);
	}
}
