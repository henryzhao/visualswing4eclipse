package org.dyno.visual.swing.borders;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.MatteBorder;

import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.FieldProperty;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jface.action.IAction;

public class MatteBorderAdapter extends BorderAdapter {

	@SuppressWarnings("unchecked")
	@Override
	public Class getBorderClass() {
		return MatteBorder.class;
	}

	@Override
	public String getBorderName() {
		return "MatteBorder";
	}

	@Override
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		InsetsProperty insetsProperty = new InsetsProperty(){
			@SuppressWarnings("unchecked")
			@Override
			protected Class getBorderClass() {
				return MatteBorder.class;
			}};
		FieldProperty colorProperty = new FieldProperty("color", "color", MatteBorder.class);
		FieldProperty tileIconProperty = new FieldProperty("tileIcon", "tileIcon", MatteBorder.class);
		return new IWidgetPropertyDescriptor[] { insetsProperty, colorProperty, tileIconProperty };
	}


	@Override
	public IAction getContextAction(JComponent widget) {
		return new MatteBorderSwitchAction(widget);
	}

	@Override
	public Object newInstance(Object bean) {
		return BorderFactory.createMatteBorder(0, 0, 0, 0, Color.black);
	}

}
