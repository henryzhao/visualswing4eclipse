
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

import java.lang.reflect.Field;
import java.net.URL;

import javax.swing.ImageIcon;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.base.ResourceIcon;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class IconWrapper implements ICodeGen {
	private static Field FILENAME_FIELD;
	private static Field LOCATION_FIELD;
	static {
		try {
			FILENAME_FIELD = ImageIcon.class.getDeclaredField("filename");
			FILENAME_FIELD.setAccessible(true);
			LOCATION_FIELD = ImageIcon.class.getDeclaredField("location");
			LOCATION_FIELD.setAccessible(true);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
	}

	public static String getImageIconFilename(Object icon) {
		try {
			return (String) FILENAME_FIELD.get(icon);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
			return null;
		}
	}

	public static void setImageIconFilename(Object icon, String filename) {
		try {
			FILENAME_FIELD.set(icon, filename);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
	}

	public static URL getImageIconLocation(Object icon) {
		try {
			return (URL) LOCATION_FIELD.get(icon);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
			return null;
		}
	}

	public static void setImageIconLocation(Object icon, URL url) {
		try {
			LOCATION_FIELD.set(icon, url);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		String rel = null;
		if (value == null) {
			return null;
		} else if (value instanceof ResourceIcon) {
			rel = value.toString();
		} else if (value instanceof ImageIcon) {
			ImageIcon imageIcon = (ImageIcon) value;
			String filename = IconWrapper.getImageIconFilename(imageIcon);
			if (filename != null) {
				rel = filename;
			} else {
				URL location = IconWrapper.getImageIconLocation(imageIcon);
				if (location != null) {
					IProject prj = WhiteBoard.getCurrentProject().getProject();
					try {
						IFile[] files = prj.getWorkspace().getRoot()
								.findFilesForLocationURI(location.toURI());
						if (files == null || files.length == 0)
							return null;
						rel = "/"
								+ files[0].getProjectRelativePath()
										.removeFirstSegments(1);
					} catch (Exception e) {
						VisualSwingPlugin.getLogger().error(e);
						return null;
					}
				} else
					return null;
			}
		}
		if (rel != null) {
			String strImageIcon = imports.addImport("javax.swing.ImageIcon");
			return "new " + strImageIcon + "(getClass().getResource(\"" + rel + "\"))";
		} else
			return null;
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}
}

