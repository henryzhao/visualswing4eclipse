package org.dyno.visual.swing.designer;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.designer.messages"; //$NON-NLS-1$
	public static String DragDropOperation_Delete;
	public static String GLASS_TARGET_VALIDATION_ERROR;
	public static String MoveResizeOperation_Reshape;
	public static String VisualDesigner_SetLaf;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
