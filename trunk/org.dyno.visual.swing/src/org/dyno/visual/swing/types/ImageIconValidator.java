
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

import java.util.StringTokenizer;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.WhiteBoard;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ICellEditorValidator;

public class ImageIconValidator implements ICellEditorValidator {
	@Override
	public String isValid(Object value) {
		if (value == null || value.equals("null"))
			return null;
		StringTokenizer tokenizer = new StringTokenizer((String) value, "/");
		if (!tokenizer.hasMoreTokens()) {
			return "Incorrect icon/image format: [/]package_segment1/package_segment2/package_segment3/.../package_segmentn/file_name";
		}
		do {
			String token = tokenizer.nextToken().trim();
			if (token.length() == 0)
				return "Incorrect icon/image format: [/]package_segment1/package_segment2/package_segment3/.../package_segmentn/file_name";
			char c = token.charAt(0);
			if (!Character.isJavaIdentifierStart(c)) {
				return "Incorrect icon/image format: package segment name should be a identifier!";
			}
			int i = 0;
			while (true) {
				c = token.charAt(i++);
				if (!Character.isJavaIdentifierPart(c) && c != '.')
					return "Incorrect icon/image format: package segment name should be a identifier!";
				if (i >= token.length())
					break;
			}
		} while (tokenizer.hasMoreTokens());
		IJavaProject prj = WhiteBoard.getCurrentProject();
		IProject project = prj.getProject();
		IResource resource = project.findMember(new Path((String) value));
		if (resource == null) {
			IPackageFragmentRoot[] roots;
			try {
				roots = prj.getPackageFragmentRoots();
				for (IPackageFragmentRoot root : roots) {
					if (!root.isArchive()) {
						String src = root.getElementName();
						src = "/" + src + value;
						resource = project.findMember(new Path(src));
						if (resource != null) {
							String ext = resource.getFileExtension();
							if (ext!=null&&(ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg")))
								return null;
							else
								return "This is not an image file:" + value;
						}
					}
				}
			} catch (JavaModelException e) {
				VisualSwingPlugin.getLogger().error(e);
				return e.getLocalizedMessage();
			}
			return "Cannot find such image file:" + value + "!";
		}
		return null;
	}
}

