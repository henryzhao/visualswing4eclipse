package org.dyno.visual.swing.wizards;

import javax.swing.JPanel;

import org.eclipse.jface.dialogs.IDialogSettings;

public class NewCustomContainerWizard extends NewVisualComponentVizard {
	public NewCustomContainerWizard() {
		super(JPanel.class.getName());
	}
	@Override
	public IDialogSettings getDialogSettings() {
		IDialogSettings dialogSettings2 = super.getDialogSettings();
		dialogSettings2.put(NewComponentPage.SETTINGS_CREATEMAIN, false);
		return dialogSettings2;
	}	
	@Override
	protected NewComponentPage createPage() {
		return new NewCustomContainerPage();
	}

}
