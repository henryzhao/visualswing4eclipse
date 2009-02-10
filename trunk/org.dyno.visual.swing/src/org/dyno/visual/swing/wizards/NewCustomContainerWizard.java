package org.dyno.visual.swing.wizards;

import javax.swing.JPanel;

public class NewCustomContainerWizard extends NewVisualComponentVizard {
	public NewCustomContainerWizard() {
		super(JPanel.class.getName());
	}
	@Override
	protected NewComponentPage createPage() {
		return new NewCustomContainerPage();
	}

}
