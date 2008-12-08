
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

package org.dyno.visual.swing.types.editor;

import java.io.BufferedReader;
import java.io.StringReader;

import javax.swing.ListModel;

import org.dyno.visual.swing.types.TypePlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ListItemDialog extends Dialog {
	private Text text;
	private ListModel model;
	private ListModelAdapter adapter;
	
	public ListItemDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("List IEditor");
		Composite composite = (Composite) super.createDialogArea(parent);
		Composite innerComposite = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		innerComposite.setLayout(layout);
		text = new Text(innerComposite, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.widthHint = 200;
		data.heightHint = 100;
		text.setLayoutData(data);
		text.setText(encodeModel(model));
		applyDialogFont(composite);
		return composite;
	}

	public void setListItemModel(ListModel model) {
		this.model = model;
	}
	public void setListModelAdapter(ListModelAdapter adapter){
		this.adapter = adapter;
	}
	private static String encodeModel(ListModel model) {
		if (model == null)
			return "null";
		StringBuilder builder = new StringBuilder();
		int size = model.getSize();
		for (int i = 0; i < size; i++) {
			Object object = model.getElementAt(i);
			if (i != 0) {
				builder.append("\n");
			}
			if (object == null) {
				builder.append("null");
			} else {
				builder.append(object.toString());
			}
		}
		return builder.toString();
	}

	@Override
	protected void cancelPressed() {
		this.model = null;
		super.cancelPressed();
	}

	@Override
	protected void okPressed() {
		String string = text.getText();
		if (string == null || string.trim().length() == 0) {
			model = null;
		} else if (string.equals("null")) {
			model = null;
		} else {
			try {
				ListModel m = adapter.newModel();
				StringReader r = new StringReader(string);
				BufferedReader br = new BufferedReader(r);
				String line;
				while ((line = br.readLine()) != null) {
					if (line.trim().length() != 0)
						adapter.addElement(m, line);
				}
				br.close();
				model = m;
			} catch (Exception e) {
				TypePlugin.getLogger().error(e);
				model = null;
			}
		}
		super.okPressed();
	}

	public Object getListItemModel() {
		return model;
	}
}

