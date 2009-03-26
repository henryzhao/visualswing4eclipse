package org.dyno.visual.swing.types;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.types.messages"; //$NON-NLS-1$
	public static String ImageIconValidator_Cannot_Find_Such_Image_File;
	public static String ImageIconValidator_Incorrect_Icon_Image_Format;
	public static String ImageIconValidator_Incorrect_Icon_Image_Format_Segment_Id;
	public static String ImageIconValidator_Not_Image_File;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
