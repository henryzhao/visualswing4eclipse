
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

import java.util.Calendar;
import java.util.Date;

import javax.swing.SpinnerDateModel;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.types.items.CalendarItems;
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

class DateAccessible implements AccessibleUI {
	private Item[] calendarItems;
	private Button btnDefault;
	private Composite pane;
	private DateCombo spInit;
	private Button btnMin;
	private DateCombo spMin;
	private Button btnMax;
	private DateCombo spMax;
	private Combo spStep;
	private Label lblInit;
    private Label lblStep;
	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		SpinnerDateModel model = (SpinnerDateModel) value;
		Comparable max = model.getEnd();
		Comparable min = model.getStart();
		Date number = model.getDate();
		int field = model.getCalendarField();
		if (max == null && min == null && number !=null && field == Calendar.DAY_OF_MONTH)
			btnDefault.setSelection(true);
		else {
			btnDefault.setSelection(false);
			Calendar cal=Calendar.getInstance();
			cal.setTime(number);
			spInit.setSelection(cal);
			btnMin.setSelection(min!=null);
			if(min!=null){
				cal = Calendar.getInstance();
				cal.setTime((Date)min);
				spMin.setSelection(cal);
			}
			btnMax.setSelection(max!=null);
			if(max!=null){
				cal = Calendar.getInstance();
				cal.setTime((Date)max);				
				spMax.setSelection(cal);
			}
			spStep.select(getIndexOfCalendarField(field));
			btnDefaultSelected();
			btnMinSelected();
			btnMaxSelected();
		}
	}
	private int getIndexOfCalendarField(int field){
		for(int i=0;i<calendarItems.length;i++){
			int value = (Integer)calendarItems[i].getValue();
			if(value==field){
				return i;
			}
		}
		return -1;
	}
	@Override
	public Object getValue() {
		if (btnDefault.getSelection())
			return new SpinnerDateModel();
		else {
			Date init=spInit.getSelection().getTime();
			init = truncate(init);
			Date min = null;
			if(btnMin.getSelection())
				min=spMin.getSelection().getTime();
			min = truncate(min);
			Date max = null;
			if(btnMax.getSelection())
				max=spMax.getSelection().getTime();
			max = truncate(max);
			int field = (Integer) calendarItems[spStep.getSelectionIndex()].getValue();
			return new SpinnerDateModel(init, min, max, field);
		}
	}
	private Date truncate(Date date){
		if(date==null)
			return date;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
		return cal.getTime();
	}

	private void btnDefaultSelected() {
		if (btnDefault.getSelection()) {
			spInit.setEnabled(false);
			btnMin.setEnabled(false);
			btnMax.setEnabled(false);
			spMin.setEnabled(false);
			spMax.setEnabled(false);
			spStep.setEnabled(false);
			lblInit.setEnabled(false);
			lblStep.setEnabled(false);
		} else {
			spInit.setEnabled(true);
			btnMin.setEnabled(true);
			btnMax.setEnabled(true);
			spStep.setEnabled(true);
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

	public DateAccessible(Composite parent) {
		calendarItems = new CalendarItems().getItems();
		pane = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginTop = 5;
		layout.marginWidth = 0;
		layout.marginLeft = 10;
		layout.verticalSpacing = 10;
		pane.setLayout(layout);
		btnDefault = new Button(pane, SWT.CHECK);
		btnDefault.setText(Messages.DateAccessible_Use_Empty_Arg_Cons);
		GridData data = new GridData();
		data.horizontalSpan = 2;
		btnDefault.setLayoutData(data);
		btnDefault.setSelection(true);
		btnDefault.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnDefaultSelected();
			}
		});
		lblInit = new Label(pane, SWT.NONE);
		lblInit.setText(Messages.DateAccessible_Initial_Value);
		data = new GridData();
		data.horizontalIndent = 25;
		lblInit.setLayoutData(data);
		spInit = new DateCombo(pane, SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		spInit.setLayoutData(data);
		btnMin = new Button(pane, SWT.CHECK);
		btnMin.setText(Messages.DateAccessible_Min);
		data = new GridData();
		data.horizontalIndent = 25;
		btnMin.setLayoutData(data);
		btnMin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnMinSelected();
			}
		});
		spMin = new DateCombo(pane, SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		spMin.setLayoutData(data);
		btnMax = new Button(pane, SWT.CHECK);
		btnMax.setText(Messages.DateAccessible_Max);
		data = new GridData();
		data.horizontalIndent = 25;
		btnMax.setLayoutData(data);
		btnMax.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnMaxSelected();
			}
		});
		spMax = new DateCombo(pane, SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		spMax.setLayoutData(data);
		lblStep = new Label(pane, SWT.NONE);
		lblStep.setText(Messages.DateAccessible_Step_Size);
		data = new GridData();
		data.horizontalIndent = 25;
		lblStep.setLayoutData(data);
		spStep = new Combo(pane, SWT.BORDER|SWT.DROP_DOWN|SWT.READ_ONLY);
		for(int i=0;i<calendarItems.length;i++){
			spStep.add(calendarItems[i].getName());
		}
		spStep.select(getIndexOfCalendarField(Calendar.DAY_OF_MONTH));
		data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		spStep.setLayoutData(data);
		btnDefaultSelected();
	}

	@Override
	public Control getAccessibleUI() {
		return pane;
	}

	@Override
	public String isInputValid() {
		if (!btnDefault.getSelection()) {
			Calendar init = spInit.getSelection();
			if (btnMin.getSelection()) {
				Calendar min = spMin.getSelection();				
				if (after(min, init))
					return Messages.DateAccessible_Earlier;
			}
			if (btnMax.getSelection()) {
				Calendar max = spMax.getSelection();
				if (after(init, max))
					return Messages.DateAccessible_Later;
			}
			if (btnMin.getSelection() && btnMax.getSelection()) {
				Calendar min = spMin.getSelection();
				Calendar max = spMax.getSelection();
				if (after(min, max))
					return Messages.DateAccessible_Not_Later;
			}
		}
		return null;
	}
	private boolean after(Calendar cal1, Calendar cal2){
		int year1=cal1.get(Calendar.YEAR);
		int year2=cal2.get(Calendar.YEAR);
		int month1=cal1.get(Calendar.MONTH);
		int month2=cal2.get(Calendar.MONTH);
		int day1=cal1.get(Calendar.DATE);
		int day2=cal2.get(Calendar.DATE);
		if(year1>year2)
			return true;
		if(year1<year2)
			return false;
		if(month1>month2)
			return true;
		if(month1<month2)
			return false;
		if(day1>day2)
			return true;
		if(day1<day2)
			return false;
		return false;
	}
}

