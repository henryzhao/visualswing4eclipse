/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types;

import java.net.URL;

import javax.swing.ImageIcon;

import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class IconEditor extends IconWrapper implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		CellEditor editor = new TextCellEditor(parent);
		editor.setValidator(new IconCellEditorValidator());
		return editor;
	}

	@Override
	public Object decodeValue(Object value) {
		if (value == null)
			return null;
		if (value.equals("null"))
			return null;
		String string = (String) value;
		return new ResourceIcon(string);
	}

	@Override
	public Object encodeValue(Object value) {
		if (value == null)
			return "null";
		if (value instanceof ImageIcon) {
			ImageIcon imageIcon = (ImageIcon) value;
			String filename = IconWrapper.getImageIconFilename(imageIcon);
			if (filename != null) {
				return filename;
			} else {
				URL location = IconWrapper.getImageIconLocation(imageIcon);
				if (location != null) {
					IProject prj = WhiteBoard.getCurrentProject().getProject();
					try {
						IFile[] files = prj.getWorkspace().getRoot()
								.findFilesForLocationURI(location.toURI());
						if (files == null || files.length == 0)
							return null;
						return "/"
								+ files[0].getProjectRelativePath()
										.removeFirstSegments(1);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				} else
					return "null";
			}
		} else
			return value.toString();
	}
}
