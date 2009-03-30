
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

package org.dyno.visual.swing.types.editor.spinnermodels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SpinnerListModel;

import org.dyno.visual.swing.types.TypePlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;


class ListAccessible implements AccessibleUI {
	private Text text;
	private Composite pane;
	public ListAccessible(Composite parent) {
		pane = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		pane.setLayout(layout);
		text = new Text(pane, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.widthHint = 200;
		data.heightHint = 100;
		text.setLayoutData(data);
	}

	@Override
	public Control getAccessibleUI() {
		return pane;
	}

	@Override
	public Object getValue() {
		try {
			List<String> list = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new StringReader(text.getText()));
			String line;
			while((line=br.readLine())!=null){
				list.add(line);
			}
			String[]items = new String[list.size()];
			return new SpinnerListModel(list.toArray(items));
		} catch (IOException e) {
			TypePlugin.getLogger().error(e);
			return null;
		}			
	}

	@Override
	public String isInputValid() {
		return null;
	}

	@Override
	public void setValue(Object value) {
		if (value == null) {
			text.setText("");
		} else {
			SpinnerListModel model = (SpinnerListModel) value;
			List<?> list = model.getList();
			StringBuilder builder = new StringBuilder();
			for(int i=0;i<list.size();i++){
				if(i!=0){
					builder.append("\n");
				}
				builder.append(list.get(i));
			}
			text.setText(builder.toString());
		}
	}
}

