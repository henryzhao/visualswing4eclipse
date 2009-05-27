package org.dyno.visual.swing.widgets.layout.constraints;

import java.awt.Component;

import org.dyno.visual.swing.base.PropertyAdapter;
import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;

public class CommonConstraintsProperty extends PropertyAdapter {
	protected LayoutAdapter adapter;
	protected Component widget;	
	protected ICellEditorFactory editorFactory;
	protected ILabelProviderFactory labelFactory;
	protected String name;
	public CommonConstraintsProperty(LayoutAdapter adapter, Component widget, ICellEditorFactory eFactory, ILabelProviderFactory lFactory, String name){
		this.adapter = adapter;
		this.widget = widget;
		this.editorFactory = eFactory;
		this.labelFactory = lFactory;
		this.name = name ;
	}
	@Override
	public String getCategory() {
		return "Location";
	}
	@Override
	public String getDescription() {
		return name;
	}
	@Override
	public String getDisplayName() {
		return name;
	}
	@Override
	public Object getId() {
		return "layout."+name;
	}
	@Override
	public ILabelProvider getLabelProvider() {
		return labelFactory.getLabelProvider();
	}
	@Override
	public Object getPropertyValue(IStructuredSelection bean) {
		return editorFactory.encodeValue(adapter.getChildConstraints(widget));
	}
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return editorFactory.createPropertyEditor(widget, parent);
	}
}
