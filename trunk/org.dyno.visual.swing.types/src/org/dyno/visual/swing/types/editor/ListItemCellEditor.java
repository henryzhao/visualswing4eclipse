/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor;

import java.text.MessageFormat;
import java.util.StringTokenizer;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

public class ListItemCellEditor extends DialogCellEditor {
	private static final int GAP = 6;
	private Composite composite;
	private Text lstText;
	private ModifyListener modifyListener;

	private class ColorCellLayout extends Layout {
		public Point computeSize(Composite editor, int wHint, int hHint, boolean force) {
			if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
			Point rgbSize = lstText.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			return new Point(GAP + rgbSize.x, rgbSize.y);
		}

		public void layout(Composite editor, boolean force) {
			Rectangle bounds = editor.getClientArea();
			Point rgbSize = lstText.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			int ty = (bounds.height - rgbSize.y) / 2;
			if (ty < 0) {
				ty = 0;
			}
			lstText.setBounds(GAP - 1, ty, bounds.width - GAP, bounds.height);
		}
	}

	public ListItemCellEditor(Composite parent) {
		this(parent, SWT.NONE);
	}

	public ListItemCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void doSetFocus() {
		lstText.setFocus();
		lstText.selectAll();
	}

	protected Control createContents(Composite cell) {
		Color bg = cell.getBackground();
		composite = new Composite(cell, getStyle());
		composite.setBackground(bg);
		composite.setLayout(new ColorCellLayout());
		lstText = new Text(composite, SWT.SINGLE);
		lstText.setBackground(bg);
		lstText.setFont(cell.getFont());
		lstText.addModifyListener(getModifyListener());
		lstText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				itemTextChanged();
			}
		});
		return composite;
	}

	private void itemTextChanged() {
		String text = lstText.getText();
		boolean newValidState = isCorrect(text);
		if (newValidState) {
			markDirty();
			doSetValue(decodeModel(text));
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
		String value = lstText.getText();
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
		ListItemDialog dialog = new ListItemDialog(cellEditorWindow.getShell());
		Object value = getValue();
		dialog.setListItemModel((ListModel) value);
		dialog.setListModelAdapter(getAdapter());
		int retcode = dialog.open();
		if (retcode == Dialog.CANCEL)
			return null;
		else
			return dialog.getListItemModel();
	}

	@Override
	protected void doSetValue(Object value) {
		super.doSetValue(value);
		if (value == null || value instanceof ComboBoxModel) {
			adapter = new ListModelAdapter() {
				@Override
				public void addElement(ListModel model, Object element) {
					((DefaultComboBoxModel) model).addElement(element);
				}

				@Override
				public ListModel newModel() {
					return new DefaultComboBoxModel();
				}
			};
		} else if (value instanceof ListModel) {
			adapter = new ListModelAdapter() {
				@Override
				public void addElement(ListModel model, Object element) {
					((DefaultListModel) model).addElement(element);
				}

				@Override
				public ListModel newModel() {
					return new DefaultListModel();
				}
			};
		}
	}

	protected Button createButton(Composite parent) {
		Button button = super.createButton(parent);
		button.setText(".");
		return button;
	}

	protected void updateContents(Object value) {
		ListModel model = (ListModel) value;
		String text;
		if (model == null) {
			text = "null";
		} else {
			text = encodeModel(model);
		}
		lstText.setText(text);
	}

	private static String encodeModel(ListModel model) {
		if (model == null)
			return "null";
		StringBuilder builder = new StringBuilder();
		int size = model.getSize();
		for (int i = 0; i < size; i++) {
			Object object = model.getElementAt(i);
			if (i != 0) {
				builder.append(", ");
			}
			if (object == null) {
				builder.append("null");
			} else {
				builder.append(object.toString());
			}
		}
		return builder.toString();
	}

	protected ListModel decodeModel(String string) {
		if (string == null || string.trim().length() == 0)
			return null;
		if (string.equals("null"))
			return null;
		StringTokenizer tokenizer = new StringTokenizer(string, ",");
		ListModelAdapter adapter = getAdapter();
		ListModel model = adapter.newModel();
		while (tokenizer.hasMoreTokens()) {
			adapter.addElement(model, tokenizer.nextToken().trim());
		}
		return model;
	}

	protected ListModelAdapter getAdapter() {
		return adapter;
	}

	private ListModelAdapter adapter;
}
