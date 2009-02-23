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

import org.dyno.visual.swing.contentTypes.VisualSwingContentDescriber;
import org.dyno.visual.swing.plugin.spi.IConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.dialogs.IDialogSettings;

public class NewComponentPage extends NewClassWizardPage implements IConstants{

	final static String PAGE_NAME = "NewClassWizardPage"; //$NON-NLS-1$

	final static String SETTINGS_CREATEMAIN = "create_main"; //$NON-NLS-1$
	final static String SETTINGS_CREATECONSTR = "create_constructor"; //$NON-NLS-1$
	final static String SETTINGS_CREATEUNIMPLEMENTED = "create_unimplemented"; //$NON-NLS-1$
	final static String SETTINGS_SUPERCLASSNAME = "super_class_name";

	@Override
	public void setSuperClass(String name, boolean canBeModified) {
		IDialogSettings section = getDialogSettings().getSection(PAGE_NAME);
		if (section != null) {
			name = section.get(SETTINGS_SUPERCLASSNAME);
			super.setSuperClass(name, isCanBeModified());
		} else {
			super.setSuperClass("javax.swing.JPanel", isCanBeModified());
		}
	}
	protected void delegateSuperClass(String name, boolean canBeModified){
		super.setSuperClass(name, canBeModified);
	}
	protected boolean isCanBeModified(){
		return false;
	}
	@Override
	protected String getFileComment(ICompilationUnit parentCU,
			String lineDelimiter) throws CoreException {
		return VisualSwingContentDescriber.VISUAL_SWING_MAGIC+" -- DO NOT REMOVE THIS LINE!";
	}
}

