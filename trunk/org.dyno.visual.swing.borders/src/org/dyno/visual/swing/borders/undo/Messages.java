package org.dyno.visual.swing.borders.undo;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.borders.undo.messages"; //$NON-NLS-1$
	public static String BorderSwitchOperation_Switch_Border;
	public static String NullBorderSwitchOperation_Switch_Border;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
