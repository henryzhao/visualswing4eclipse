package org.dyno.visual.swing.borders;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jface.action.IAction;

public class EmptyBorderAdapter extends BorderAdapter {

	@SuppressWarnings("unchecked")
	@Override
	public Class getBorderClass() {
		return EmptyBorder.class;
	}

	@Override
	public String getBorderName() {
		return "EmptyBorder";
	}

	@Override
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		InsetsProperty insetsProperty = new InsetsProperty() {
			@SuppressWarnings("unchecked")
			@Override
			protected Class getBorderClass() {
				return EmptyBorder.class;
			}
		};
		return new IWidgetPropertyDescriptor[] { insetsProperty };
	}

	@Override
	public IAction getContextAction(JComponent widget) {
		return new EmptyBorderSwitchAction(widget);
	}

	@Override
	public Object newInstance(Object bean) {
		return BorderFactory.createEmptyBorder();
	}

}
