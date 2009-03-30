package org.dyno.visual.swing.editors;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.editors.messages"; //$NON-NLS-1$
	public static String AbstractDesignerEditor_Illegal_File;
	public static String ComponentTreeContentProvider_Other_Components;
	public static String ComponentTreeLabelProvider_Form;
	public static String ComponentTreeLabelProvider_Root;
	public static String EventDesc_Name;
	public static String VisualSwingEditor_Designer_Creation_Job;
	public static String VisualSwingEditor_Generating_Designer;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
