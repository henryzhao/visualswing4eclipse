/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.wizards;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
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
public class VsLayoutContainerInitializer extends ClasspathContainerInitializer {
	private byte[] data;
	private File jar;
	private File src;

	public VsLayoutContainerInitializer() {
		IPath path = Platform.getLocation();
		path = path.append("layoutext");
		File folder = path.toFile();
		if (!folder.exists())
			folder.mkdirs();
		jar = new File(folder, "grouplayout.jar");
		src = new File(folder, "grouplayout.zip");
		if (!jar.exists()) {
			createFile(jar, "/layoutext/grouplayout.jar");
			createFile(src, "/layoutext/grouplayout.zip");
		}
	}

	private void createFile(File out, String resource) {
		if (data == null)
			data = new byte[1024];
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(getClass().getResourceAsStream(resource));
			bos = new BufferedOutputStream(new FileOutputStream(out));
			int length;
			while ((length = bis.read(data)) > 0) {
				bos.write(data, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
		Path jar_path = new Path(jar.getAbsolutePath());
		Path src_path = new Path(src.getAbsolutePath());
		IClasspathContainer con = new VsLayoutContainer(jar_path, src_path);
		IClasspathContainer[] cons = new IClasspathContainer[] { con };
		Path vsPath = new Path("VS_LAYOUT");
		IJavaProject[] projects = new IJavaProject[] { project };
		JavaCore.setClasspathContainer(vsPath, projects, cons, null);
	}

}
