package org.dyno.visual.swing.widgets.grouplayout;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.widgets.grouplayout.messages"; //$NON-NLS-1$
	public static String GroupLayoutAdapter_Horizontal_Anchor;
	public static String GroupLayoutAdapter_Vertical_Anchor;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
