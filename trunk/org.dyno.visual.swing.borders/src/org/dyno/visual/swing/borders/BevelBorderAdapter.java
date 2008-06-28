package org.dyno.visual.swing.borders;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;

import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.FieldProperty;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jface.action.IAction;

public class BevelBorderAdapter extends BorderAdapter {
	@SuppressWarnings("unchecked")
	@Override
	public Class getBorderClass() {
		return BevelBorder.class;
	}

	@Override
	public String getBorderName() {
		return "BevelBorder";
	}

	@Override
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		FieldProperty bevelTypeProperty = new FieldProperty("bevelType", "bevelType", BevelBorder.class, new ItemProviderLabelProviderFactory(new BevelBorderTypeItems()), new ItemProviderCellEditorFactory(new BevelBorderTypeItems()));
		FieldProperty highlightOuterProperty = new FieldProperty("highlightOuter", "highlightOuter", BevelBorder.class);
		FieldProperty highlightInnerProperty = new FieldProperty("highlightInner", "highlightInner", BevelBorder.class);
		FieldProperty shadowInnerProperty = new FieldProperty("shadowInner", "shadowInner", BevelBorder.class);
		FieldProperty shadowOuterProperty = new FieldProperty("shadowOuter", "shadowOuter", BevelBorder.class);
		return new IWidgetPropertyDescriptor[] {bevelTypeProperty, 
				highlightOuterProperty, 
				highlightInnerProperty, 
				shadowInnerProperty,
				shadowOuterProperty
			};
	}

	@Override
	public IAction getContextAction(JComponent widget) {
		return new BevelBorderSwitchAction(widget);
	}

	@Override
	public Object newInstance(Object bean) {
		return BorderFactory.createBevelBorder(BevelBorder.LOWERED);
	}

}
