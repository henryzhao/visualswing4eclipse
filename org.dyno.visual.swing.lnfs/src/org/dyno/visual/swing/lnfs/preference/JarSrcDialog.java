package org.dyno.visual.swing.lnfs.preference;

import java.io.File;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class JarSrcDialog extends Dialog {
	private Text txtJar;
	private Text txtSrc;

	public JarSrcDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.JarSrcDialog_Choose_Jar);
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		main.setLayout(layout);
		Label lblJar = new Label(main, SWT.NONE);
		lblJar.setText(Messages.JarSrcDialog_Archive);
		GridData data = new GridData();
		data.widthHint = 180;
		data.heightHint = 16;
		txtJar = new Text(main, SWT.SINGLE | SWT.BORDER);
		txtJar.setLayoutData(data);
		txtJar.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				updateButtonState();
			}});
		Button btnJar = new Button(main, SWT.PUSH);
		btnJar.setText(Messages.JarSrcDialog_Browse_1);
		btnJar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browseJar();
			}
		});
		Label lblSrc = new Label(main, SWT.NONE);
		lblSrc.setText(Messages.JarSrcDialog_Source);
		data = new GridData();
		data.widthHint = 180;
		data.heightHint = 16;
		txtSrc = new Text(main, SWT.SINGLE | SWT.BORDER);
		txtSrc.setLayoutData(data);
		txtSrc.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				updateButtonState();
			}});
		Button btnSrc = new Button(main, SWT.PUSH);
		btnSrc.setText(Messages.JarSrcDialog_Browse_2);
		btnSrc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browseSrc();
			}
		});
		return main;
	}

	private void browseJar() {
		FileDialog dialog = new FileDialog(getShell());
		dialog.setText(Messages.JarSrcDialog_Choose_Jar);
		dialog.setFilterExtensions(new String[] { "*.jar" }); //$NON-NLS-1$
		String jarpath = dialog.open();
		if (jarpath != null) {
			txtJar.setText(jarpath);
		}
		updateButtonState();
	}

	private String jar_path;
	private String src_path;

	public String getJarPath() {
		return jar_path;
	}

	public String getSrcPath() {
		return src_path;
	}

	@Override
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		updateButtonState();
		return control;
	}

	@Override
	protected void okPressed() {
		String error = checkInput();
		if (error == null) {
			jar_path = txtJar.getText().trim();
			src_path = txtSrc.getText().trim();
			if (src_path != null && src_path.length() == 0) {
				src_path = null;
			}
			super.okPressed();
		} else {
			MessageDialog.openError(getShell(), Messages.JarSrcDialog_Error, error);
		}
	}

	private String checkInput() {
		String jarpath = txtJar.getText().trim();
		String srcpath = txtSrc.getText().trim();
		if (jarpath == null || jarpath.length() == 0) {
			return Messages.JarSrcDialog_Jar_Path;
		}
		File file = new File(jarpath);
		if (!file.exists()) {
			return Messages.JarSrcDialog_File_Not_Exist;
		}
		if (srcpath != null && srcpath.length() != 0) {
			file = new File(srcpath);
			if (!file.exists()) {
				return Messages.JarSrcDialog_File_Not_Exist;
			}
		}
		return null;
	}
	private void updateButtonState(){
		boolean ok=checkInput()==null;
		getButton(IDialogConstants.OK_ID).setEnabled(ok);
	}
	private void browseSrc() {
		FileDialog dialog = new FileDialog(getShell());
		dialog.setText(Messages.JarSrcDialog_Choose_Source);
		dialog.setFilterExtensions(new String[] { "*.zip" }); //$NON-NLS-1$
		String srcpath = dialog.open();
		if (srcpath != null) {
			txtSrc.setText(srcpath);
		}
		updateButtonState();
	}
}
