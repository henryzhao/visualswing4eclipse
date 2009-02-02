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

import java.lang.reflect.Constructor;

import javax.swing.JDialog;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

@SuppressWarnings("unchecked")
public class NewDialogPage extends NewComponentPage {
	@Override
	public boolean isCreateMain() {
		return false;
	}

	private void createConstructor(IType type, String param, String args, IProgressMonitor monitor) throws JavaModelException {
		StringBuffer buf = new StringBuffer();
		buf.append("public " + type.getTypeQualifiedName('.') + "(");
		if (param != null)
			buf.append(param);
		buf.append("){\n");
		if (args != null)
			buf.append("super(" + args + ");\n");
		buf.append(INIT_METHOD_NAME+"();\n");
		buf.append("}\n");
		type.createMethod(buf.toString(), null, false, monitor);
	}

	@Override
	protected void createTypeMembers(IType type, ImportsManager imports, IProgressMonitor monitor) throws CoreException {
		super.createTypeMembers(type, imports, monitor);
		String lineDelim = "\n";
		String verfield = "private static final long serialVersionUID = 1L;";
		verfield += lineDelim;
		type.createField(verfield, null, false, monitor);
		String lnffield = "private static final " + imports.addImport("java.lang.String") + " PREFERRED_LOOK_AND_FEEL = null;" + lineDelim;
		type.createField(lnffield, null, false, monitor);

		Constructor[] cons = JDialog.class.getConstructors();
		if (cons != null) {
			for (Constructor<JDialog> con : cons) {
				Class<?>[] pts = con.getParameterTypes();
				if (pts == null || pts.length == 0)
					createConstructor(type, null, null, monitor);
				else {
					StringBuffer ptBuf = new StringBuffer();
					StringBuffer agBuf = new StringBuffer();
					for (int i = 0; i < pts.length; i++) {
						String cName = pts[i].getCanonicalName();
						cName = imports.addImport(cName);
						String cArg = "arg";
						if (cName.equals("Dialog") || cName.equals("Frame") || cName.equals("Window"))
							cArg = "parent";
						else if (cName.equals("boolean"))
							cArg = "modal";
						else if (cName.equals("String"))
							cArg = "title";
						else if (cName.equals("ModalityType"))
							cArg = "modalityType";
						if (i == 0) {
							ptBuf.append(cName + " " + cArg);
							agBuf.append(cArg);
						} else {
							ptBuf.append(", " + cName + " " + cArg);
							agBuf.append(", " + cArg);
						}
					}
					createConstructor(type, ptBuf.toString(), agBuf.toString(), monitor);
				}
			}
		}

		StringBuffer buf = new StringBuffer();
		buf.append("private void "+INIT_METHOD_NAME+"(");
		buf.append(") {");
		buf.append(lineDelim);
		buf.append("setLayout(new ");
		buf.append(imports.addImport("org.dyno.visual.swing.layouts.GroupLayout"));
		buf.append("());");
		buf.append(lineDelim);
		buf.append("setSize(400,300);");
		buf.append(lineDelim);
		buf.append("}");
		type.createMethod(buf.toString(), null, false, monitor);
		// Create main
		if (super.isCreateMain()) {
			createInstallLnF(type, imports, monitor);
			createMain(type, imports, monitor);
		}
	}

	private void createInstallLnF(IType type, ImportsManager imports, IProgressMonitor monitor) throws JavaModelException {
		StringBuffer buf;
		buf = new StringBuffer();
		buf.append("private static void installLnF() {\n");
		buf.append("try {\n");
		String strUIManager = imports.addImport("javax.swing.UIManager");
		buf.append("String lnfClassname = PREFERRED_LOOK_AND_FEEL;\n");
		buf.append("if (lnfClassname == null)\n");
		buf.append("lnfClassname = " + strUIManager + ".getCrossPlatformLookAndFeelClassName();\n");
		buf.append(strUIManager + ".setLookAndFeel(lnfClassname);\n");
		buf.append("} catch (Exception e) {\n");
		buf.append("System.err.println(\"Cannot install \" + PREFERRED_LOOK_AND_FEEL + \" on this platform:\" + e.getMessage());\n");
		buf.append("}\n");
		buf.append("}\n");
		type.createMethod(buf.toString(), null, false, monitor);
	}

	private void createMain(IType type, ImportsManager imports, IProgressMonitor monitor) throws JavaModelException {
		StringBuffer buf;
		buf = new StringBuffer();
		String cName = imports.addImport("java.lang.String");
		buf.append("/**\n");
		buf.append("* Main entry of the class.\n");
		buf.append("* Note: This class is only created so that you can easily preview the result at runtime.\n"); 
		buf.append("* It is not expected to be managed by the designer.\n");
		buf.append("* You can modify it as you like.\n");
		buf.append("*/\n");		
		buf.append("public static void main(" + cName + "[] args){\n");
		buf.append("installLnF();\n");
		cName = imports.addImport("javax.swing.SwingUtilities");
		buf.append(cName + ".invokeLater(new Runnable(){\n");
		buf.append("@Override\n");
		buf.append("public void run(){\n");
		cName = type.getTypeQualifiedName('.');
		buf.append(cName + " dialog = new " + cName + "();\n");
		buf.append("dialog.setTitle(\"" + type.getElementName() + "\");\n");
		buf.append("dialog.setLocationRelativeTo(null);\n");
		buf.append("dialog.setVisible(true);\n");
		buf.append("}\n");
		buf.append("});\n");
		buf.append("}\n");
		type.createMethod(buf.toString(), null, false, monitor);
	}

}

