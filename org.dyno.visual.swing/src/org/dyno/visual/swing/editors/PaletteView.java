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

package org.dyno.visual.swing.editors;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.SharedScrolledComposite;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;

/**
 * 
 * PaletteView
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class PaletteView extends ViewPart implements SelectionListener {
	private static final String ADAPTER_EXTENSION_POINT = "org.dyno.visual.swing.widgetAdapter";
	private static final String GROUP_EXTENSION_POINT = "org.dyno.visual.swing.widgetGroup";
	public static PaletteView singleton;
	private HashMap<String, ToolBar> toolbars;
	private HashMap<String, ExpandableComposite> expandItems;
	private SharedScrolledComposite expandBar;

	public PaletteView() {
		singleton = this;
	}

	public static PaletteView getSingleton() {
		return singleton;
	}

	public void createPartControl(Composite parent) {
		expandBar = new SharedScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL) {
		};
		expandBar.setExpandHorizontal(true);
		expandBar.setExpandVertical(true);
		Composite container = new Composite(expandBar, SWT.NONE);
		expandBar.setContent(container);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		toolbars = new HashMap<String, ToolBar>();
		expandItems = new HashMap<String, ExpandableComposite>();
		parseGroupExtensions(container);
		parseWidgetExtensions();

		ExpandableComposite expandItem = new ExpandableComposite(container, SWT.NONE);
		expandItem.setExpanded(false);
		expandItem.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
		expandItem.setText("Custom Component");
		expandItem.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				updateScrolledComposite();
			}
		});
		ToolBar toolBar = new ToolBar(expandItem, SWT.VERTICAL | SWT.RIGHT | SWT.FLAT);
		expandItem.setClient(toolBar);
		ToolItem toolItem = new ToolItem(toolBar, SWT.CHECK);
		toolItem.setText("Custom Swing Component");
		toolItem.setToolTipText("Choose custom component");
		String iconUrl = "icons/beans.png";
		Image img = VisualSwingPlugin.getSharedImage(VisualSwingPlugin.PLUGIN_ID, iconUrl);
		toolItem.setImage(img);
		toolItem.addSelectionListener(this);
		new ToolItem(toolBar, SWT.SEPARATOR);

		updateScrolledComposite();
	}
	public void setFocus() {
		expandBar.setFocus();
	}

	private void parseGroupExtensions(Composite bar) {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
				.getExtensionPoint(GROUP_EXTENSION_POINT);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseGroupExtension(extensions[i], bar);
				}
			}
		}
	}

	private void parseGroupExtension(IExtension extension, Composite bar) {
		IContributor contributor = extension.getContributor();
		String pluginId = contributor.getName();
		Bundle bundle = Platform.getBundle(pluginId);
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("group")) {
					addGroup(configs[i], bundle, bar);
				}
			}
		}
	}

	private void parseWidgetExtensions() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
				.getExtensionPoint(ADAPTER_EXTENSION_POINT);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseWidgetExtension(extensions[i]);
				}
			}
		}
	}

	private void parseWidgetExtension(IExtension extension) {
		IContributor contributor = extension.getContributor();
		String pluginId = contributor.getName();
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("widget")) {
					addWidget(configs[i], pluginId);
				}
			}
		}
	}

	private void addGroup(IConfigurationElement config, Bundle bundle,
			Composite bar) {
		String id = config.getAttribute("id");
		String displayName = config.getAttribute("displayName");
		if (displayName == null || displayName.equals("")) {
			displayName = id;
		}
		ToolBar toolbar = toolbars.get(id);
		if (toolbar == null) {
			ExpandableComposite expandItem = new ExpandableComposite(bar,
					SWT.NONE);
			String sExpanded = config.getAttribute("expanded");
			if (sExpanded == null || sExpanded.trim().length() == 0)
				sExpanded = "true";
			expandItem.setExpanded(sExpanded.equals("true"));
			expandItem.setFont(JFaceResources.getFontRegistry().getBold(
					JFaceResources.DIALOG_FONT));
			expandItem.setText(displayName);
			expandItem.addExpansionListener(new ExpansionAdapter() {
				@Override
				public void expansionStateChanged(ExpansionEvent e) {
					updateScrolledComposite();
				}
			});
			toolbar = new ToolBar(expandItem, SWT.VERTICAL | SWT.RIGHT
					| SWT.FLAT);
			expandItem.setClient(toolbar);
			toolbars.put(id, toolbar);
			expandItems.put(id, expandItem);
		}
	}

	private void updateScrolledComposite() {
		Composite container = (Composite) expandBar.getContent();
		container.layout();
		expandBar.layout();
		expandBar.reflow(true);
	}

	private void addWidget(IConfigurationElement config, String pluginId) {
		String abs = config.getAttribute("abstract");
		if (abs != null && abs.equals("true"))
			return;
		String sRootPaneContainer = config.getAttribute("rootPaneContainer");
		if (sRootPaneContainer != null
				&& sRootPaneContainer.trim().length() > 0
				&& sRootPaneContainer.equals("true"))
			return;
		String sShown = config.getAttribute("shown");
		if (sShown != null && sShown.trim().equals("false"))
			return;
		String groupId = config.getAttribute("groupId");
		if (groupId != null && groupId.trim().length() > 0) {
			ToolBar toolbar = toolbars.get(groupId);
			if (toolbar != null) {
				String widgetClassname = config.getAttribute("widgetClass");
				String widgetName = config.getAttribute("widgetName");
				if (widgetName == null || widgetName.trim().length() == 0)
					widgetName = widgetClassname;
				ToolItem toolItem = new ToolItem(toolbar, SWT.CHECK);
				toolItem.setText(" " + widgetName);
				toolItem.setToolTipText(widgetClassname);
				String iconUrl = config.getAttribute("icon");
				Image img = VisualSwingPlugin.getSharedImage(pluginId, iconUrl);
				toolItem.setImage(img);
				toolItem.addSelectionListener(this);
				toolItem.setData(widgetClassname);
			}
		}
	}

	public static void clearToolSelection() {
		singleton.clearSelection();
	}

	private void clearSelection() {
		expandBar.getDisplay().asyncExec(new ClearRunnable());
	}

	class ClearRunnable implements Runnable {
		@Override
		public void run() {
			if (selectedItem != null && selectedItem.getSelection()) {
				selectedItem.setSelection(false);
				selectedItem = null;
			}
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		ToolItem item = (ToolItem) e.getSource();
		setSelectedItem(item);
	}
	
	private void setSelectedItem(ToolItem item) {

		if (item.getSelection()) {
			String beanClassname = (String) item.getData();
			if (beanClassname != null) {
				WidgetAdapter adapter = ExtensionRegistry.createWidgetAdapter(beanClassname);
				List<WidgetAdapter> list = new ArrayList<WidgetAdapter>();
				list.add(adapter);
				WhiteBoard.setSelectedWidget(list);
			}else{
				Shell parent = getViewSite().getShell();
				try {
					SelectionDialog typeDialog = JavaUI.createTypeDialog(parent, new ProgressMonitorDialog(parent), WhiteBoard.getCurrentProject().getProject(), IJavaElementSearchConstants.CONSIDER_CLASSES, false);
					if(typeDialog.open()==Window.OK){
						Object[] result = typeDialog.getResult();
						if(result!=null&&result.length>0){
							IType type = (IType) result[0];
							String className = type.getElementName();
							IPackageFragment packageFragment = type.getPackageFragment();
							String pkg=packageFragment.getElementName();
							if(pkg!=null&&pkg.trim().length()>0)
								className=pkg+"."+className;
							WhiteBoard.getCurrentProject().getProject().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
							String[] classPath = JavaRuntime.computeDefaultRuntimeClassPath(WhiteBoard.getCurrentProject());
							URL[] urls = new URL[classPath.length];
							for (int i = 0; i < classPath.length; i++) {
								File cp = new File(classPath[i]);
								if (cp.exists())
									urls[i] = cp.toURI().toURL();
							}
							final Class<?> beanClass = new URLClassLoader(urls, getClass().getClassLoader()).loadClass(className);
							if(!JComponent.class.isAssignableFrom(beanClass)){
								MessageDialog.openError(parent, "Error", "Chosen class must be a javax.swing.JComponent derived class!");
								item.setSelection(false);
								return;
							}
							Component bean = (Component)beanClass.newInstance();
							if(bean instanceof Container){
								((Container)bean).doLayout();
							}
							WidgetAdapter beanAdapter = ExtensionRegistry.createWidgetAdapter(bean);
							List<WidgetAdapter> list = new ArrayList<WidgetAdapter>();
							list.add(beanAdapter);
							WhiteBoard.setSelectedWidget(list);
						}
					}
				} catch (Exception e) {
					VisualSwingPlugin.getLogger().error(e);
				}
			}
		} else {
			WhiteBoard.setSelectedWidget(null);
		}
		if (selectedItem != null && selectedItem != item
				&& selectedItem.getSelection()) {
			selectedItem.setSelection(false);
		}
		selectedItem = item.getSelection()?item:null;
	}

	private ToolItem selectedItem;
}

