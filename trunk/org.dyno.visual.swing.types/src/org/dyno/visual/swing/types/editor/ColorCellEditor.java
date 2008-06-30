package org.dyno.visual.swing.types.editor;

import java.text.MessageFormat;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

public class ColorCellEditor extends DialogCellEditor {
	private static final int GAP = 6;
	private Composite composite;
	private Text rgbText;
	private ModifyListener modifyListener;

	private class ColorCellLayout extends Layout {
		public Point computeSize(Composite editor, int wHint, int hHint, boolean force) {
			if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
			Point rgbSize = rgbText.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			return new Point(GAP + rgbSize.x, rgbSize.y);
		}

		public void layout(Composite editor, boolean force) {
			Rectangle bounds = editor.getClientArea();
			Point rgbSize = rgbText.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			int ty = (bounds.height - rgbSize.y) / 2;
			if (ty < 0) {
				ty = 0;
			}
			rgbText.setBounds(GAP - 1, ty, bounds.width - GAP, bounds.height);
		}
	}

	public ColorCellEditor(Composite parent) {
		this(parent, SWT.NONE);
	}

	public ColorCellEditor(Composite parent, int style) {
		super(parent, style);
		doSetValue(new RGB(0, 0, 0));
		setValidator(new ColorCellEditorValidator());
	}

	@Override
	protected void doSetFocus() {
		rgbText.setFocus();
		rgbText.selectAll();
	}

	protected Control createContents(Composite cell) {
		Color bg = cell.getBackground();
		composite = new Composite(cell, getStyle());
		composite.setBackground(bg);
		composite.setLayout(new ColorCellLayout());
		rgbText = new Text(composite, SWT.SINGLE);
		rgbText.setBackground(bg);
		rgbText.setFont(cell.getFont());
		rgbText.addModifyListener(getModifyListener());
		rgbText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				colorTextChange();
			}
		});
		return composite;
	}

	private void colorTextChange() {
		String text = rgbText.getText();
		boolean newValidState = isCorrect(text);
		if (newValidState) {
			markDirty();
			doSetValue(decodeRGB(text));
		} else {
			setErrorMessage(MessageFormat.format(getErrorMessage(), new Object[] { text }));
		}
		fireApplyEditorValue();
	}

	private ModifyListener getModifyListener() {
		if (modifyListener == null) {
			modifyListener = new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					editOccured(e);
				}
			};
		}
		return modifyListener;
	}

	protected void editOccured(ModifyEvent e) {
		String value = rgbText.getText();
		if (value == null) {
			value = "";//$NON-NLS-1$
		}
		Object typedValue = value;
		boolean oldValidState = isValueValid();
		boolean newValidState = isCorrect(typedValue);
		if (typedValue == null && newValidState) {
			Assert.isTrue(false, "Validator isn't limiting the cell editor's type range");//$NON-NLS-1$
		}
		if (!newValidState) {
			// try to insert the current value into the error message.
			setErrorMessage(MessageFormat.format(getErrorMessage(), new Object[] { value }));
		}
		valueChanged(oldValidState, newValidState);
	}

	protected Object openDialogBox(Control cellEditorWindow) {
		ColorDialog dialog = new ColorDialog(cellEditorWindow.getShell());
		Object value = getValue();
		if (value != null) {
			dialog.setRGB((RGB) value);
		}
		value = dialog.open();
		return dialog.getRGB();
	}

	protected Button createButton(Composite parent) {
		Button button = super.createButton(parent);
		button.setText(".");
		return button;
	}

	protected void updateContents(Object value) {
		RGB rgb = (RGB) value;
		String text;
		if (rgb == null) {
			text = "null";
		} else {
			text = encodeRGB(rgb);
		}
		rgbText.setText(text);
	}

	private static String encodeRGB(RGB rgb) {
		return "(" + rgb.red + ", " + rgb.green + ", " + rgb.blue + ")";
	}

	private static RGB decodeRGB(String string) {
		string = string.trim();
		string = string.substring(1, string.length() - 1);
		StringTokenizer tokenizer = new StringTokenizer(string, ",");
		String sRed = tokenizer.nextToken().trim();
		String sGreen = tokenizer.nextToken().trim();
		String sBlue = tokenizer.nextToken().trim();
		int red = 0;
		int green = 0;
		int blue = 0;
		try {
			red = Integer.parseInt(sRed);
			green = Integer.parseInt(sGreen);
			blue = Integer.parseInt(sBlue);
		} catch (NumberFormatException nfe) {
		}
		return new RGB(red, green, blue);
	}
}
