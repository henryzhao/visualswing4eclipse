
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

package org.dyno.visual.swing.lnfs;

import org.dyno.visual.swing.base.PluginLogger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class LnfPlugin extends AbstractUIPlugin {
	// The shared instance
	private static final String PLUGIN_ID="org.dyno.visual.swing.lnfs";
	private static LnfPlugin plugin;
	private PluginLogger logger;	
	/**
	 * The constructor
	 */
	public LnfPlugin() {
	}
	public static String getPluginID(){
		return PLUGIN_ID;
	}
	public static PluginLogger getLogger(){
		if(plugin==null||plugin.logger==null){
			return PluginLogger.instance;
		}
		return plugin.logger;
	}
	public static LnfPlugin getDefault(){
		return plugin;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		logger = new PluginLogger(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static Image getSharedImage(String path) {
		ImageRegistry registry = getDefault().getImageRegistry();
		ImageDescriptor image_descriptor = registry.getDescriptor(path);
		if (image_descriptor == null) {
			image_descriptor = getImageDescriptor(path);
			registry.put(path, image_descriptor);
		}
		return registry.get(path);
	}

	public static ImageDescriptor getSharedDescriptor(String path) {
		ImageRegistry registry = getDefault().getImageRegistry();
		ImageDescriptor image_descriptor = registry.getDescriptor(path);
		if (image_descriptor == null) {
			image_descriptor = getImageDescriptor(path);
			registry.put(path, image_descriptor);
		}
		return image_descriptor;
	}

	public static Image getSharedImage(String pluginId, String path) {
		String iconKey = pluginId + path;
		ImageRegistry registry = getDefault().getImageRegistry();
		ImageDescriptor image_descriptor = registry.getDescriptor(iconKey);
		if (image_descriptor == null) {
			Bundle bundle = Platform.getBundle(pluginId);
			image_descriptor = ImageDescriptor.createFromURL(bundle.getResource(path));
			registry.put(iconKey, image_descriptor);
		}
		return registry.get(iconKey);
	}	
}

