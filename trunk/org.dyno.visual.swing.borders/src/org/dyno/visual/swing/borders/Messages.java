package org.dyno.visual.swing.borders;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.borders.messages"; //$NON-NLS-1$
	public static String InsetsCellEditorValidator_Bottom_Gap_GT_0;
	public static String InsetsCellEditorValidator_Incorrect_Format_Warning;
	public static String InsetsCellEditorValidator_Left_Gap_GT_0;
	public static String InsetsCellEditorValidator_Right_Gap_GT_0;
	public static String InsetsCellEditorValidator_Top_Gap_GT_0;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
