package org.dyno.visual.swing.wizards;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class NewInternalFramePage extends NewComponentPage {
	@Override
	public boolean isCreateMain() {
		return false;
	}

	@Override
	protected void createTypeMembers(IType type, ImportsManager imports,
			IProgressMonitor monitor) throws CoreException {
		super.createTypeMembers(type, imports, monitor);
		String lineDelim = "\n";
		String verfield = "private static final long serialVersionUID = 1L;";
		verfield += lineDelim;
		type.createField(verfield, null, false, monitor);
		String lnffield = "private static final "
				+ imports.addImport("java.lang.String")
				+ " PREFERRED_LOOK_AND_FEEL = null;" + lineDelim;
		type.createField(lnffield, null, false, monitor);

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
		buf.append(imports
				.addImport("org.dyno.visual.swing.layouts.GroupLayout"));
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

	private void createInstallLnF(IType type, ImportsManager imports,
			IProgressMonitor monitor) throws JavaModelException {
		StringBuffer buf;
		buf = new StringBuffer();
		buf.append("private static void installLnF() {\n");
		buf.append("try {\n");
		String strUIManager = imports.addImport("javax.swing.UIManager");
		buf.append("String lnfClassname = PREFERRED_LOOK_AND_FEEL;\n");
		buf.append("if (lnfClassname == null)\n");
		buf.append("lnfClassname = " + strUIManager
				+ ".getCrossPlatformLookAndFeelClassName();\n");
		buf.append(strUIManager + ".setLookAndFeel(lnfClassname);\n");
		buf.append("} catch (Exception e) {\n");
		buf
				.append("System.err.println(\"Cannot install \" + PREFERRED_LOOK_AND_FEEL + \" on this platform:\" + e.getMessage());\n");
		buf.append("}\n");
		buf.append("}\n");
		type.createMethod(buf.toString(), null, false, monitor);
	}

	private void createMain(IType type, ImportsManager imports,
			IProgressMonitor monitor) throws JavaModelException {
		StringBuffer buf;
		buf = new StringBuffer();
		String cName = imports.addImport("java.lang.String");
		buf.append("public static void main(" + cName + "[] args){\n");
		buf.append("installLnF();\n");
		cName = imports.addImport("javax.swing.SwingUtilities");
		buf.append(cName + ".invokeLater(new Runnable(){\n");
		buf.append("@Override\n");
		buf.append("public void run(){\n");
		cName = type.getTypeQualifiedName('.');
		buf.append(cName + " internalFrame = new " + cName + "();\n");
		cName = imports.addImport("javax.swing.JFrame");
		buf.append(cName + " frame = new " + cName + "();\n");
		cName = imports.addImport("javax.swing.JDesktopPane");
		buf.append(cName+" desktop = new "+cName+"();\n");
		buf.append("internalFrame.setBounds(10, 10, 400, 300);\n");
		buf.append("desktop.add(internalFrame);\n");
		cName = imports.addImport("java.awt.BorderLayout");		
		buf.append("frame.add(desktop, " + cName + ".CENTER);\n");
		buf.append("frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n");
		buf.append("frame.setTitle(\"" + type.getElementName() + "\");\n");
		buf.append("frame.pack();\n");
		buf.append("frame.setLocationRelativeTo(null);\n");
		buf.append("frame.setVisible(true);\n");
		buf.append("}\n");
		buf.append("});\n");
		buf.append("}\n");
		type.createMethod(buf.toString(), null, false, monitor);
	}

}
