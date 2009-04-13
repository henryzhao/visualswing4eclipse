package org.dyno.visual.swing.parser.adapters;

import java.awt.Component;

import org.dyno.visual.swing.base.WidgetProperty;
import org.dyno.visual.swing.parser.spi.IPropertyCodeGenerator;
import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class WidgetPropertyParser implements IPropertyCodeGenerator, IAdaptableContext {
	private WidgetProperty property;
	
	public String getJavaCode(Object bean, ImportRewrite imports) {
		if (bean instanceof Component) {
			Component comp = (Component) bean;
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(comp);
			if (adapter != null) {
				StringBuilder builder = new StringBuilder();
				Object value = property.getFieldValue(bean);
				ICodeGen gen = property.getCodeGenerator();
				if (gen != null && value != null) {
					String initCode = gen.getInitJavaCode(value, imports);
					if (initCode != null)
						builder.append(initCode);
				}
				if (!adapter.isRoot()) {
					String name = adapter.getID();
					builder.append(name + ".");
				}
				String code;
				if (gen != null) {
					code = value == null ? "null" : gen.getJavaCode(value, imports);
				} else {
					code = value == null ? "null" : value.toString();
				}
				if (code != null) {
					builder.append(property.getSetName() + "(");
					builder.append(code);
					builder.append(");\n");
				}
				return builder.toString();
			} else
				return null;
		} else
			return null;
	}
	
	public void setAdaptable(IAdaptable adaptable) {
		this.property = (WidgetProperty) adaptable;
	}

}
