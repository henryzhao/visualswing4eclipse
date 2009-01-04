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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.JavaUtil;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
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
public class LayoutExtensionLibrary implements IClasspathContainer {
	private IPath jar_path;
	private IPath src_path;
	private IPath containerPath;
	public LayoutExtensionLibrary(){
		this(JavaUtil.VS_LAYOUTEXT);
	}
	public LayoutExtensionLibrary(IPath containerPath){
		this.containerPath = containerPath;
		IPath path = Platform.getLocation();
		path = path.append(".metadata"); //$NON-NLS-1$
		path = path.append(".plugins"); //$NON-NLS-1$
		path = path.append(VisualSwingPlugin.getPluginID());
		path = path.append("layoutext");		 //$NON-NLS-1$
		File folder = path.toFile();
		if (!folder.exists())
			folder.mkdirs();		
		File jar = new File(folder, "grouplayout.jar"); //$NON-NLS-1$
		File src = new File(folder, "grouplayout.zip"); //$NON-NLS-1$
		if (!jar.exists()) {
			createFile(jar, "/layoutext/grouplayout.jar"); //$NON-NLS-1$
			createFile(src, "/layoutext/grouplayout.zip"); //$NON-NLS-1$
		}
		jar_path = new Path(jar.getAbsolutePath());
		src_path = new Path(src.getAbsolutePath());
	}

	private void createFile(File out, String resource) {
		byte[] data = new byte[1024];
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
			VisualSwingPlugin.getLogger().error(e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) {
					VisualSwingPlugin.getLogger().error(e);
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
					VisualSwingPlugin.getLogger().error(e);
				}
			}
		}
	}	

	public IClasspathEntry[] getClasspathEntries() {
		IClasspathEntry layoutextlib = JavaCore.newLibraryEntry(jar_path, src_path, new Path("src"), true); //$NON-NLS-1$
		return new IClasspathEntry[] { layoutextlib };
	}

	public String getDescription() {
		return Messages.LayoutExtensionLibrary_Layout_Ext;
	}

	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	public IPath getPath() {
		return containerPath;
	}
	public boolean equals(Object o){
		if(o==null)
			return false;
		if(o instanceof LayoutExtensionLibrary)
			return true;
		else
			return false;
	}
}

