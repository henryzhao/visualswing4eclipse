package org.dyno.visual.swing.parser.adapters;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JToolBar;

import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JButtonParser extends WidgetParser {

	@Override
	protected String generateSetCode(ImportRewrite imports, IWidgetPropertyDescriptor property) {
		JButton jButton = (JButton) adaptable.getWidget();
		Container parent = jButton.getParent();
		if (property.getId().equals("border") && parent instanceof JToolBar) {
			return null;
		} else if (property.getId().equals("defaultCapable") && parent instanceof JToolBar) {
			return null;
		} else
			return super.generateSetCode(imports, property);
	}

}
