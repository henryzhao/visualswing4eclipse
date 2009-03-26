package org.dyno.visual.swing.widgets.layout;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.widgets.layout.messages"; //$NON-NLS-1$
	public static String BorderLayoutAdapter_Component_Placement;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
