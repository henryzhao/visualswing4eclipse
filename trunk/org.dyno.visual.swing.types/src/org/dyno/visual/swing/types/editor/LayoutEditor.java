package org.dyno.visual.swing.types.editor;

import org.dyno.visual.swing.base.FactoryEditor;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.types.items.LayoutItems;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class LayoutEditor extends FactoryEditor {
	public LayoutEditor() {
		super(new LayoutItems());
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		if(value==null)
			return null;
		BorderAdapter adapter = BorderAdapter.getBorderAdapter(value.getClass());
		return adapter.getInitJavaCode(value, imports);
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if(value==null)
			return "null";
		BorderAdapter adapter = BorderAdapter.getBorderAdapter(value.getClass());
		return adapter.getJavaCode(value, imports);
	}
}
