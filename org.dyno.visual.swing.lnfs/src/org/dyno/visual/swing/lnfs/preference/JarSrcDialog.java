package org.dyno.visual.swing.lnfs.preference;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class JarSrcDialog extends Dialog {
	public JarSrcDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Choose Java archive");
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		main.setLayout(layout);
		Label lblJar = new Label(main, SWT.NONE);
		lblJar.setText("Archive:");
		GridData data = new GridData();
		data.widthHint = 140;
		Text txtJar = new Text(main, SWT.SINGLE|SWT.BORDER);
		txtJar.setLayoutData(data);
		Button btnJar = new Button(main, SWT.PUSH);
		btnJar.setText("Browse...");
		Label lblSrc = new Label(main, SWT.NONE);
		lblSrc.setText("Source:");
		data = new GridData();
		data.widthHint = 140;
		Text txtSrc = new Text(main, SWT.SINGLE|SWT.BORDER);
		txtSrc.setLayoutData(data);
		Button btnSrc = new Button(main, SWT.PUSH);
		btnSrc.setText("Browse...");
		return main;
	}

}
