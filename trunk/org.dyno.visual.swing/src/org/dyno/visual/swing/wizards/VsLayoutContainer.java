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

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
/**
 * 
 * VsLayoutContainer
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class VsLayoutContainer implements IClasspathContainer {
	private Path jar_path;
	private Path src_path;

	public VsLayoutContainer(Path jar, Path src) {
		this.jar_path = jar;
		this.src_path = src;
	}

	public IClasspathEntry[] getClasspathEntries() {
		IClasspathEntry layoutextlib = JavaCore.newLibraryEntry(jar_path, src_path, new Path("src"), true);
		return new IClasspathEntry[] { layoutextlib };
	}

	public String getDescription() {
		return "GroupLayout Extension Library";
	}

	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	public IPath getPath() {
		return new Path("VS_LAYOUT");
	}
}

