package org.dyno.visual.swing.borders;

import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.FieldProperty;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jface.action.IAction;

public class SoftBevelBorderAdapter extends BorderAdapter {
	@SuppressWarnings("unchecked")
	@Override
	public Class getBorderClass() {
		return SoftBevelBorder.class;
	}

	@Override
	public String getBorderName() {
		return "SoftBevelBorder";
	}

	@Override
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		FieldProperty bevelTypeProperty = new FieldProperty("bevelType", "bevelType", SoftBevelBorder.class, new ItemProviderLabelProviderFactory(new BevelBorderTypeItems()), new ItemProviderCellEditorFactory(new BevelBorderTypeItems()));
		FieldProperty highlightOuterProperty = new FieldProperty("highlightOuter", "highlightOuter", SoftBevelBorder.class);
		FieldProperty highlightInnerProperty = new FieldProperty("highlightInner", "highlightInner", SoftBevelBorder.class);
		FieldProperty shadowInnerProperty = new FieldProperty("shadowInner", "shadowInner", SoftBevelBorder.class);
		FieldProperty shadowOuterProperty = new FieldProperty("shadowOuter", "shadowOuter", SoftBevelBorder.class);
		return new IWidgetPropertyDescriptor[] {bevelTypeProperty, 
				highlightOuterProperty, 
				highlightInnerProperty, 
				shadowInnerProperty,
				shadowOuterProperty
			};
	}

	@Override
	public IAction getContextAction(JComponent widget) {
		return new SoftBevelBorderSwitchAction(widget);
	}

	@Override
	public Object newInstance(Object bean) {
		return new SoftBevelBorder(BevelBorder.LOWERED);
	}

}
