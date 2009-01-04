package org.dyno.visual.swing.borders.action;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.borders.action.messages"; //$NON-NLS-1$
	public static String TitledBorderSwitchAction_Border_Title;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
