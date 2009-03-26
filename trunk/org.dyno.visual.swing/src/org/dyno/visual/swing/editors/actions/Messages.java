package org.dyno.visual.swing.editors.actions;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.dyno.visual.swing.editors.actions.messages"; //$NON-NLS-1$
	public static String BottomAlignAction_Bottom_Alignment_in_Row;
	public static String CenterAlignAction_Center_Alignment;
	public static String CopyAction_Copy_Components;
	public static String CutAction_Cut_Components;
	public static String DeleteAction_Delete_Components;
	public static String DuplicateAction_Duplicate_Components;
	public static String LeftAlignAction_Left_Alignment_in_Column;
	public static String MiddleAlignAction_Center_Alignment;
	public static String PasteAction_Paste_Components;
	public static String PreviewAction_Preview_Design;
	public static String RightAlignAction_Right_Alignment_in_Column;
	public static String SameHeightAction_Same_Height;
	public static String SameWidthAction_Same_Width;
	public static String SelectAllAction_Select_All;
	public static String SourceViewAction_View_Source_Code;
	public static String TopAlignAction_Top_Alignment_in_Row;
	public static String VarChangeAction_Change_Var_Name;
	public static String VarNameDialog_Change_Var_Name;
	public static String ViewSourceCodeJob_View_Source_Code;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
