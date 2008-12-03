
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
import javax.swing.SpinnerModel;

import org.dyno.visual.swing.types.endec.CalendarFieldWrapper;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.swt.widgets.Composite;

public class SpinnerDateModelType extends SpinnerModelType {

	protected SpinnerDateModelType() {
		super("date");
	}

	@Override
	public AccessibleUI createEditPane(Composite parent) {
		return new DateAccessible(parent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return "null";
		SpinnerDateModel sdm = (SpinnerDateModel) value;
		String className = imports.addImport("javax.swing.SpinnerDateModel");
		int calField = sdm.getCalendarField();
		Comparable start = sdm.getStart();
		Comparable end = sdm.getEnd();
		if (start == null && end == null && calField == Calendar.DAY_OF_MONTH)
			return "new " + className + "()";
		return "spinnerDateModel";
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(SpinnerModel o1, SpinnerModel o2) {
		SpinnerDateModel sdm1 = (SpinnerDateModel) o1;
		SpinnerDateModel sdm2 = (SpinnerDateModel) o2;
		Date d1 = sdm1.getDate();
		Date d2 = sdm2.getDate();
		if (!equals(d1, d2))
			return 1;
		Comparable start1 = sdm1.getStart();
		Comparable start2 = sdm2.getStart();
		if (!equals(start1, start2))
			return 1;
		Comparable end1 = sdm1.getEnd();
		Comparable end2 = sdm2.getEnd();
		if (!equals(end1, end2))
			return 1;
		int calendarField1 = sdm1.getCalendarField();
		int calendarField2 = sdm2.getCalendarField();
		if (calendarField1 != calendarField2)
			return 1;
		return 0;
	}

	@SuppressWarnings("unchecked")
	private boolean equals(Comparable o1, Comparable o2) {
		if (o1 == null) {
			if (o2 == null) {
				return true;
			} else {
				return false;
			}
		} else {
			if (o2 == null) {
				return false;
			} else {
				return o1.compareTo(o2) == 0;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return null;
		SpinnerDateModel sdm = (SpinnerDateModel) value;
		Date d = sdm.getDate();
		int calField = sdm.getCalendarField();
		Comparable start = sdm.getStart();
		Comparable end = sdm.getEnd();
		if (start == null && end == null && calField == Calendar.DAY_OF_MONTH)
			return null;
		String className = imports.addImport("javax.swing.SpinnerDateModel");
		StringBuilder builder = new StringBuilder();
		builder.append(className + " spinnerDateModel = null;\n");
		builder.append("{\n");
		builder.append(genDateCode("init", d, imports));
		builder.append(genDateCode("start", (Date) start, imports));
		builder.append(genDateCode("end", (Date) end, imports));
		builder.append("spinnerDateModel = " + "new " + className + "(init_date, start_date, end_date, "
				+ new CalendarFieldWrapper().getJavaCode(calField, imports) + ");\n");
		builder.append("}\n");
		return builder.toString();

	}

	private String genDateCode(String varName, Date d, ImportRewrite imports) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		String dateName = imports.addImport("java.util.Date");
		String calName = imports.addImport("java.util.Calendar");
		StringBuilder builder = new StringBuilder();
		builder.append(calName + " " + varName + "_cal = " + calName + ".getInstance();\n");
		builder.append(varName + "_cal.set(" + cal.get(Calendar.YEAR) + ", " + cal.get(Calendar.MONTH) + ", " + cal.get(Calendar.DATE) + ");\n");
		builder.append(dateName + " " + varName + "_date = " + varName + "_cal.getTime();\n");
		return builder.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object clone(Object object) {
		SpinnerDateModel model = (SpinnerDateModel) object;
		Date date = model.getDate();
		Comparable start = model.getStart();
		Comparable end = model.getEnd();
		int cal = model.getCalendarField();		
		return new SpinnerDateModel(date, start, end, cal);
	}
}

