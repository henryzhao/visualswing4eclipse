package org.dyno.visual.swing.wizards;

import javax.swing.JFrame;

public class NewFrameCreationWizard extends NewVisualComponentVizard {
	public NewFrameCreationWizard() {
		super(JFrame.class.getName());
	}
}
