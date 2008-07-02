package org.dyno.visual.swing.types.editor.spinnermodels;

import java.util.Calendar;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;

public class DatePopup extends Dialog {
	private DateTime picker;

	public DatePopup(Shell parent) {
		super(parent);
		setShellStyle(getShellStyle() | SWT.NO_TRIM | SWT.BORDER);
	}

	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		applyDialogFont(composite);
		initializeDialogUnits(composite);
		dialogArea = createDialogArea(composite);
		buttonBar = createButtonBar(composite);
		return composite;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Control bar = super.createButtonBar(parent);
		bar.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		return bar;
	}

	private static final int TODAY_ID = 1025;
	private Point location;

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, TODAY_ID, "Today", false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	protected void initializeBounds() {
		super.initializeBounds();
		Shell shell = getShell();
		if (location != null)
			shell.setLocation(location);
	}

	protected void setButtonLayoutData(Button button) {
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		int widthHint = 75;
		Point size = picker.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		if(size.x<245)
			widthHint = 40;
		Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		data.widthHint = Math.max(widthHint, minSize.x);
		button.setLayoutData(data);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == TODAY_ID) {
			setCalendar(Calendar.getInstance());
			super.buttonPressed(IDialogConstants.OK_ID);
		} else {
			super.buttonPressed(buttonId);
		}
	}

	@Override
	protected void okPressed() {
		calendar = Calendar.getInstance();
		calendar.set(picker.getYear(), picker.getMonth(), picker.getDay());
		super.okPressed();
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		picker = new DateTime(composite, SWT.CALENDAR);
		picker.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));		
		setCalendar(calendar);
		applyDialogFont(composite);
		return composite;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar cal) {
		this.calendar = cal;
		if (cal != null && picker != null) {
			picker.setYear(cal.get(Calendar.YEAR));
			picker.setMonth(cal.get(Calendar.MONTH));
			picker.setDay(cal.get(Calendar.DATE));
		}
	}

	private Calendar calendar;
}
