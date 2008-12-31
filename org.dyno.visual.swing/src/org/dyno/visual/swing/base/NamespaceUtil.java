package org.dyno.visual.swing.base;

public class NamespaceUtil {
	private static String GET = "get";

	public static String getGetMethodName(String name) {
		return GET + Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

	public static String getNameFromFieldName(String name) {
		return name;
	}

	public static String getFieldName(String name) {
		return name;
	}

	public static String getFieldNameFromGetMethodName(String getMethodName) {
		String name = getMethodName.substring(GET.length());
		name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
		return name;
	}

	public static boolean isGetMethodName(String name) {
		return name.startsWith(GET);
	}

	public static String getCapitalName(String name) {
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}
	

	@SuppressWarnings("unchecked")
	public static String getBasename(Class clazz) {
		String className = clazz.getName();
		int dot = className.lastIndexOf('.');
		if (dot != -1)
			className = className.substring(dot + 1);
		return Character.toLowerCase(className.charAt(0)) + className.substring(1);
	}	
}
