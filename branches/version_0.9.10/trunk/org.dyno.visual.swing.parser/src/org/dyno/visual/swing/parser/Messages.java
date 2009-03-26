package org.dyno.visual.swing.parser;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.parser.messages"; //$NON-NLS-1$
	public static String DefaultSourceParser_Error;
	public static String DefaultSourceParser_Not_Supported_Lnf;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
