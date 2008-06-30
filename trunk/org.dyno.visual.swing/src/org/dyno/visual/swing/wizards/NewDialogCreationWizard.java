package org.dyno.visual.swing.wizards;

import javax.swing.JDialog;

public class NewDialogCreationWizard extends NewVisualComponentVizard {
	public NewDialogCreationWizard() {
		super(JDialog.class.getName());
	}
}
