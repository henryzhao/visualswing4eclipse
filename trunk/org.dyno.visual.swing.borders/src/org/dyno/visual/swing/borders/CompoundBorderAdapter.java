package org.dyno.visual.swing.borders;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.CompoundBorder;

import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.FieldProperty;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jface.action.IAction;

public class CompoundBorderAdapter extends BorderAdapter {

	@SuppressWarnings("unchecked")
	@Override
	public Class getBorderClass() {
		return CompoundBorder.class;
	}

	@Override
	public String getBorderName() {
		return "CompoundBorder";
	}

	@Override
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		FieldProperty outside = new FieldProperty("outsideBorder", "outsideBorder", CompoundBorder.class);
		FieldProperty inside = new FieldProperty("insideBorder", "insideBorder", CompoundBorder.class);
		return new IWidgetPropertyDescriptor[] {outside, inside};
	}

	@Override
	public IAction getContextAction(JComponent widget) {
		return new CompoundBorderSwitchAction(widget);
	}

	@Override
	public Object newInstance(Object bean) {
		return BorderFactory.createCompoundBorder();
	}

}
