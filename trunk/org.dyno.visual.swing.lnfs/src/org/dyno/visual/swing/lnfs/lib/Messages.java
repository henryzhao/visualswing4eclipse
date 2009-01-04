package org.dyno.visual.swing.lnfs.lib;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.lnfs.lib.messages"; //$NON-NLS-1$
	public static String LookAndFeelLib_Laf_Xml_Not_Found;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
