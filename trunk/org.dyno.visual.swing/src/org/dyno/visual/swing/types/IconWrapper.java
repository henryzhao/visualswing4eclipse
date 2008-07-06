/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types;

import java.lang.reflect.Field;
import java.net.URL;

import javax.swing.ImageIcon;

import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
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
			e.printStackTrace();
		}
	}

	public static String getImageIconFilename(Object icon) {
		try {
			return (String) FILENAME_FIELD.get(icon);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void setImageIconFilename(Object icon, String filename) {
		try {
			FILENAME_FIELD.set(icon, filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static URL getImageIconLocation(Object icon) {
		try {
			return (URL) LOCATION_FIELD.get(icon);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void setImageIconLocation(Object icon, URL url) {
		try {
			LOCATION_FIELD.set(icon, url);
		} catch (Exception e) {
			e.printStackTrace();
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
					String root = prj.getWorkspace().getRoot().getRawLocation().toString();
					String path = location.getFile().toString();
					path = path.substring(1);
					path = path.substring(root.length());
					Path ipath = new Path(path);
					rel = "/" + ipath.removeFirstSegments(2);
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
