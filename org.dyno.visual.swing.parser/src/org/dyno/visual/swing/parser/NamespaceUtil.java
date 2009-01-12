package org.dyno.visual.swing.parser;

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

}
