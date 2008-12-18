package org.dyno.visual.swing.parser.adapters;

import java.awt.CardLayout;
import java.awt.Component;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class CardLayoutParser extends LayoutParser {


	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		CardLayout layout = (CardLayout) layoutAdapter.getContainer().getLayout();
		int hgap = layout.getHgap();
		int vgap = layout.getVgap();
		String strLayoutName= imports.addImport("java.awt.CardLayout");
		if (hgap != 0 || vgap != 0) {
			return "new "+strLayoutName+"(" + hgap + ", " + vgap + ")";
		} else {
			return "new "+strLayoutName+"()";
		}
	}

	@Override
	protected String getChildConstraints(Component child, ImportRewrite imports) {
		return "\""+WidgetAdapter.getWidgetAdapter(child).getName()+"\"";
	}

}
