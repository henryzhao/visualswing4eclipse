package org.dyno.visual.swing.wizards;

import javax.swing.JPanel;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;

public class NewCustomContainerWizard extends NewVisualComponentVizard {
	public NewCustomContainerWizard() {
		super(JPanel.class.getName());
	}
	@Override
	public IDialogSettings getDialogSettings() {
		IDialogSettings dialogSettings2 = super.getDialogSettings();
		IDialogSettings section = dialogSettings2.getSection(NewComponentPage.PAGE_NAME);
		if (section == null) {
			section = new DialogSettings(NewComponentPage.PAGE_NAME);
			dialogSettings2.addSection(section);
		}
		section.put(NewComponentPage.SETTINGS_CREATEMAIN, false);
		return dialogSettings2;
	}	
	@Override
	protected NewComponentPage createPage() {
		return new NewCustomContainerPage();
	}

}
