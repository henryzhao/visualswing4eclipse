/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.dyno.visual.swing.wizards;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.dialogs.IDialogSettings;

public class NewComponentPage extends NewClassWizardPage {

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
			super.setSuperClass(name, false);
		} else {
			super.setSuperClass("javax.swing.JPanel", false);
		}
	}

	@Override
	protected void createTypeMembers(IType type, ImportsManager imports, IProgressMonitor monitor) throws CoreException {
		super.createTypeMembers(type, imports, monitor);
		String lineDelim = "\n";
		String verfield = "private static final long serialVersionUID = 1L;";
		verfield += lineDelim;
		type.createField(verfield, null, false, monitor);

		StringBuffer buf = new StringBuffer();
		buf.append("public " + type.getTypeQualifiedName('.') + "(){");
		buf.append(lineDelim);
		buf.append("initComponent();");
		buf.append(lineDelim);
		buf.append("}");
		type.createMethod(buf.toString(), null, false, monitor);

		buf = new StringBuffer();
		buf.append("private void initComponent(");
		buf.append(") {");
		buf.append(lineDelim);
		buf.append("setLayout(new ");
		buf.append(imports.addImport("org.dyno.visual.swing.layouts.GroupLayout"));
		buf.append("());");
		buf.append(lineDelim);
		buf.append("}");
		type.createMethod(buf.toString(), null, false, monitor);
	}
}
