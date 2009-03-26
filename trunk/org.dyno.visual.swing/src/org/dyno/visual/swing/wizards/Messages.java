package org.dyno.visual.swing.wizards;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.wizards.messages"; //$NON-NLS-1$
	public static String LayoutExtensionLibrary_Layout_Ext;
	public static String NewVisualSwingExtensionLibraryWizard_Lib;
	public static String NewVisualSwingExtensionLibraryWizard_Lib_Page;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
