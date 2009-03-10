
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

package org.dyno.visual.swing.base;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

public class ResourceImage extends Image {
	private String path;
	private Image image;
	public ResourceImage(Image image, String path){
		this.path = path;
		this.image = image;	
		if(image==null)
			throw new IllegalArgumentException(Messages.RESOURCE_IMAGE_CANNOT_FIND_IMG_PATH+path);
	}
	public ResourceImage(String p) {
		this.path = p;
		IJavaProject prj = VisualSwingPlugin.getCurrentProject();
		IProject project = prj.getProject();
		IResource resource = project.findMember(new Path(p));
		if (resource == null) {
			IPackageFragmentRoot[] roots;
			try {
				roots = prj.getPackageFragmentRoots();
				for (IPackageFragmentRoot root : roots) {
					if (!root.isArchive()) {
						String src = root.getElementName();
						src = "/" + src + p; //$NON-NLS-1$
						resource = project.findMember(new Path(src));
						if (resource != null) {
							String ext = resource.getFileExtension();
							if (ext.equals("gif") || ext.equals("png") || ext.equals("jpg")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								IPath fullPath = project.getWorkspace().getRoot().getRawLocation().append(resource.getFullPath());
								String fullpath = fullPath.toString();
								image = Toolkit.getDefaultToolkit().getImage(fullpath);
							} else {
								break;
							}
						}
					}
				}
			} catch (JavaModelException e) {
				VisualSwingPlugin.getLogger().error(e);
			}
		}
		if(image==null)
			throw new IllegalArgumentException(Messages.RESOURCE_IMAGE_CANNOT_FIND_IMG_PATH+p);
	}

	public String toString() {
		return path;
	}
	@Override
	public Graphics getGraphics() {
		return image.getGraphics();
	}
	@Override
	public int getHeight(ImageObserver observer) {
		return image.getWidth(observer);
	}
	@Override
	public Object getProperty(String name, ImageObserver observer) {
		return image.getProperty(name, observer);
	}
	@Override
	public ImageProducer getSource() {
		return image.getSource();
	}
	@Override
	public int getWidth(ImageObserver observer) {
		return image.getWidth(observer);
	}
	@Override
	public void flush() {
		image.flush();
	}
	@Override
	public float getAccelerationPriority() {
		return image.getAccelerationPriority();
	}
	
	@Override
	public ImageCapabilities getCapabilities(GraphicsConfiguration gc) {
		return image.getCapabilities(gc);
	}
	@Override
	public Image getScaledInstance(int width, int height, int hints) {
		return image.getScaledInstance(width, height, hints);
	}
	@Override
	public void setAccelerationPriority(float priority) {
		image.setAccelerationPriority(priority);
	}
	public Image getDelegateImage(){
		return image;
	}
}

