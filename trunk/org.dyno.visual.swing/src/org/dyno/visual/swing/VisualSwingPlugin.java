/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing;

import java.util.HashMap;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
/**
 * 
 * VisualSwingPlugin
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class VisualSwingPlugin extends AbstractUIPlugin {
	private static final String DRAGGING_CURSOR_ICON = "/icons/dragging.gif";
	private Cursor draggingGesture;
	private HashMap<Integer, Cursor> cursors;
	private Display display;

	private void initializeCursor() {
		display = PlatformUI.getWorkbench().getDisplay();
		draggingGesture = new Cursor(display, VisualSwingPlugin.getSharedImage(DRAGGING_CURSOR_ICON).getImageData(), 0, 0);
		cursors = new HashMap<Integer, Cursor>();
	}

	// The plug-in ID
	public static final String PLUGIN_ID = "org.dyno.visual.swing";

	public static String getPluginID() {
		return PLUGIN_ID;
	}

	// The shared instance
	private static VisualSwingPlugin plugin;

	/**
	 * The constructor
	 */
	public VisualSwingPlugin() {
	}

	public Cursor getCursor(int style) {
		Cursor cursor = cursors.get(style);
		if (cursor == null) {
			cursor = new Cursor(display, style);
			cursors.put(style, cursor);
		}
		return cursor;
	}

	public Cursor getDraggingGesture() {
		return draggingGesture;
	}

	public static IWorkbenchPage getActivePage() {
		IWorkbenchWindow window = VisualSwingPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
			return null;
		return window.getActivePage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		initializeCursor();		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		if (draggingGesture != null) {
			draggingGesture.dispose();
			draggingGesture = null;
		}
		for (Cursor cursor : cursors.values()) {
			cursor.dispose();
		}
		cursors = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static VisualSwingPlugin getDefault() {
		return plugin;
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
