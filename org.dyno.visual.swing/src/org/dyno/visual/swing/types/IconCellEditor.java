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

package org.dyno.visual.swing.types;

import java.text.MessageFormat;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.ImageSelectionDialog;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
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

public class IconCellEditor extends DialogCellEditor {
	private static final int GAP = 6;
	private Composite composite;
	private Text iconText;
	private ModifyListener modifyListener;

	private class IconCellLayout extends Layout {
		public Point computeSize(Composite editor, int wHint, int hHint,
				boolean force) {
			if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
			Point rgbSize = iconText.computeSize(SWT.DEFAULT, SWT.DEFAULT,
					force);
			return new Point(GAP + rgbSize.x, rgbSize.y);
		}

		public void layout(Composite editor, boolean force) {
			Rectangle bounds = editor.getClientArea();
			Point rgbSize = iconText.computeSize(SWT.DEFAULT, SWT.DEFAULT,
					force);
			int ty = (bounds.height - rgbSize.y) / 2;
			if (ty < 0) {
				ty = 0;
			}
			iconText.setBounds(GAP - 1, ty, bounds.width - GAP, bounds.height);
		}
	}

	public IconCellEditor(Composite parent) {
		this(parent, SWT.NONE);
	}

	public IconCellEditor(Composite parent, int style) {
		super(parent, style);
		setValidator(new ImageIconValidator());
	}

	@Override
	protected void doSetFocus() {
		iconText.setFocus();
		iconText.selectAll();
	}

	protected Control createContents(Composite cell) {
		Color bg = cell.getBackground();
		composite = new Composite(cell, getStyle());
		composite.setBackground(bg);
		composite.setLayout(new IconCellLayout());
		iconText = new Text(composite, SWT.SINGLE);
		iconText.setBackground(bg);
		iconText.setFont(cell.getFont());
		iconText.addModifyListener(getModifyListener());
		iconText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				fontTextChanged();
			}
		});
		return composite;
	}

	private void fontTextChanged() {
		String text = iconText.getText();
		boolean newValidState = isCorrect(text);
		if (newValidState) {
			markDirty();
			doSetValue(text);
		} else {
			setErrorMessage(MessageFormat.format(getErrorMessage(),
					new Object[] { text }));
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
		String value = iconText.getText();
		if (value == null) {
			value = "";//$NON-NLS-1$
		}
		Object typedValue = value;
		boolean oldValidState = isValueValid();
		boolean newValidState = isCorrect(typedValue);
		if (typedValue == null && newValidState) {
			Assert.isTrue(false,
					"Validator isn't limiting the cell editor's type range");//$NON-NLS-1$
		}
		if (!newValidState) {
			// try to insert the current value into the error message.
			setErrorMessage(MessageFormat.format(getErrorMessage(),
					new Object[] { value }));
		}
		valueChanged(oldValidState, newValidState);
	}

	private IPackageFragmentRoot getSourceRoot(IJavaProject prj) {
		try {
			IJavaElement[] children = prj.getChildren();
			for (IJavaElement child : children) {
				if (child instanceof IPackageFragmentRoot) {
					IPackageFragmentRoot childRoot = (IPackageFragmentRoot) child;
					if (!childRoot.isArchive())
						return childRoot;
				}
			}
		} catch (JavaModelException e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		return null;
	}

	protected Object openDialogBox(Control cellEditorWindow) {
		try {
			ImageSelectionDialog isd = new ImageSelectionDialog(
					cellEditorWindow.getShell());
			String text = iconText.getText();
			if (text != null && text.trim().length() > 0) {
				IJavaProject javaProject = VisualSwingPlugin.getCurrentProject();
				if (javaProject != null) {
					IPackageFragmentRoot src_root = getSourceRoot(javaProject);
					if (src_root != null) {
						String srcName = src_root.getElementName();
						IProject prj = javaProject.getProject();
						IFile file = prj.getFolder(srcName).getFile(text);
						if (file != null && file.exists())
							isd.setImageFile(file);
					}
				}
			}
			int ret = isd.open();
			if (ret == Window.OK) {
				IFile file = isd.getImageFile();
				if (file == null) {
					return "";
				} else {
					IPath location = file.getFullPath();
					location = location.removeFirstSegments(2);
					String path = location.toString();
					if (path.startsWith("/"))
						return path;
					else
						return "/" + path;
				}
			}
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		return null;
	}

	protected Button createButton(Composite parent) {
		Button result = super.createButton(parent);
		result.setText(".");
		return result;
	}

	protected void updateContents(Object value) {
		iconText.setText(value == null ? "" : (String) value);
	}
}
