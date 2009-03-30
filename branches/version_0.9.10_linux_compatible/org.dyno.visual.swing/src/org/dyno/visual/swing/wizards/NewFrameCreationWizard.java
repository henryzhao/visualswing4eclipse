/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.wizards;

import javax.swing.JFrame;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
/**
 * 
 * NewFrameCreationWizard
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class NewFrameCreationWizard extends NewVisualComponentVizard {
	public NewFrameCreationWizard() {
		super(JFrame.class.getName());
	}
	@Override
	public IDialogSettings getDialogSettings() {
		IDialogSettings dialogSettings2 = super.getDialogSettings();
		IDialogSettings section = dialogSettings2.getSection(NewComponentPage.PAGE_NAME);
		if (section == null) {
			section = new DialogSettings(NewComponentPage.PAGE_NAME);
			dialogSettings2.addSection(section);
		}
		section.put(NewComponentPage.SETTINGS_CREATEMAIN, true);
		return dialogSettings2;
	}
	@Override
	protected NewComponentPage createPage() {
		return new NewFramePage();
	}
}

