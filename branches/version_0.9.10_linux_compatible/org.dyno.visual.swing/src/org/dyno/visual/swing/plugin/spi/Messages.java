package org.dyno.visual.swing.plugin.spi;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.plugin.spi.messages"; //$NON-NLS-1$
	public static String WidgetAdapter_Add_Edit_Events;
	public static String WidgetAdapter_Border;
	public static String WidgetAdapter_Delete_Events;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
