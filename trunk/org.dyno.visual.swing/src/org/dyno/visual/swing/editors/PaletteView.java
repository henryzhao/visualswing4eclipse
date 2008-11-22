/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.editors;

import java.util.HashMap;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
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
		expandBar = new SharedScrolledComposite(parent, SWT.H_SCROLL
				| SWT.V_SCROLL) {
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
		if (selectedItem != null && selectedItem != item
				&& selectedItem.getSelection()) {
			selectedItem.setSelection(false);
		}
		if (item.getSelection()) {
			String beanClassname = (String) item.getData();
			WidgetAdapter adapter = ExtensionRegistry
					.createWidgetAdapter(beanClassname);
			WhiteBoard.setSelectedWidget(adapter);
			selectedItem = item;
		} else {
			WhiteBoard.setSelectedWidget(null);
			selectedItem = null;
		}
	}

	private ToolItem selectedItem;
}
