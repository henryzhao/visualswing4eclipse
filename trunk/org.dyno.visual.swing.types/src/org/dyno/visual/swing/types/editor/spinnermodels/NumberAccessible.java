package org.dyno.visual.swing.types.editor.spinnermodels;

import javax.swing.SpinnerNumberModel;

import org.dyno.visual.swing.types.editor.spinnermodels.types.NumberType;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

class NumberAccessible implements AccessibleUI {
	private Button btnDefault;
	private Composite pane;
	private ComboViewer viewer;
	private Combo cmbType;
	private Spinner spInit;
	private Button btnMin;
	private Spinner spMin;
	private Button btnMax;
	private Spinner spMax;
	private Spinner spStep;
	private Label lblType;
	private Label lblInit;
	private Label lblStep;

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		SpinnerNumberModel model = (SpinnerNumberModel) value;
		Comparable max = model.getMaximum();
		Comparable min = model.getMinimum();
		Number number = model.getNumber();
		Number size = model.getStepSize();
		if (max == null && min == null && number instanceof Integer && size instanceof Integer && number.intValue() == 0 && size.intValue() == 1)
			btnDefault.setSelection(true);
		else {
			btnDefault.setSelection(false);
			int index = NumberType.getNumberTypeIndex(number);
			cmbType.select(index);
			spInit.setSelection(number.intValue());
			btnMin.setSelection(min!=null);
			if(min!=null)
				spMin.setSelection(((Number)min).intValue());
			btnMax.setSelection(max!=null);
			if(max!=null)
				spMax.setSelection(((Number)max).intValue());
			spStep.setSelection(size.intValue());
			btnDefaultSelected();
			cmbTypeSelected();
			btnMinSelected();
			btnMaxSelected();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getValue() {
		if (btnDefault.getSelection())
			return new SpinnerNumberModel();
		else {
			int index = cmbType.getSelectionIndex();
			NumberType type = NumberType.getTypes()[index];
			Number init = type.valueOf(spInit.getSelection());
			Number min = null;
			if (btnMin.getSelection()) {
				min = type.valueOf(spMin.getSelection());
			}
			Number max = null;
			if (btnMax.getSelection()) {
				max = type.valueOf(spMax.getSelection());
			}
			Number size = type.valueOf(spStep.getSelection());
			return new SpinnerNumberModel(init, (Comparable) min, (Comparable) max, size);
		}
	}

	private void cmbTypeSelected() {
		int index = cmbType.getSelectionIndex();
		NumberType type = NumberType.getTypes()[index];
		spInit.setMaximum(type.getMaximum());
		spMin.setMaximum(type.getMaximum());
		spMax.setMaximum(type.getMaximum());
		spStep.setMaximum(type.getMaximum());

	}

	private void btnDefaultSelected() {
		if (btnDefault.getSelection()) {
			cmbType.setEnabled(false);
			spInit.setEnabled(false);
			btnMin.setEnabled(false);
			btnMax.setEnabled(false);
			spMin.setEnabled(false);
			spMax.setEnabled(false);
			spStep.setEnabled(false);
			lblType.setEnabled(false);
			lblInit.setEnabled(false);
			lblStep.setEnabled(false);
		} else {
			cmbType.setEnabled(true);
			spInit.setEnabled(true);
			btnMin.setEnabled(true);
			btnMax.setEnabled(true);
			spStep.setEnabled(true);
			lblType.setEnabled(true);
			lblInit.setEnabled(true);
			lblStep.setEnabled(true);
			btnMinSelected();
			btnMinSelected();
		}
	}

	private void btnMinSelected() {
		spMin.setEnabled(btnMin.getSelection());
	}

	private void btnMaxSelected() {
		spMax.setEnabled(btnMax.getSelection());
	}

	public NumberAccessible(Composite parent) {
		pane = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginTop = 5;
		layout.marginWidth = 0;
		layout.marginLeft = 10;
		layout.verticalSpacing = 5;
		pane.setLayout(layout);
		btnDefault = new Button(pane, SWT.CHECK);
		btnDefault.setText("Use empty argument constructor.");
		GridData data = new GridData();
		data.horizontalSpan = 2;
		data.horizontalAlignment = SWT.FILL;
		data.widthHint = 265;
		btnDefault.setLayoutData(data);
		btnDefault.setSelection(true);
		btnDefault.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnDefaultSelected();
			}
		});
		lblType = new Label(pane, SWT.NONE);
		lblType.setText("Number Type:");
		data = new GridData();
		data.horizontalIndent = 25;
		lblType.setLayoutData(data);
		cmbType = new Combo(pane, SWT.DROP_DOWN | SWT.READ_ONLY);
		viewer = new ComboViewer(cmbType);
		viewer.setContentProvider(new NumberTypeContentProvider());
		viewer.setInput(NumberType.getTypes());
		cmbType.select(NumberType.getInitialIndex());
		cmbType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cmbTypeSelected();
			}
		});
		data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		cmbType.setLayoutData(data);
		lblInit = new Label(pane, SWT.NONE);
		lblInit.setText("Initial Value:");
		data = new GridData();
		data.horizontalIndent = 25;
		lblInit.setLayoutData(data);
		spInit = new Spinner(pane, SWT.BORDER);
		spInit.setMinimum(0);
		spInit.setMaximum(Integer.MAX_VALUE);
		spInit.setSelection(0);
		data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		spInit.setLayoutData(data);
		btnMin = new Button(pane, SWT.CHECK);
		btnMin.setText("Minimum:");
		data = new GridData();
		data.horizontalIndent = 25;
		btnMin.setLayoutData(data);
		btnMin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnMinSelected();
			}
		});
		spMin = new Spinner(pane, SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		spMin.setLayoutData(data);
		spMin.setMinimum(0);
		spMin.setMaximum(Integer.MAX_VALUE);
		spMin.setSelection(0);
		btnMax = new Button(pane, SWT.CHECK);
		btnMax.setText("Maximum:");
		data = new GridData();
		data.horizontalIndent = 25;
		btnMax.setLayoutData(data);
		btnMax.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnMaxSelected();
			}
		});
		spMax = new Spinner(pane, SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		spMax.setLayoutData(data);
		spMax.setMinimum(0);
		spMax.setMaximum(Integer.MAX_VALUE);
		spMax.setSelection(0);
		lblStep = new Label(pane, SWT.NONE);
		lblStep.setText("Step Size:");
		data = new GridData();
		data.horizontalIndent = 25;
		lblStep.setLayoutData(data);
		spStep = new Spinner(pane, SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		spStep.setLayoutData(data);
		spStep.setSelection(1);
		spStep.setMinimum(1);
		spStep.setMaximum(Integer.MAX_VALUE);
		spStep.setSelection(0);
		btnDefaultSelected();
	}

	@Override
	public Control getAccessibleUI() {
		return pane;
	}

	@Override
	public String isInputValid() {
		if (!btnDefault.getSelection()) {
			int init = spInit.getSelection();
			int index = cmbType.getSelectionIndex();
			if (index == -1)
				return "Please select a value type!";
			NumberType type = NumberType.getTypes()[index];
			int minTValue = type.getMininum();
			int maxTValue = type.getMaximum();
			if (init < minTValue || init > maxTValue)
				return "The initial value value should be between " + minTValue + " and " + maxTValue;
			if (btnMin.getSelection()) {
				int min = spMin.getSelection();
				if (min < minTValue || min > maxTValue)
					return "The minimum value value should be between " + minTValue + " and " + maxTValue;
				if (init < min)
					return "The initial value should not be less than the minimum value!";
			}
			if (btnMax.getSelection()) {
				int max = spMax.getSelection();
				if (max < minTValue || max > maxTValue)
					return "The maximum value value should be between " + minTValue + " and " + maxTValue;
				if (init > max)
					return "The initial value should not be greater than the maximum value!";
			}
			if (btnMin.getSelection() && btnMax.getSelection()) {
				int min = spMin.getSelection();
				int max = spMax.getSelection();
				if (min > max)
					return "The minumum value should not be greater than the maximum value!";
			}
			int size = spStep.getSelection();
			if (size <= 0)
				return "The step size should be greater than 0";
			if (size > maxTValue)
				return "The step size should not be greater than " + maxTValue;
		}
		return null;
	}

}