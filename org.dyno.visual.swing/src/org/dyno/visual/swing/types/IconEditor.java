
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

import java.net.URL;

import javax.swing.ImageIcon;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.base.ResourceIcon;
import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public class IconEditor extends IconWrapper implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		return new IconCellEditor(parent);
	}

	@Override
	public Object decodeValue(Object value) {
		if (value == null)
			return null;
		String string = (String) value;
		return new ResourceIcon(string);
	}

	@Override
	public Object encodeValue(Object value) {
		if (value == null)
			return null;
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
						VisualSwingPlugin.getLogger().error(e);
						return null;
					}
				} else
					return null;
			}
		} else
			return value.toString();
	}
}

