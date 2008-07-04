/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor.spinnermodels;

import java.text.DateFormat;
import java.util.Calendar;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.swt.widgets.Widget;

public class DateCombo extends Composite {
	DateFormat dateFormat;
	Text text;
	DatePopup popup;
	Button arrow;

	class DateComboLayout extends Layout {
		@Override
		protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
			return DateCombo.this.computeSize(wHint, hHint, flushCache);
		}

		@Override
		protected void layout(Composite composite, boolean flushCache) {
			Point size = composite.getSize();
			int width = size.x;
			int height = size.y;
			text.setBounds(0, 0, width - 20, height);
			arrow.setBounds(width - 21, -1, 19, height - 1);
		}
	}

	/**
	 * @see SWT#BORDER
	 * @see SWT#READ_ONLY
	 * @see SWT#FLAT
	 * @see Widget#getStyle()
	 */
	public DateCombo(Composite parent, int style) {
		super(parent, style = checkStyle(style));
		setLayout(new DateComboLayout());
		int bg = getEnabled() ? SWT.COLOR_WHITE : SWT.COLOR_WIDGET_BACKGROUND;
		setBackground(getDisplay().getSystemColor(bg));
		dateFormat = DateFormat.getDateInstance();
		int textStyle = SWT.SINGLE;
		if ((style & SWT.READ_ONLY) != 0)
			textStyle |= SWT.READ_ONLY;
		if ((style & SWT.FLAT) != 0)
			textStyle |= SWT.FLAT;
		text = new Text(this, textStyle);

		int arrowStyle = SWT.ARROW | SWT.DOWN;
		if ((style & SWT.FLAT) != 0)
			arrowStyle |= SWT.FLAT;
		arrow = new Button(this, arrowStyle);
		arrow.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				popupDialog();
			}});
	}
	private void popupDialog(){
		createPopup(calendar);
		popup.setCalendar(calendar);
		Display display = getDisplay ();
		Rectangle parentRect = display.map (getParent (), null, getBounds ());
		Point comboSize = getSize ();
		int x = parentRect.x;
		int y = parentRect.y + comboSize.y;
		popup.setLocation(new Point(x, y));
		if(popup.open()==Dialog.OK){
			Calendar cal = popup.getCalendar();
			setSelection(cal);
		}
	}
	@Override
	public void setEnabled(boolean enabled) {
		int bg = enabled ? SWT.COLOR_WHITE : SWT.COLOR_WIDGET_BACKGROUND;
		Color bgc = getDisplay().getSystemColor(bg);
		setBackground(bgc);
		text.setBackground(bgc);
		arrow.setBackground(bgc);
		text.setEnabled(enabled);
		arrow.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	@Override
	public boolean setFocus() {
		return text.setFocus();		
	}

	static int checkStyle(int style) {
		int mask = SWT.BORDER | SWT.READ_ONLY | SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		return style & mask;
	}

	public void setDateFormat(DateFormat format) {
		dateFormat = format;
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void addModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Modify, typedListener);
	}

	public void addSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Selection, typedListener);
		addListener(SWT.DefaultSelection, typedListener);
	}

	public void addVerifyListener(VerifyListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Verify, typedListener);
	}

	public void clearSelection() {
		checkWidget();
		text.clearSelection();
	}

	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		int width = 0, height = 0;

		GC gc = new GC(text);
		int spacer = gc.stringExtent(" ").x; //$NON-NLS-1$
		String str = text.getText();
		int textWidth = 50;
		if (str != null && str.length() > 0)
			textWidth = gc.stringExtent(str).x;
		gc.dispose();
		Point textSize = text.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		Point arrowSize = arrow.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		int borderWidth = getBorderWidth();

		height = Math.max(textSize.y, arrowSize.y);
		width = textWidth + 2 * spacer + arrowSize.x + 2 * borderWidth;
		if (wHint != SWT.DEFAULT)
			width = wHint;
		if (hHint != SWT.DEFAULT)
			height = hHint;
		return new Point(width + 2 * borderWidth, height + 2 * borderWidth);
	}

	public void copy() {
		checkWidget();
		text.copy();
	}

	void createPopup(Calendar cal) {
		popup = new DatePopup(getShell());
		popup.setCalendar(cal);
	}

	public Calendar getSelection() {
		return calendar;
	}

	public void setSelection(Calendar cal) {
		this.calendar = cal;
		if(this.calendar==null){
			clearSelection();
		}else{
			String txt = dateFormat.format(calendar.getTime());
			text.setText(txt);
		}
	}
	private Calendar calendar;
}
