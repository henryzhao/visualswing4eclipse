package org.dyno.visual.swing;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.messages"; //$NON-NLS-1$
	public static String PRELOADER_JOB_NAME;
	public static String PRELOADER_LOADING;
	public static String PRELOADER_TASK_NAME;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
