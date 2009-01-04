package org.dyno.visual.swing.types.editor.spinnermodels.types;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.types.editor.spinnermodels.types.messages"; //$NON-NLS-1$
	public static String ByteType_Byte;
	public static String DoubleType_Double;
	public static String FloatType_Float;
	public static String IntegerType_Integer;
	public static String LongType_Long;
	public static String ShortType_Short;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
