package org.dyno.visual.swing.lnfs;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.lnfs.messages"; //$NON-NLS-1$
	public static String MetaLookAndFeelAdapter_Meta_Lnf;
	public static String MotifLookAndFeelAdapter_Motif_Lnf;
	public static String NimbusLookAndFeelAdapter_Nimbus_Lnf;
	public static String WindowsClassicLookAndFeelAdapter_Classic_Windows_Lnf;
	public static String WindowsLookAndFeelAdapter_Windows_Lnf;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
