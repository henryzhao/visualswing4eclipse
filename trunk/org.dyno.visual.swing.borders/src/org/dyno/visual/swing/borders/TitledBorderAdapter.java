package org.dyno.visual.swing.borders;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.FieldProperty;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jface.action.IAction;

public class TitledBorderAdapter extends BorderAdapter {
	@SuppressWarnings("unchecked")
	@Override
	public Class getBorderClass() {
		return TitledBorder.class;
	}

	@Override
	public String getBorderName() {
		return "TitledBorder";
	}

	@Override
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		FieldProperty title = new FieldProperty("title", "title", TitledBorder.class);
		FieldProperty border = new FieldProperty("border", "border", TitledBorder.class);
		FieldProperty titlePosition = new FieldProperty("titlePosition", "titlePosition", TitledBorder.class, new ItemProviderLabelProviderFactory(
				new TitlePositionItems()), new ItemProviderCellEditorFactory(new TitlePositionItems()));
		FieldProperty titleJustification = new FieldProperty("titleJustification", "titleJustification", TitledBorder.class,
				new ItemProviderLabelProviderFactory(new TitleJustificationItems()), new ItemProviderCellEditorFactory(new TitleJustificationItems()));
		FieldProperty titleFont = new FieldProperty("titleFont", "titleFont", TitledBorder.class);
		FieldProperty titleColor = new FieldProperty("titleColor", "titleColor", TitledBorder.class);
		return new IWidgetPropertyDescriptor[] { title, border, titlePosition, titleJustification, titleFont, titleColor };
	}

	@Override
	public IAction getContextAction(JComponent widget) {
		return new TitledBorderSwitchAction(widget);
	}

	@Override
	public Object newInstance(Object bean) {
		return BorderFactory.createTitledBorder("Border Title");
	}

}
