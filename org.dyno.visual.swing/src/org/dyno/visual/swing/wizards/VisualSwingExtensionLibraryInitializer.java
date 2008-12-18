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

package org.dyno.visual.swing.wizards;

import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.ILibraryExtension;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * 
 * VsLayoutContainerInitializer
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class VisualSwingExtensionLibraryInitializer extends
		ClasspathContainerInitializer {

	public VisualSwingExtensionLibraryInitializer() {
	}

	@Override
	public void initialize(IPath containerPath, IJavaProject project)
			throws CoreException {
		List<IClasspathContainer> cons = new ArrayList<IClasspathContainer>();
		if (containerPath.equals(JavaUtil.VS_LAYOUTEXT))
			cons.add(new LayoutExtensionLibrary(containerPath));
		List<ILibraryExtension> libExts = ExtensionRegistry.getLibExtensions();
		for (ILibraryExtension libExt : libExts) {
			IClasspathContainer con = libExt.createLibExt(containerPath);
			System.out.println();
			if (con != null) {
				cons.add(con);
			}
		}
		if (!cons.isEmpty()) {
			IClasspathContainer[] cons_array = new IClasspathContainer[cons
					.size()];
			cons.toArray(cons_array);
			IJavaProject[] projects = new IJavaProject[] { project };
			JavaCore.setClasspathContainer(containerPath, projects, cons_array,
					null);
		}
	}
}
