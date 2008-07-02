package org.dyno.visual.swing.base;

public class FactoryItem {
	private String objectName;
	@SuppressWarnings("unchecked")
	private Class objectClazz;
	private IFactory factory;

	@SuppressWarnings("unchecked")
	public FactoryItem(String name, Class clazz, IFactory factory) {
		this.objectName = name;
		this.factory = factory;
		this.objectClazz = clazz;
	}

	public String getObjectName() {
		return objectName;
	}

	public IFactory getFactory() {
		return factory;
	}

	public boolean isSelected(Object value) {
		if (value == null && objectClazz == null)
			return true;
		else
			return value.getClass() == objectClazz;
	}
}
