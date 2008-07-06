/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor;

import java.util.HashMap;

import javax.swing.SpinnerModel;

import org.dyno.visual.swing.types.editor.spinnermodels.AccessibleUI;
import org.dyno.visual.swing.types.editor.spinnermodels.SpinnerModelContentProvider;
import org.dyno.visual.swing.types.editor.spinnermodels.SpinnerModelType;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class SpinnerModelDialog extends Dialog {
	private ComboViewer viewer;

	public SpinnerModelDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		getShell().setText("Spinner Model IEditor");
		modelPanes = new HashMap<SpinnerModelType, AccessibleUI>();
		Composite innerComposite = new Composite(composite, SWT.NONE);
		innerComposite.setLayoutData(new GridData());
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		innerComposite.setLayout(layout);
		Label lbl = new Label(innerComposite, SWT.NONE);
		lbl.setText("Type:");
		GridData data = new GridData();
		lbl.setLayoutData(data);
		Combo cmbType = new Combo(innerComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
		viewer = new ComboViewer(cmbType);
		viewer.setContentProvider(new SpinnerModelContentProvider());
		viewer.setInput(SpinnerModelType.getModelTypes());
		ISelection selection = createModelTypeSelection(model);
		viewer.setSelection(selection);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				viewer_selectionChanged(event.getSelection());
			}
		});
		data = new GridData();
		data.widthHint = 280;
		cmbType.setLayoutData(data);
		modelPane = new Composite(innerComposite, SWT.NONE);
		modelPane.setLayout(new StackLayout());
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		data.heightHint = 170;
		data.horizontalIndent = 36;
		modelPane.setLayoutData(data);
		applyDialogFont(composite);
		viewer_selectionChanged(selection);
		SpinnerModelType type = SpinnerModelType.getSpinnerModelType(model);
		AccessibleUI paneUI = modelPanes.get(type);
		if (paneUI != null)
			paneUI.setValue(model);
		applyDialogFont(composite);
		return composite;
	}

	private void viewer_selectionChanged(ISelection selection) {
		if (selection instanceof StructuredSelection && !selection.isEmpty()) {
			SpinnerModelType type = (SpinnerModelType) ((StructuredSelection) selection).getFirstElement();
			AccessibleUI paneUI = modelPanes.get(type);
			if (paneUI == null) {
				paneUI = type.createEditPane(modelPane);
				if (paneUI != null) {
					modelPanes.put(type, paneUI);
				}
			}
			if (paneUI != null) {
				((StackLayout) modelPane.getLayout()).topControl = paneUI.getAccessibleUI();
				modelPane.layout();
			}
		}
	}

	@Override
	protected void okPressed() {
		ISelection selection = viewer.getSelection();
		if (selection.isEmpty()) {
			MessageDialog.openError(getShell(), "Error", "Please select a spinner model type!");
			return;
		}
		SpinnerModelType type = (SpinnerModelType) ((StructuredSelection) selection).getFirstElement();
		AccessibleUI paneUI = modelPanes.get(type);
		String message = paneUI.isInputValid();
		if (message != null) {
			MessageDialog.openError(getShell(), "Error", message);
			return;
		}
		model = (SpinnerModel) paneUI.getValue();
		super.okPressed();
	}

	private ISelection createModelTypeSelection(Object value) {
		return new StructuredSelection(SpinnerModelType.getSpinnerModelType(value));
	}

	public void setSpinnerModel(SpinnerModel model) {
		this.model = model;
	}

	public SpinnerModel getSpinnerModel() {
		return model;
	}

	private Composite modelPane;
	private SpinnerModel model;
	private HashMap<SpinnerModelType, AccessibleUI> modelPanes;
}
