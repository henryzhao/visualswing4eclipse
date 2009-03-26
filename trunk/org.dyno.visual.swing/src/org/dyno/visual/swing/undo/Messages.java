package org.dyno.visual.swing.undo;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.undo.messages"; //$NON-NLS-1$
	public static String CutOperation_Cut_Components;
	public static String DeleteOperation_Delete_Components;
	public static String DuplicateOperation_Duplicate_Components;
	public static String PasteOperation_Paste_Components;
	public static String SetValueOperation_Changing_Value;
	public static String SetValueOperation_Error;
	public static String SetValueOperation_Error_Ocurrs_While_Setting_Property;
	public static String SetWidgetValueOperation_Changing_Component_Value;
	public static String VarChangeOperation_Change_Var;
	public static String VarChangeOperation_Enter_New_Name;
	public static String VarChangeOperation_Invalid_Id;
	public static String VarNameDialog_Changing_Var_Name;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
