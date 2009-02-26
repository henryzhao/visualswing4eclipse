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

import javax.swing.JApplet;

import org.eclipse.jface.dialogs.IDialogSettings;

/**
 * 
 * NewDialogCreationWizard
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class NewAppletCreationWizard extends NewVisualComponentVizard {
	public NewAppletCreationWizard() {
		super(JApplet.class.getName());
	}

	@Override
	public IDialogSettings getDialogSettings() {
		IDialogSettings dialogSettings2 = super.getDialogSettings();
		dialogSettings2.put(NewComponentPage.SETTINGS_CREATEMAIN, false);
		return dialogSettings2;
	}

	@Override
	protected NewComponentPage createPage() {
		return new NewAppletPage();
	}
}
