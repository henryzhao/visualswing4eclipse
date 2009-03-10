package org.dyno.visual.swing.base;

import java.awt.Component;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.dyno.visual.swing.plugin.spi.IValueParser;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.undo.SetValueOperation;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;


@SuppressWarnings("unchecked")
public class BeanDescriptorProperty extends PropertyAdapter{
	private Object lastValue;
	private Object default_value;

	private ICellEditorFactory editorFactory;
	private ILabelProviderFactory labelFactory;
	private PropertyDescriptor property;

	private IStructuredSelection bean;
	private String category;
	private TypeAdapter typeAdapter;
	public BeanDescriptorProperty(PropertyDescriptor pd) {
		this.property = pd;
		Class<?> type = getPropertyType();
		typeAdapter = ExtensionRegistry.getTypeAdapter(type);
		if (typeAdapter != null) {
			labelFactory = typeAdapter.getRenderer();
			editorFactory = typeAdapter.getEditor();
		}
	}

	@Override
	public void init(IConfigurationElement config, Class beanClass) {
	}

	public void setBean(IStructuredSelection bean) {
		this.bean = bean;
	}

	
	@Override
	public Object getPropertyValue(IStructuredSelection bean) {
		assert !bean.isEmpty();
		try {
			Object value = getFieldValue(bean.getFirstElement());
			if (isEditable()) {
				if (editorFactory != null)
					value = editorFactory.encodeValue(value);
				else {
					Class type = lastValue.getClass();
					TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
					value = ta.getEditor().decodeValue(value);
				}
			}
			lastValue = value;
			return value;
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		lastValue = null;
		return null;
	}

	@Override
	public Object getFieldValue(Object bean) {
		try {
			Method readMethod = property.getReadMethod();
			if (readMethod != null) 
				return readMethod.invoke(bean);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		return null;
	}

	@Override
	public void setFieldValue(Object bean, Object newValue) {
		Method writeMethod = property.getWriteMethod();
		if(writeMethod!=null){
			try {
				writeMethod.invoke(bean, newValue);
			} catch (Exception e) {
				VisualSwingPlugin.getLogger().error(e);
			}
		}
	}
	@Override
	public boolean isPropertyResettable(IStructuredSelection bean) {
		return true;
	}
	
	@Override
	protected Object getDefaultValue(Object b, String lnfClassname) {
		return default_value;
	}
	
	@Override
	public void setPropertyValue(IStructuredSelection bean, Object value) {
		assert !bean.isEmpty();
		if (isEditable()) {
			try {
				if (editorFactory != null)
					value = editorFactory.decodeValue(value);
				else {
					Class type = lastValue.getClass();
					TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
					value = ta.getEditor().decodeValue(value);
				}
				for (Object b : bean.toArray()) {
					IUndoableOperation operation = new SetValueOperation(b, this, value);
					IOperationHistory operationHistory = PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
					if (b instanceof Component) {
						Component jcomp = (Component) b;
						WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(jcomp);
						if (adapter != null) {
							operation.addContext(adapter.getUndoContext());
						}
					}
					operationHistory.execute(operation, null, null);
				}
			} catch (Exception e) {
				VisualSwingPlugin.getLogger().error(e);
			}
		}
	}
	private boolean isEditable() {
		if (editorFactory == null) {
			if (lastValue == null)
				return false;
			else {
				Class type = lastValue.getClass();
				TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
				if (ta != null && ta.getEditor() != null)
					return true;
				else
					return false;
			}
		}
		return true;
	}

	
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		if (isEditable()) {
			if (editorFactory != null)
				return editorFactory.createPropertyEditor(bean, parent);
			else {
				Class type = lastValue.getClass();
				TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
				return ta.getEditor().createPropertyEditor(bean, parent);
			}
		} else {
			return null;
		}
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getDescription() {
		return property.getName();
	}

	@Override
	public String getDisplayName() {
		return property.getName()+TEXT_TRAILING;
	}

	@Override
	public String[] getFilterFlags() {
		return null;
	}

	@Override
	public Object getHelpContextIds() {
		return null;
	}

	@Override
	public Object getId() {
		return property.getName();
	}

	@Override
	public ILabelProvider getLabelProvider() {
		return labelFactory == null ? null : labelFactory.getLabelProvider();
	}

	@Override
	public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {
		return false;
	}

	@Override
	public boolean cloneProperty(Object bean, Component clone) {
		try {
			Object value = getFieldValue(bean);
			setFieldValue(clone, value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean isGencode() {
		return true;
	}

	@Override
	public void setFilterFlags(String[] filters) {
	}

	@Override
	public void setCategory(String categoryName) {
		this.category = categoryName;
	}

	@Override
	public boolean isEdited(WidgetAdapter adapter) {
		Map<String, Boolean> editedMap = adapter.getEditingMap();
		Boolean bool = editedMap.get(property.getName());
		return bool == null ? false : bool.booleanValue();
	}

	@Override
	public IValueParser getValueParser() {
		return typeAdapter==null?null:typeAdapter.getParser();
	}
	
	@Override
	protected Class getObjectClass() {
		return getClass();
	}
	@Override
	
	public Class getPropertyType(){
		return property.getPropertyType();
	}

	@Override
	public String getSetName() {
		return property.getWriteMethod().getName();
	}

}
