package org.dyno.visual.swing.parser.adapters;

import java.awt.Component;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.FieldProperty;
import org.dyno.visual.swing.base.TypeAdapter;
import org.dyno.visual.swing.parser.spi.IPropertyCodeGenerator;
import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

@SuppressWarnings("unchecked")
public class FieldPropertyParser implements IPropertyCodeGenerator, IAdaptableContext {
	private FieldProperty property;
	
	public String getJavaCode(Object bean, ImportRewrite imports) {
		if (bean instanceof Component) {
			Component comp = (Component) bean;
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(comp);
			if (adapter != null) {
				StringBuilder builder = new StringBuilder();
				Class typeClass = property.getPropertyType();
				TypeAdapter typeAdapter = ExtensionRegistry.getTypeAdapter(typeClass);
				Object value = property.getFieldValue(bean);
				if (typeAdapter != null && typeAdapter.getCodegen() != null) {
					String initCode = typeAdapter.getCodegen().getInitJavaCode(value, imports);
					if (initCode != null)
						builder.append(initCode);
				}
				if (!adapter.isRoot()) {
					String name = adapter.getID();
					builder.append(name + ".");
				}
				builder.append(property.getFieldName() + "=");
				if (typeAdapter != null && typeAdapter.getCodegen() != null) {
					if (value == null) {
						builder.append("null");
					} else {
						builder.append(typeAdapter.getCodegen().getJavaCode(value, imports));
					}
				} else {
					builder.append(value == null ? "null" : value.toString());
				}
				builder.append(";\n");
				return builder.toString();
			} else
				return null;
		} else
			return null;
	}

	
	public void setAdaptable(IAdaptable adaptable) {
		this.property = (FieldProperty) adaptable;
	}

}
