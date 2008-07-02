package org.dyno.visual.swing.types.editor;

import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

public class SpinnerModelCellEditor extends DialogCellEditor {
	private static final int GAP = 6;
	private Composite composite;
	private Label lblText;

	private class ColorCellLayout extends Layout {
		public Point computeSize(Composite editor, int wHint, int hHint, boolean force) {
			if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
			Point rgbSize = lblText.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			return new Point(GAP + rgbSize.x, rgbSize.y);
		}

		public void layout(Composite editor, boolean force) {
			Rectangle bounds = editor.getClientArea();
			Point rgbSize = lblText.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			int ty = (bounds.height - rgbSize.y) / 2;
			if (ty < 0) {
				ty = 0;
			}
			lblText.setBounds(GAP - 1, ty, bounds.width - GAP, bounds.height);
		}
	}

	public SpinnerModelCellEditor(Composite parent) {
		this(parent, SWT.NONE);
	}

	public SpinnerModelCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	protected Control createContents(Composite cell) {
		Color bg = cell.getBackground();
		composite = new Composite(cell, getStyle());
		composite.setBackground(bg);
		composite.setLayout(new ColorCellLayout());
		lblText = new Label(composite, SWT.LEFT);
		lblText.setBackground(bg);
		lblText.setFont(cell.getFont());
		return composite;
	}

	protected Object openDialogBox(Control cellEditorWindow) {
		SpinnerModelDialog dialog = new SpinnerModelDialog(cellEditorWindow.getShell());
		Object value = getValue();
		dialog.setSpinnerModel((SpinnerModel)value);
		int retcode = dialog.open();
		if (retcode == Dialog.CANCEL)
			return null;
		else
			return dialog.getSpinnerModel();
	}

	protected Button createButton(Composite parent) {
		Button button = super.createButton(parent);
		button.setText(".");
		return button;
	}

	protected void updateContents(Object value) {
		lblText.setText(getText(value));
	}
	public String getText(Object value) {
		if (value == null)
			return "null";
		if (value instanceof SpinnerModel) {
			if (value instanceof SpinnerDateModel) {
				return "SpinnerDateModel";
			} else if (value instanceof SpinnerNumberModel) {
				return "SpinnerNumberModel";
			} else if (value instanceof SpinnerListModel) {
				return "SpinnerListModel";
			}
		}
		return value.toString();
	}
}
