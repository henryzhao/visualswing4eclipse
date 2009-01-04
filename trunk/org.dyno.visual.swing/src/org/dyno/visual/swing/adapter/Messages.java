package org.dyno.visual.swing.adapter;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.adapter.messages"; //$NON-NLS-1$
	public static String BEAN_NAME_PROPERTY_CATEGRORY;
	public static String BEAN_NAME_PROPERTY_DISPLAY_NAME;
	public static String BEAN_NAME_VALIDATOR_ALREADY_USED_VAR_NAME;
	public static String BEAN_NAME_VALIDATOR_ILLEGAL_VAR_NAME;
	public static String BEAN_NAME_VALIDATOR_NONEMPTY_WARNING;
	public static String FIELD_ACCESS_PROPERTY_CATEGORY;
	public static String FIELD_ACCESS_PROPERTY_DISPLAY_NAME;
	public static String GET_ACCESS_PROPERTY_CATEGORY;
	public static String GET_ACCESS_PROPERTY_DISPLAY_NAME;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
