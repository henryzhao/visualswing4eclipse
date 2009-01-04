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

package org.dyno.visual.swing.widgets.undo;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
/**
 * 
 * ButtonGroupNameDialog
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
class ButtonGroupNameDialog extends Dialog {
	private String message;
	private String input;
	private Text text;
	protected ButtonGroupNameDialog(Shell parentShell) {
		super(parentShell);
	}
	public void setPromptMessage(String message){
		this.message = message;
	}
	public void setInput(String input){
		this.input = input;
	}
	public String getInput(){
		return this.input;
	}
	
	@Override
	protected void okPressed() {
		this.input = text.getText();
		super.okPressed();
	}
	@Override
	protected Control createDialogArea(Composite parent) {
		super.getShell().setText(Messages.ButtonGroupNameDialog_Change_Var_Name);
		Composite area = (Composite) super.createDialogArea(parent);
		Label label = new Label(area, SWT.NONE);
		label.setText(message);
		GridData data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		data.grabExcessHorizontalSpace = true;
		label.setLayoutData(data);
		text = new Text(area, SWT.SINGLE|SWT.BORDER);
		text.setText(input);
		text.selectAll();
		text.setFocus();
		data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		data.grabExcessHorizontalSpace = true;
		text.setLayoutData(data);
		return area;
	}

}

