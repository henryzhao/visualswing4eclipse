
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
		if (value == null || value.equals("")) //$NON-NLS-1$
			return null;
		StringTokenizer tokenizer = new StringTokenizer((String) value, "/"); //$NON-NLS-1$
		if (!tokenizer.hasMoreTokens()) {
			return Messages.ImageIconValidator_Incorrect_Icon_Image_Format;
		}
		do {
			String token = tokenizer.nextToken().trim();
			if (token.length() == 0)
				return Messages.ImageIconValidator_Incorrect_Icon_Image_Format;
			if (tokenizer.hasMoreTokens()) {
				char c = token.charAt(0);
				if (!Character.isJavaIdentifierStart(c)) {
					return Messages.ImageIconValidator_Incorrect_Icon_Image_Format_Segment_Id;
				}
				int i = 0;
				while (true) {
					c = token.charAt(i++);
					if (!Character.isJavaIdentifierPart(c) && c != '.')
						return Messages.ImageIconValidator_Incorrect_Icon_Image_Format_Segment_Id;
					if (i >= token.length())
						break;
				}
			}
		} while (tokenizer.hasMoreTokens());
		IJavaProject prj = VisualSwingPlugin.getCurrentProject();
		IProject project = prj.getProject();
		IResource resource = project.findMember(new Path((String) value));
		if (resource == null) {
			IPackageFragmentRoot[] roots;
			try {
				roots = prj.getPackageFragmentRoots();
				for (IPackageFragmentRoot root : roots) {
					if (!root.isArchive()) {
						String src = root.getElementName();
						src = "/" + src + value; //$NON-NLS-1$
						resource = project.findMember(new Path(src));
						if (resource != null) {
							String ext = resource.getFileExtension();
							if (ext!=null&&(ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg"))) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								return null;
							else
								return Messages.ImageIconValidator_Not_Image_File + value;
						}
					}
				}
			} catch (JavaModelException e) {
				VisualSwingPlugin.getLogger().error(e);
				return e.getLocalizedMessage();
			}
			return Messages.ImageIconValidator_Cannot_Find_Such_Image_File + value + "!"; //$NON-NLS-2$
		}
		return null;
	}
}

