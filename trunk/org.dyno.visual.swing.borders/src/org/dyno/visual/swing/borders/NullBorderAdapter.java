package org.dyno.visual.swing.borders;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;

public class NullBorderAdapter extends BorderAdapter {

	@Override
	public IAction getContextAction(JComponent widget) {
		return new NullBorderSwitchAction(widget);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class getBorderClass() {
		return null;
	}

	@Override
	public String getBorderName() {
		return "null";
	}

	@Override
	public Object newInstance(Object bean) {
		return null;
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		return "null";
	}
}
