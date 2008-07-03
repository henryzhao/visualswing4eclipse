package org.dyno.visual.swing.editors;

import org.dyno.visual.swing.base.NamespaceManager;

public class DefaultNamespaceManager extends NamespaceManager {
	private static String GET = "get";

	public String getGetMethodName(String name) {
		return GET + Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

	public String getNameFromFieldName(String name) {
		return name;
	}

	public String getFieldName(String name) {
		return name;
	}

	@Override
	public String getFieldNameFromGetMethodName(String getMethodName) {
		String name = getMethodName.substring(GET.length());
		name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
		return name;
	}

	@Override
	public boolean isGetMethodName(String name) {
		return name.startsWith(GET);
	}

	@Override
	public String getCapitalName(String name) {
		return Character.toUpperCase(name.charAt(0))+name.substring(1);
	}
}
