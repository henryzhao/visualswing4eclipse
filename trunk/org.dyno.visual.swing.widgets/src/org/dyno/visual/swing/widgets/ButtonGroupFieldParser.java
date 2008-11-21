package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.lang.reflect.Field;

import javax.swing.ButtonGroup;

import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.parser.spi.IFieldParser;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class ButtonGroupFieldParser implements IFieldParser {

	@Override
	public void parseField(CompilationUnit cunit, Component bean, Field field) {
		if (ButtonGroup.class.isAssignableFrom(field.getType())) {
			try {
				field.setAccessible(true);
				Object fieldValue = field.get(bean);
				String fieldName = field.getName();
				ButtonGroup group = (ButtonGroup) fieldValue;
				String widgetName = NamespaceManager.getInstance()
						.getNameFromFieldName(fieldName);
				ButtonGroupAdapter adapter = new ButtonGroupAdapter(widgetName,
						group);
				WidgetAdapter rootAdapter = WidgetAdapter
						.getWidgetAdapter(bean);
				rootAdapter.getInvisibles().add(adapter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean acceptTypeSignature(String sig) {
		return sig.indexOf("ButtonGroup") != -1;
	}

	@Override
	public boolean isDesigningField(IType type, IField field) {
		try {
			String sig = field.getTypeSignature();
			if (acceptTypeSignature(sig)) {
				String fieldName = field.getElementName();
				String getMethodName = "init"
						+ NamespaceManager.getInstance().getCapitalName(
								fieldName);
				IMethod method = type.getMethod(getMethodName, new String[0]);
				if (method != null && method.exists()) {
					return true;
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public boolean removeField(IType type, String fieldName, IProgressMonitor monitor) {
		String name = NamespaceManager.getInstance().getFieldName(fieldName);
		IMethod method = type.getMethod("init"+ NamespaceManager.getInstance().getCapitalName(name), new String[0]);
		if (method != null && method.exists()) {
			try {
				method.delete(true, monitor);
				return true;
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
