package org.dyno.visual.swing.borders;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.LineBorder;

import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.FieldProperty;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jface.action.IAction;


public class LineBorderAdapter extends BorderAdapter{
	@Override
	public IAction getContextAction(JComponent widget) {
		return new LineBorderSwitchAction(widget);
	}

	@Override
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		FieldProperty thicknessProperty=new FieldProperty("thickness", "thickness", LineBorder.class);
		FieldProperty lineColorProperty=new FieldProperty("lineColor", "lineColor", LineBorder.class);
		FieldProperty roundedCornersProperty = new FieldProperty("roundedCorners","roundedCorners", LineBorder.class);		
		return new IWidgetPropertyDescriptor[]{thicknessProperty, lineColorProperty, roundedCornersProperty};
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class getBorderClass() {
		return LineBorder.class;
	}

	@Override
	public String getBorderName() {
		return "LineBorder";
	}

	@Override
	public Object newInstance(Object bean) {
		return BorderFactory.createLineBorder(Color.black);
	}
}
